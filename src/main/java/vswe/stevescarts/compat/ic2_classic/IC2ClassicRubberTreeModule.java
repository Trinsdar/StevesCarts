package vswe.stevescarts.compat.ic2_classic;

import ic2.api.classic.audio.PositionSpec;
import ic2.core.IC2;
import ic2.core.block.resources.BlockRubberWood;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import vswe.stevescarts.api.farms.EnumHarvestResult;
import vswe.stevescarts.api.farms.ITreeProduceModule;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.workers.tools.ModuleTreeTap;
import vswe.stevescarts.modules.workers.tools.ModuleWoodcutter;

/**
 * Created by modmuss50 on 08/05/2017.
 */
public class IC2ClassicRubberTreeModule implements ITreeProduceModule {

	public static final ResourceLocation IC2_SAPLING_NAME = new ResourceLocation("ic2", "blockrubsapling");
	public static final ResourceLocation IC2_LEAF_NAME = new ResourceLocation("ic2", "leaves");
	public static final ResourceLocation IC2_LOG_NAME = new ResourceLocation("ic2", "blockrubwood");

	@Override
	public EnumHarvestResult isLeaves(IBlockState blockState, BlockPos pos, EntityMinecartModular cart) {
		if (blockState.getBlock().getRegistryName().equals(IC2_LEAF_NAME)) {
			if (cart.hasModule(ModuleTreeTap.class)) {
				return EnumHarvestResult.DISALLOW;
			}
			return EnumHarvestResult.ALLOW;
		}
		return EnumHarvestResult.SKIP;
	}

	@Override
	public EnumHarvestResult isWood(IBlockState blockState, BlockPos pos, EntityMinecartModular cart) {
		if (blockState.getBlock().getRegistryName().equals(IC2_LOG_NAME)) {
			return EnumHarvestResult.ALLOW;
		}
		return EnumHarvestResult.SKIP;
	}

	@Override
	public boolean isSapling(ItemStack itemStack) {
		return itemStack.getItem().getRegistryName().equals(IC2_SAPLING_NAME);
	}

	@Override
	public boolean plantSapling(World world, BlockPos pos, ItemStack stack, FakePlayer fakePlayer) {
		Block block = Block.getBlockFromItem(stack.getItem());
		if (block.canPlaceBlockAt(world, pos.up())) {
			world.setBlockState(pos.up(), block.getDefaultState());
			return true;
		}
		return false;
	}

	@Override
	public boolean harvest(IBlockState blockState, BlockPos pos, EntityMinecartModular cart, NonNullList<ItemStack> drops, boolean simulate, ModuleWoodcutter woodcutter) {
		if (!cart.hasModule(ModuleTreeTap.class)) {
			return false;
		}
		BlockPos workPos = pos;
		IBlockState workSate = cart.world.getBlockState(workPos);
		boolean foundBlock = false;
		while (isWood(workSate, workPos, cart) == EnumHarvestResult.ALLOW) {
			if (workSate.getBlock() instanceof BlockRubberWood) {
				foundBlock = true;
				boolean server = IC2.platform.isSimulating();
				if (workSate.getValue(BlockRubberWood.resin) && workSate.getValue(BlockRubberWood.collectable)) {
					drops.add(Ic2Items.stickyResin.copy());
					if (!simulate && server) {
						cart.world.setBlockState(workPos, workSate.withProperty(BlockRubberWood.collectable, false));
						cart.world.scheduleUpdate(workPos, workSate.getBlock(), 100);
						(IC2.network.get(true)).announceBlockUpdate(cart.world, workPos);
						IC2.audioManager.playOnce(cart, PositionSpec.Center, Ic2Sounds.treeTapUse, true, IC2.audioManager.getDefaultVolume());
						woodcutter.damageTool(1);
						woodcutter.startWorking(20);
					}
				}
				workPos = workPos.up();
				workSate = cart.world.getBlockState(workPos);
			}
		}
		return foundBlock;
	}
}

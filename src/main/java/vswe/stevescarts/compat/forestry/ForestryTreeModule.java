package vswe.stevescarts.compat.forestry;

import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.genetics.AlleleManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import vswe.stevescarts.api.farms.EnumHarvestResult;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

/**
 * Created by modmuss50 on 15/11/16.
 */
public class ForestryTreeModule implements ITreeModule {

	public ITreeRoot treeRoot;
	public Item sapling;
	public Block leaves;

	public ForestryTreeModule() {
		treeRoot = (ITreeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");
		sapling = Item.REGISTRY.getObject(new ResourceLocation("forestry", "sapling"));
		leaves = Block.REGISTRY.getObject(new ResourceLocation("forestry", "leaves"));
	}

	@Override
	public EnumHarvestResult isLeaves(IBlockState blockState, BlockPos pos, EntityMinecartModular cart) {
		if(leaves == null || leaves == Blocks.AIR){
			return EnumHarvestResult.SKIP;
		}
		return blockState.getBlock() == leaves ? EnumHarvestResult.ALLOW : EnumHarvestResult.SKIP;
	}

	@Override
	public EnumHarvestResult isWood(IBlockState blockState, BlockPos pos, EntityMinecartModular cart) {
		return blockState.getBlock().getRegistryName().getResourceDomain().equals("forestry") && blockState.getBlock().getRegistryName().getResourcePath().startsWith("logs") ? EnumHarvestResult.ALLOW
		                                                                                                                                                                      : EnumHarvestResult.SKIP;
	}

	@Override
	public boolean isSapling(@Nonnull ItemStack itemStack) {
		if(sapling == null || sapling == Items.AIR){
			return false;
		}
		return itemStack.getItem() == sapling;
	}

	@Override
	public boolean plantSapling(World world, BlockPos pos, @Nonnull ItemStack stack, FakePlayer fakePlayer) {
		if (stack.getItem() == sapling && world.isAirBlock(pos.up())) {
			ITree tree = treeRoot.getMember(stack);
			if (tree != null && tree.canStay(world, pos.up())) {
				treeRoot.plantSapling(world, tree, fakePlayer.getGameProfile(), pos.up());
				return true;
			}
		}
		return false;
	}
}

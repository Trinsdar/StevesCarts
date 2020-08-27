package vswe.stevescarts.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vswe.stevescarts.Constants;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.data.ModuleData;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCartModule extends Item implements ModItems.IMultipleItemModelDefinition {

	public ItemCartModule() {
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(StevesCarts.tabsSC2);
	}

	public String getName(@Nonnull ItemStack par1ItemStack) {
		final ModuleData data = getModuleData(par1ItemStack, true);
		if (data == null) {
			return "Unknown SC2 module";
		}
		return data.getName();
	}

	@Override
	public String getUnlocalizedName() {
		return "item.SC2:unknownmodule";
	}

	@Override
	public String getUnlocalizedName(@Nonnull ItemStack item) {
		final ModuleData data = getModuleData(item, true);
		if (data != null) {
			return "item.SC2:" + data.getRawName();
		}
		return getUnlocalizedName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
		if (!isInCreativeTab(tab)) {
			return;
		}
		for (final ModuleData module : ModuleData.getList().values()) {
			if (!module.getIsLocked() && module.getIsValid()) {
				items.add(module.getItemStack());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, final World world, final List<String> tooltip, ITooltipFlag flag) {
		final ModuleData module = getModuleData(stack, true);
		if (module != null) {
			module.addInformation(tooltip, stack.getTagCompound());
		} else if (!stack.isEmpty() && stack.getItem() instanceof ItemCartModule) {
			tooltip.add("Module id " + stack.getItemDamage());
		} else {
			tooltip.add("Unknown module id");
		}
	}

	public ModuleData getModuleData(@Nonnull ItemStack itemstack) {
		return getModuleData(itemstack, false);
	}

	public ModuleData getModuleData(@Nonnull ItemStack itemstack, final boolean ignoreSize) {
		if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemCartModule && (ignoreSize || TileEntityCartAssembler.getSlotStatus(itemstack) != TileEntityCartAssembler.getRemovedSize())) {
			return ModuleData.getList().get((byte) itemstack.getItemDamage());
		}
		return null;
	}

	public void addExtraDataToCart(final NBTTagCompound save, @Nonnull ItemStack module, final int i) {
		if (module.getTagCompound() != null && module.getTagCompound().hasKey("Data")) {
			save.setByte("Data" + i, module.getTagCompound().getByte("Data"));
		} else {
			final ModuleData data = getModuleData(module, true);
			if (data.isUsingExtraData()) {
				save.setByte("Data" + i, data.getDefaultExtraData());
			}
		}
	}

	public void addExtraDataToModule(final NBTTagCompound save, final ModuleBase module, final int i) {
		if (module.hasExtraData()) {
			save.setByte("Data" + i, module.getExtraData());
		}
	}

	public void addExtraDataToModule(@Nonnull ItemStack module, final NBTTagCompound info, final int i) {
		NBTTagCompound save = module.getTagCompound();
		if (save == null) {
			module.setTagCompound(save = new NBTTagCompound());
		}
		if (info != null && info.hasKey("Data" + i)) {
			save.setByte("Data", info.getByte("Data" + i));
		} else {
			final ModuleData data = getModuleData(module, true);
			if (data.isUsingExtraData()) {
				save.setByte("Data", data.getDefaultExtraData());
			}
		}
	}

	@Override
	public Map<Integer, ResourceLocation> getModels() {
		Map<Integer, ResourceLocation> map = new HashMap<>();
		for (ModuleData data: ModuleData.getList().values()) {
			String name = data.getRawName();
			if (name != null) {
				map.put((int) data.getID(), new ResourceLocation(Constants.MOD_ID, "module_" + name));
			}
		}
		return map;
	}

}

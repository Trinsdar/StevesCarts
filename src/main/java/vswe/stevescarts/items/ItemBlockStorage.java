package vswe.stevescarts.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vswe.stevescarts.StevesCarts;

import javax.annotation.Nonnull;

public class ItemBlockStorage extends ItemBlock {
	public static String[] blocks = new String[] {"reinforced_metal", "galgadorian", "enhanced_galgadorian"};

	public ItemBlockStorage(final Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(StevesCarts.tabsSC2Blocks);
	}

	@Override
	public String getUnlocalizedName(@Nonnull ItemStack item) {
		if (!item.isEmpty()) {
			return "item.SC2:BlockStorage" + item.getItemDamage();
		}
		return "item.unknown";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
		if (!isInCreativeTab(tab)) {
			return;
		}
		for (int i = 0; i < ItemBlockStorage.blocks.length; ++i) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public int getMetadata(final int dmg) {
		return dmg;
	}
}

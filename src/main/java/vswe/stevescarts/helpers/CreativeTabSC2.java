package vswe.stevescarts.helpers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CreativeTabSC2 extends CreativeTabs {
	@Nonnull
	private ItemStack item;

	public CreativeTabSC2(final String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return item;
	}

	public void setIcon(@Nonnull ItemStack item) {
		this.item = item;
	}
}

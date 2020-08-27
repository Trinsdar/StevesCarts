package vswe.stevescarts.upgrades;

import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.helpers.Localization;

public class Redstone extends BaseEffect {
	@Override
	public String getName() {
		return Localization.UPGRADES.REDSTONE.translate();
	}

	@Override
	public void update(final TileEntityUpgrade upgrade) {
		if (upgrade.getWorld().isBlockIndirectlyGettingPowered(upgrade.getPos()) >= 1 && upgrade.getMaster() != null) {
			upgrade.getMaster().doAssemble();
		}
	}
}

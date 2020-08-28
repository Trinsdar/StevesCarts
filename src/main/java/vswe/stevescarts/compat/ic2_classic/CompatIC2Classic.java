package vswe.stevescarts.compat.ic2_classic;

import vswe.stevescarts.api.ISCHelpers;
import vswe.stevescarts.api.ISCPlugin;
import vswe.stevescarts.api.SCLoadingPlugin;

/**
 * Created by modmuss50 on 08/05/2017.
 */
@SCLoadingPlugin(dependentMod = "ic2-classic-spmod")
public class CompatIC2Classic implements ISCPlugin {

	@Override
	public void loadAddons(ISCHelpers plugins) {
		plugins.registerTree(new IC2ClassicRubberTreeModule());
	}
}

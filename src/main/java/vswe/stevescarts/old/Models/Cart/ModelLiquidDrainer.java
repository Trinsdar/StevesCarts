package vswe.stevescarts.old.Models.Cart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevescarts.modules.ModuleBase;
@SideOnly(Side.CLIENT)
public class ModelLiquidDrainer extends ModelCleaner
{
	public String modelTexture(ModuleBase module) {
		return "/models/cleanerModelLiquid.png";
	}

    public ModelLiquidDrainer()
    {
		super();
    }
	
}

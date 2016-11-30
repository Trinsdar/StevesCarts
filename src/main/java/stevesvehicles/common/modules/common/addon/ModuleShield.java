package stevesvehicles.common.modules.common.addon;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevesvehicles.api.network.DataReader;
import stevesvehicles.client.gui.assembler.SimulationInfo;
import stevesvehicles.client.gui.assembler.SimulationInfoBoolean;
import stevesvehicles.client.gui.screen.GuiVehicle;
import stevesvehicles.client.localization.entry.block.LocalizationAssembler;
import stevesvehicles.client.localization.entry.module.LocalizationIndependence;
import stevesvehicles.common.modules.IActivatorModule;
import stevesvehicles.common.vehicles.VehicleBase;

public class ModuleShield extends ModuleAddon implements IActivatorModule {
	public ModuleShield(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public void loadSimulationInfo(List<SimulationInfo> simulationInfo) {
		simulationInfo.add(new SimulationInfoBoolean(LocalizationAssembler.INFO_SHIELD, "shield", true));
	}

	public float getShieldDistance() {
		return shieldDistance;
	}

	public float getShieldAngle() {
		return shieldAngle;
	}

	public boolean hasShield() {
		return shield;
	}

	private static final int MAX_SHIELD_DISTANCE = 18;
	private static final int MIN_SHIELD_DISTANCE = 0;
	private static final float SHIED_DISTANCE_SPEED = 0.25F;
	private boolean shield = true;
	private float shieldDistance = MAX_SHIELD_DISTANCE;
	private float shieldAngle;
	private DataParameter<Boolean> STATUS;

	@Override
	public void update() {
		super.update();
		if (hasShield() && !getVehicle().hasFuelForModule() && !getVehicle().getWorld().isRemote) {
			setShieldStatus(false);
		}
		if (shield) {
			getVehicle().getEntity().extinguish();
		}
		if (!getShieldStatus() && shieldDistance > MIN_SHIELD_DISTANCE) {
			shieldDistance -= SHIED_DISTANCE_SPEED;
			if (shieldDistance <= MIN_SHIELD_DISTANCE) {
				shield = false;
			}
		} else if (getShieldStatus() && shieldDistance < MAX_SHIELD_DISTANCE) {
			shieldDistance += SHIED_DISTANCE_SPEED;
			shield = true;
		}
		if (shield) {
			shieldAngle = (float) ((shieldAngle + 0.125F) % (Math.PI * 100));
		}
	}

	@Override
	public boolean receiveDamage(DamageSource source, float val) {
		return !hasShield();
	}

	@Override
	public boolean hasSlots() {
		return false;
	}

	@Override
	public boolean hasGui() {
		return true;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
		drawString(gui, getModuleName(), 8, 6, 0x404040);
	}

	public void setShieldStatus(boolean val) {
		if (!isPlaceholder()) {
			updateDw(STATUS, val);
		}
	}

	private boolean getShieldStatus() {
		if (isPlaceholder()) {
			return getBooleanSimulationInfo();
		} else {
			return getDw(STATUS);
		}
	}

	@Override
	public int guiWidth() {
		return 80;
	}

	@Override
	public int guiHeight() {
		return 40;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		drawToggleBox(gui, "shield", getShieldStatus(), x, y);
	}

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x, y, TOGGLE_IMAGE_RECT);
	}

	private String getStateName() {
		return LocalizationIndependence.SHIELD_TOGGLE.translate(getShieldStatus() ? "1" : "0");
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) throws IOException {
		if (button == 0) {
			if (inRect(x, y, TOGGLE_BOX_RECT)) {
				sendPacketToServer(getDataWriter());
			}
		}
	}

	@Override
	public void readData(DataReader dr, EntityPlayer player) {
		updateDw(STATUS, getShieldStatus());
	}

	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		STATUS = createDw(DataSerializers.BOOLEAN);
		registerDw(STATUS, false);
	}

	@Override
	public int getConsumption(boolean isMoving) {
		return hasShield() ? 20 : super.getConsumption(isMoving);
	}

	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("Shield", getShieldStatus());
	}

	@Override
	protected void load(NBTTagCompound tagCompound) {
		setShieldStatus(tagCompound.getBoolean("Shield"));
	}

	@Override
	public void doActivate(int id) {
		setShieldStatus(true);
	}

	@Override
	public void doDeActivate(int id) {
		setShieldStatus(false);
	}

	@Override
	public boolean isActive(int id) {
		return getShieldStatus();
	}
}
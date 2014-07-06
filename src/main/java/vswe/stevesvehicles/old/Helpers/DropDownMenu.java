package vswe.stevesvehicles.old.Helpers;

import java.util.ArrayList;

import net.minecraft.util.IIcon;
import vswe.stevesvehicles.old.Interfaces.GuiDetector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DropDownMenu {
	public static final int SCROLL_HEIGHT = 170;
	public static final int SCROLL_HEADER_HEIGHT = 14;	
	public static final int SCROLL_TOP_MARGIN = 20;
    public static final int TAB_WIDTH = 76;
    public static final int TAB_SPACING = 2;

    private int moduleScroll;
	private int index;
	public DropDownMenu(int index) {
		this.index = index;
		this.moduleScroll = 0;
	}

	public static void update(GuiDetector gui, int x, int y, ArrayList<DropDownMenu> menus) {
		if (gui.currentObject == null) {		
			for (DropDownMenu menu : menus){
				if (gui.inRect(x,y, menu.getHeaderRect())) {
					menu.forceGoUp = false;
					menu.update(true);
					for (DropDownMenu menu2 : menus) {
						if (!menu.equals(menu2)) {
							menu2.forceGoUp = true;
							menu2.update(false);
						}
					}
					return;
				}
			}
			
			for (DropDownMenu menu : menus){
				menu.update(gui.inRect(x,y,menu.getMainRect()));
			}
		}else{
			for (DropDownMenu menu : menus){
				menu.update(false);
			}		
		}
	}
	
	private boolean forceGoUp;
	private void update(boolean hasFocus) {
		if (!forceGoUp && hasFocus) {
			if (moduleScroll < SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT) {
				moduleScroll += 10;
				if (moduleScroll > SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT) {
					moduleScroll = SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT;
				}
			}
		}else{
			if (moduleScroll > 0) {
				moduleScroll -= 25;
				if (moduleScroll <= 0) {
					moduleScroll = 0;
					forceGoUp = false;
				}
			}
		}		
	}

	public void drawMain(GuiDetector gui, int x, int y) {
		ResourceHelper.bindResource(GuiDetector.dropdownTexture);
	
		int [] rect = getMainRect();
		gui.drawTexturedModalRect(gui.getGuiLeft()+rect[0], gui.getGuiTop() + rect[1], 0, SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT-moduleScroll, rect[2], rect[3]);		
	}
	
	public void drawHeader(GuiDetector gui) {
		ResourceHelper.bindResource(GuiDetector.dropdownTexture);
	
		int [] rect = getHeaderRect();
		gui.drawTexturedModalRect(gui.getGuiLeft()+rect[0], gui.getGuiTop() + rect[1], (TAB_WIDTH + TAB_SPACING) * index, SCROLL_HEIGHT-SCROLL_HEADER_HEIGHT + 1, rect[2], rect[3]);
	}

    public void drawContent(GuiDetector gui, int index) {
        drawContent(gui, index, 0, 0, 16);
    }

    public void drawContent(GuiDetector gui, int index, int srcX, int srcY) {
        drawContent(gui, index, srcX, srcY, 256);
    }

	public void drawContent(GuiDetector gui, int index, int srcX, int srcY, int textureSize) {
		int[] rect = getContentRect(index);
		if (rect == null) {
			return;
		}		
		
		int gap = rect[1] - getMainRect()[1] + rect[3];

		if (gap > 0) {
			int height = Math.min(rect[3], gap);
			int offset = rect[3] - height;

			gui.drawRectWithTextureSize(gui.getGuiLeft()+rect[0],  gui.getGuiTop() + rect[1]+offset, srcX, srcY+offset, rect[2], height, textureSize);
		}
	}
	
	public void drawContent(GuiDetector gui, int index, IIcon icon) {
		int[] rect = getContentRect(index);
		if (rect == null) {
			return;
		}
		
		int gap = rect[1] - getMainRect()[1] + rect[3];

		if (gap > 0) {
			int height = Math.min(rect[3], gap);
			int offset = rect[3] - height;
			gui.drawIcon(icon, gui.getGuiLeft()+rect[0],  gui.getGuiTop() + rect[1]+offset, rect[2] / 16F, height / 16F, 0F, offset/16F);
		}
	}	

	public int[] getContentRect(int posId) {

		int objectsPerRow = 11;
		int objectsRows = 7;
		int objectWidth = 16;
		int objectHeight = 16;
		int objectY = 31;
		
		//bigger objects
		if (this.index == 2) {
			objectsPerRow = 9;
			objectsRows = 10;
			objectWidth = 20;
			objectHeight = 11;	
			objectY = 34;
		}
		
		posId = getCurrentId(posId, objectsPerRow * objectsRows);
		if (posId < 0 || posId >= objectsPerRow * objectsRows) {
			return null;
		}
		
		int x = posId % objectsPerRow;
		int y = posId / objectsPerRow;
		
	
		int targetX = x * (objectWidth + 3) + 25;
		int targetY = y * (objectHeight + 3) + SCROLL_TOP_MARGIN + objectY + getScroll() - SCROLL_HEIGHT;	
		return new int[] {targetX, targetY, objectWidth, objectHeight};
	}	
	
	
	
	public int [] getMainRect() {
		return new int[] {11, SCROLL_TOP_MARGIN, 232, moduleScroll};		
	}
	
	public int [] getHeaderRect() {
		return new int[] {11 + (TAB_WIDTH + TAB_SPACING) * index, SCROLL_TOP_MARGIN + moduleScroll, TAB_WIDTH, SCROLL_HEADER_HEIGHT};
	}
	
	
	public int getScroll() {
		return moduleScroll;
	}
	
	
	protected int getCurrentId(int index, int objects) {
		return index;
	}
	
	
	public void onClick(GuiDetector gui, int x, int y) {
	
	}
}
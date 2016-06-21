package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiButton;
import net.minecraft.util.ResourceLocation;

public class ButtonCross extends DarkcraftGuiButton
{
	private static GuiTexture gt = new GuiTexture(new ResourceLocation(DarkcraftMod.modName, "textures/gui/buttoncross.png"),16,16);

	public ButtonCross(int x, int y)
	{
		super("cross", x, y, gt, null);
	}

	public ButtonCross(DarkcraftGui gui)
	{
		super("cross", 0, gui.guiH-16, gt, null);
	}
}

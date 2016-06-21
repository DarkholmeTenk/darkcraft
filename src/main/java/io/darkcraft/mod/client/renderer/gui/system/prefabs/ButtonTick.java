package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiButton;
import net.minecraft.util.ResourceLocation;

public class ButtonTick extends DarkcraftGuiButton
{
	private static GuiTexture gt = new GuiTexture(new ResourceLocation(DarkcraftMod.modName, "textures/gui/buttontick.png"),16,16);

	public ButtonTick(int x, int y)
	{
		super("tick", x, y, gt, null);
	}

	public ButtonTick(DarkcraftGui gui)
	{
		super("tick", gui.guiW-16, gui.guiH-16, gt, null);
	}

}

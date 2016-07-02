package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiButton;
import net.minecraft.util.ResourceLocation;

public class ButtonCloseOpen extends DarkcraftGuiButton
{
	private static final ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/gui/bits/closeopen.png");
	private static final GuiTexture closed =	new GuiTexture(tex,new UVStore(0,1,0,0.5),16,16);
	private static final GuiTexture open =		new GuiTexture(tex,new UVStore(0,1,0.5,1),16,16);
	public boolean isOpen = false;

	public ButtonCloseOpen(int x, int y)
	{
		super("closeopen", x, y, closed, null);
		w = 16;
		h = 16;
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		isOpen = !isOpen;
		if(parent != null)
			parent.clickableClicked(this, isOpen ? "closeopen-close" : "closeopen-open");
		return true;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(isOpen)
			open.render(0, 0, 0, true);
		else
			closed.render(0, 0, 0, true);
	}
}

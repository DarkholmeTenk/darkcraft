package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiButton;
import net.minecraft.util.ResourceLocation;

public class ButtonCloseOpen extends DarkcraftGuiButton
{
	private static final ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/gui/bits/closeopen.png");
	private final GuiTexture closed;
	private final GuiTexture open;
	public boolean isOpen = false;

	public ButtonCloseOpen(int x, int y, int size)
	{
		super("closeopen", x, y, size, size, null);
		closed =	new GuiTexture(tex,new UVStore(0,1,0,0.5),size,size);
		open =		new GuiTexture(tex,new UVStore(0,1,0.5,1),size,size);
		w = 16;
		h = 16;
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		isOpen = !isOpen;
		if(parent != null)
			parent.clickableClicked(this, isOpen ? "closeopen-close" : "closeopen-open", button);
		return true;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(!visible) return;
		if(isOpen)
			open.render(0, 0, 0, true);
		else
			closed.render(0, 0, 0, true);
	}
}

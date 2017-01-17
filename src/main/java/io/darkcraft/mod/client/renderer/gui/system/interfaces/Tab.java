package io.darkcraft.mod.client.renderer.gui.system.interfaces;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiButton;

public class Tab<T extends AbstractGuiElement> extends DarkcraftGuiButton
{
	private final ResourceLocation rl;
	private final UVStore uv;

	private final ResourceLocation downRL;
	private final UVStore downUV;

	public boolean down;

	public Tab(String id, ResourceLocation rl, UVStore uv, ResourceLocation downRL, UVStore downUV)
	{
		super(id, 0,0, 24,24, "");
		this.rl = rl;
		this.uv = uv;
		this.downRL =downRL;
		this.downUV = downUV;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		RenderHelper.resetColour();
		if(down)
		{
			RenderHelper.bindTexture(downRL);
			RenderHelper.uiFace(0, 0, 24, 24, 0, downUV, true);
		}
		else
		{
			RenderHelper.bindTexture(rl);
			RenderHelper.uiFace(0, 0, 24, 24, 0, uv, true);
		}
	}

	public boolean filter(T t)
	{
		return true;
	}
}
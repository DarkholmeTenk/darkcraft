package io.darkcraft.mod.client.renderer.gui.chalk;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.common.magic.systems.symbolic.ChalkType;

public class ChalkBackground extends AbstractGuiElement
{

	public ChalkBackground(int _x, int _y)
	{
		super(_x, _y, 200, 200);
	}

	private int getLevel()
	{
		return 0;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		RenderHelper.bindTexture(ChalkType.TEXTURES[getLevel()]);
		RenderHelper.uiFace(0, 0, 200, 200, 0, UVStore.defaultUV, true);
	}

}

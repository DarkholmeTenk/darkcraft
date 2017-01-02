package io.darkcraft.mod.client.renderer.gui.system.spells;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.common.magic.systems.component.IComponent;

public class ComponentIcon extends AbstractGuiElement
{
	public IComponent component;

	public ComponentIcon(int _x, int _y, int width, int height, IComponent component)
	{
		super(_x, _y, width, height);
		this.component = component;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(component == null) return;
		GL11.glColor3f(1, 1, 1);
		RenderHelper.bindTexture(component.getIcon());
		RenderHelper.uiFace(0, 0, w, h, 0, component.getIconLocation(), true);
	}
}

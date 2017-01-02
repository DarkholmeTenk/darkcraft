package io.darkcraft.mod.client.renderer.gui.system.spells;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.common.magic.systems.spell.CastType;
import io.darkcraft.mod.common.magic.systems.spell.Spell;

public class SpellIcon extends AbstractGuiElement
{
	public Spell spell;

	public SpellIcon(int _x, int _y, int width, int height, Spell _spell)
	{
		super(_x, _y, width, height);
		spell = _spell;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(spell == null) return;
		if((spell.components.length == 0) || (spell.mostExpensiveComponent == null)) return;
		GL11.glColor3f(1, 1, 1);
		RenderHelper.bindTexture(spell.mostExpensiveComponent.component.getIcon());
		RenderHelper.uiFace(0, 0, w, h, 0, spell.mostExpensiveComponent.component.getIconLocation(), true);
		RenderHelper.bindTexture(CastType.icon);
		RenderHelper.uiFace(0, 0, w, h, 0, spell.type.uv, true);
	}
}

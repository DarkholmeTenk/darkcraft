package io.darkcraft.mod.client;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.spell.CastType;
import io.darkcraft.mod.common.magic.systems.spell.Spell;

import org.lwjgl.opengl.GL11;

public class ClientHelper
{
	public static void renderSpellIcon(Spell spell,int x, int y, int w, int h)
	{
		if((spell == null) || (spell.mostExpensiveComponent == null)) return;
		IComponent c = spell.mostExpensiveComponent.component;
		GL11.glColor3f(1, 1, 1);
		RenderHelper.bindTexture(c.getIcon());
		RenderHelper.uiFace(x,y,w,h,2,c.getIconLocation(),true);
		RenderHelper.bindTexture(CastType.icon);
		RenderHelper.uiFace(x,y,w,h,2,spell.type.uv,true);
	}
}

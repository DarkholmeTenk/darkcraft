package io.darkcraft.mod.client.renderer.gui.system.spells;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;

public class ComponentInstanceLabel extends AbstractGuiElement
{
	public ComponentInstance compInst;
	public Colour labelColour = Colour.white;
	public boolean shadow = true;
	private String str;

	private final int scale;

	public ComponentInstanceLabel(int x, int y, int width, ComponentInstance ci)
	{
		this(x,y,width,ci,1);
	}

	public ComponentInstanceLabel(int x, int y, int width, ComponentInstance ci, int scale)
	{
		super(x, y, width, 16 * scale);
		this.scale = scale;
		setCI(ci);
	}

	public void setCI(ComponentInstance ci)
	{
		compInst = ci;
		str = ci.toString();
		FontRenderer fr = RenderHelper.getFontRenderer();
		int size = scale * (4 + (12 * fr.listFormattedStringToWidth(str, w-20).size()));
		setSize(w, size);
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		RenderHelper.bindTexture(compInst.component.getIcon());
		RenderHelper.uiFace(0, 0, 16 * scale, 16 * scale, 0, compInst.component.getIconLocation(), true);
		FontRenderer fr = RenderHelper.getFontRenderer();
		GL11.glPushMatrix();
		GL11.glScaled(scale, scale, scale);
		int y = 4 - 12;
		List<String> strl = fr.listFormattedStringToWidth(str, (w/scale)-20);
		for(String strp : strl)
			fr.drawString(strp, 18, y+=12, labelColour.asInt, shadow);
		GL11.glPopMatrix();
	}

}

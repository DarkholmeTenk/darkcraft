package io.darkcraft.mod.client.renderer.gui.system;

import java.util.EnumSet;

import net.minecraft.client.gui.FontRenderer;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableInternal;

public class DarkcraftEnumSetButton<T extends Enum<T>> extends DarkcraftLabel implements IClickable
{
	private final GuiTexture bg;

	private final EnumResolver<T> resolver;
	private final T[] allEnums;
	private final EnumSet<T> enums;
	private T current;

	public DarkcraftEnumSetButton(int x, int y, int w, int h, Class<T> enumClass)
	{
		this(x,y,w,h,enumClass.getEnumConstants(),EnumSet.allOf(enumClass), new DefaultEnumResolver<T>());
	}

	public DarkcraftEnumSetButton(int x, int y, int w, int h, T[] vals, EnumSet<T> enums, EnumResolver<T> resolver)
	{
		super(x,y,w,"");
		setSize(w, h);
		if(enums.isEmpty() || (vals.length == 0)) throw new RuntimeException("Can't instantiate with no options");
		bg = new ScalableInternal(w,h);
		this.enums = enums;
		this.allEnums = vals;
		this.resolver = resolver;
		current = allEnums[0];
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		bg.render(0, 0, 0, true);
		FontRenderer fr = RenderHelper.getFontRenderer();
		String text = resolver.toString(current);
		int fx = (bg.w - fr.getStringWidth(text)) / 2;
		int fy = ((bg.h - fr.FONT_HEIGHT) / 2) + 1;
		fr.drawString(text, fx, fy, colour.asInt, shadow);
	}

	private void next(boolean up)
	{
		int o = up ? 1 : -1;
		do
			current = allEnums[((current.ordinal() + o) + allEnums.length) % allEnums.length];
		while(!enums.contains(current));
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		next(button == 0);
		parent.clickableClicked(this, "change", button);
		return true;
	}

	public T getValue()
	{
		return current;
	}

	public static interface EnumResolver<T extends Enum<T>>
	{
		public String toString(T t);
	}

	private static class DefaultEnumResolver<T extends Enum<T>> implements EnumResolver<T>
	{
		@Override
		public String toString(T t)
		{
			return t.name();
		}
	}
}

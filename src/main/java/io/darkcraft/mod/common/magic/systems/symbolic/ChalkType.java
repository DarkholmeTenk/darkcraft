package io.darkcraft.mod.common.magic.systems.symbolic;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.mod.DarkcraftMod;

public enum ChalkType
{
	White(new Colour(0.7f,0.7f,0.7f), new Colour(0.1f,0.2f,1f,0.6f)),
	Blood(new Colour(0.6f,0.1f,0.05f), new Colour(0.8f,0,0,0.6f));

	private static String[] names;

	public final Colour colour;
	public final Colour glowColour;
	private ChalkType(Colour _colour, Colour _glowColour)
	{
		colour = _colour;
		glowColour = _glowColour;
	}
	public static String[] getNames()
	{
		if(names == null)
		{
			ChalkType[] values = values();
			names = new String[values.length];
			for(int i = 0; i < values.length; i++)
				names[i] = values[i].name();
		}
		return names;
	}

	public static final ResourceLocation[] TEXTURES = {
			new ResourceLocation(DarkcraftMod.modName, "textures/chalk/level1.png")
	};
}

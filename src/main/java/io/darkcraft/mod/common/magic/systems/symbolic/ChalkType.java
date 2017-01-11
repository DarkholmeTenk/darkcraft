package io.darkcraft.mod.common.magic.systems.symbolic;

import io.darkcraft.darkcore.mod.datastore.Colour;

public enum ChalkType
{
	WHITE("basic", new Colour(0.9f, 0.9f, 0.9f)),
	BLOOD("blood", new Colour(0.7f, 0.1f, 0.0f)),
	ENDER("ender", new Colour(0.1f, 0.7f, 0.4f)),
	SOUL ("soul",  new Colour(0.2f, 0.3f, 0.9f));

	public final Colour renderColour;
	public final String name;

	private ChalkType(String name, Colour renderColour)
	{
		this.name = name;
		this.renderColour = renderColour;
	}

	public static String[] getNames()
	{
		ChalkType[] types = values();
		String[] names = new String[types.length];
		for(int i = 0; i < types.length; i++)
			names[i] = types[i].name;
		return null;
	}
}

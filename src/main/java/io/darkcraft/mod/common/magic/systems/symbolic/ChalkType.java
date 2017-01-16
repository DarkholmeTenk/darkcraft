package io.darkcraft.mod.common.magic.systems.symbolic;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.mod.DarkcraftMod;

public enum ChalkType
{
	WHITE("basic", new Colour(0.9f, 0.9f, 0.9f), 2),
	BLOOD("blood", new Colour(0.7f, 0.1f, 0.0f), 3),
	ENDER("ender", new Colour(0.1f, 0.7f, 0.4f), 4),
	SOUL ("soul",  new Colour(0.2f, 0.3f, 0.9f), 6),
	GODLY("godly", new Colour(0.8f, 0.8f, 0.2f), 8);

	public static final ResourceLocation[] TEXTURES = {
			new ResourceLocation(DarkcraftMod.modName, "textures/chalk/level1.png"),
			new ResourceLocation(DarkcraftMod.modName, "textures/chalk/level2.png"),
			new ResourceLocation(DarkcraftMod.modName, "textures/chalk/level3.png"),
			new ResourceLocation(DarkcraftMod.modName, "textures/chalk/level4.png"),
			new ResourceLocation(DarkcraftMod.modName, "textures/chalk/level5.png")
	};

	public final Colour renderColour;
	public final String name;
	private final int level;

	private ChalkType(String name, Colour renderColour, int level)
	{
		this.name = name;
		this.renderColour = renderColour;
		this.level = level;
	}

	public static String[] getNames()
	{
		ChalkType[] types = values();
		String[] names = new String[types.length];
		for(int i = 0; i < types.length; i++)
			names[i] = types[i].name;
		return names;
	}

	public int getLevel()
	{
		return level;
	}
}

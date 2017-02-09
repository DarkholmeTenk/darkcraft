package io.darkcraft.mod.common.magic.systems.symbolic;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.mod.DarkcraftMod;

public enum ChalkType
{
	/**
	 * Level 1
	 */
	WHITE("basic", new Colour(0.9f, 0.9f, 0.9f), 2,
			cp(0,2, 0,-2)),
	/**
	 * Level 2
	 */
	BLOOD("blood", new Colour(0.7f, 0.1f, 0.0f), 3,
			cp(0,2, 2,-2, -2,-2)),
	/**
	 * Level 3
	 */
	ENDER("ender", new Colour(0.1f, 0.7f, 0.4f), 4,
			cp(0,2, 2,0, 0,-2, -2,0)),
	/**
	 * Level 4
	 */
	SOUL ("soul",  new Colour(0.2f, 0.3f, 0.9f), 6,
			cp(0,2, 2,1, 2,-1, 0,-2, -2,-1, -2,1)),
	/**
	 * Level 5
	 */
	GODLY("godly", new Colour(0.8f, 0.8f, 0.2f), 8,
			cp(1,2, 2,1, 2,-1, 1,-2, -1,-2, -2,-1, -2,1, -1,2));

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
	private final CP[] positions;

	private ChalkType(String name, Colour renderColour, int level, CP[] positions)
	{
		this.name = name;
		this.renderColour = renderColour;
		this.level = level;
		this.positions = positions;
		if(positions.length != level)
			throw new RuntimeException("Wrong number of positions : " + this + "l:"+level+"p"+positions.length);
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

	public ResourceLocation getTexture()
	{
		return TEXTURES[ordinal()];
	}

	public Pair<Integer, Integer>[] getPositions()
	{
		return positions;
	}

	private static class CP extends Pair<Integer,Integer>
	{
		public CP(Integer _a, Integer _b)
		{
			super(_a, _b);
		}
	}

	private static CP[] cp(int... values)
	{
		CP[] arr = new CP[values.length / 2];
		for(int i = 0; i < values.length; i+=2)
			arr[i/2] = new CP(values[i], values[i+1]);
		return arr;
	}
}

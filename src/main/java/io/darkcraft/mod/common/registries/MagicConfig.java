package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.DarkcraftMod;

public class MagicConfig
{
	public static ConfigFile	config;
	public static double		xpCostMult		= 0.1;
	public static double		minCostMult		= 0.5;
	public static double		maxCostMult		= 1.5;
	public static double		projectileSpeed	= .8;

	public static double		fieldDecay		= 0.95;
	public static double		fieldDecrease	= 0.1;
	public static int			fieldTicks		= 20;
	public static int			fieldSize		= 5;
	public static boolean		fieldSizeY		= false;
	public static double		fieldMax		= 100;

	public static void refreshConfigs()
	{
		if (config == null) config = DarkcraftMod.configHandler.registerConfigNeeder("Magic");
		xpCostMult = config.getDouble("spell xp mult", 0.1, "This is the value the spell cost is multiplied by to work out how much xp to award");
		minCostMult = config.getDouble("min spell cost mult", 0.5, "The lowest amount of the original spell cost that can be reached by player being skilled in that skill");
		maxCostMult = config.getDouble("max spell cost mult", 1.5, "The maximum amount of the original spell cost that can be reached by player being unskilled in that skill");
		projectileSpeed = config.getDouble("projectile speed", .8);

		fieldDecay = MathHelper.clamp(config.getDouble("field decay multiplier", 0.95, "Every magic field tick, the current field strength is multiplied by this","This has to be between 0 and 1"),0,1);
		fieldDecrease = config.getDouble("field decay const", 0.1, "A static amount for the field to decrease by every field tick, after the multiplier is taken into account");
		fieldTicks = Math.max(1, config.getInt("field tick rate", 20, "Number of regular ticks per field tick","Must be at least 1"));
		fieldSize = MathHelper.clamp(config.getInt("field size", 5, "This value affects the size of each chunk of the magic field","The size of each chunk is 2^this value","Min: 2, Max:8"), 2, 8);
		fieldSizeY = config.getBoolean("field size y", false, "If false, the magic field will be the same no matter your y value","If true, the field will be divided into y chunks too");
		fieldMax = config.getDouble("field max", 100, "Sets the maximum value the field can take");
	}
}

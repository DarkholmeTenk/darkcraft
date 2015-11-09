package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.mod.DarkcraftMod;

public class MagicConfig
{
	public static ConfigFile config;
	public static double xpCostMult = 0.1;
	public static double minCostMult = 0.5;
	public static double maxCostMult = 1.5;

	public static void refreshConfigs()
	{
		if(config == null)
			config = DarkcraftMod.configHandler.registerConfigNeeder("Magic");
		xpCostMult		= config.getDouble("spell xp mult", 0.1, "This is the value the spell cost is multiplied by to work out how much xp to award");
		minCostMult		= config.getDouble("min spell cost mult", 0.5, "The lowest amount of the original spell cost that can be reached by player being skilled in that skill");
		maxCostMult		= config.getDouble("max spell cost mult", 1.5, "The maximum amount of the original spell cost that can be reached by player being unskilled in that skill");

	}
}

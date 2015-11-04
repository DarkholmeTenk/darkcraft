package io.darkcraft.mod.common.registries;

import io.darkcraft.mod.common.skills.MagicSkill;
import skillapi.api.internal.ISkillAPI;

public class SkillRegistry
{
	public static ISkillAPI api;

	public static MagicSkill destruction = new MagicSkill("destruction");
	public static MagicSkill mysticism = new MagicSkill("mysticism");
	public static MagicSkill restoration = new MagicSkill("restoration");
	public static MagicSkill alteration = new MagicSkill("alteration");
	public static MagicSkill conjuration = new MagicSkill("conjuration");
	public static MagicSkill illusion = new MagicSkill("illusion");

	public static void requestAPI(ISkillAPI apiRequest)
	{
		api = apiRequest;
		api.registerSkill(destruction);
		api.registerSkill(mysticism);
		api.registerSkill(restoration);
		api.registerSkill(alteration);
		api.registerSkill(conjuration);
		api.registerSkill(illusion);
	}
}

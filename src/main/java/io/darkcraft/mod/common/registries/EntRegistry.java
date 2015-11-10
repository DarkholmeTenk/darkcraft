package io.darkcraft.mod.common.registries;

import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.entities.EntitySpellProjectile;
import cpw.mods.fml.common.registry.EntityRegistry;

public class EntRegistry
{
	public static void registerEntities()
	{
		EntityRegistry.registerModEntity(EntitySpellProjectile.class, "dcSpellProjectile", 0, DarkcraftMod.i, 8, 10, true);
	}
}

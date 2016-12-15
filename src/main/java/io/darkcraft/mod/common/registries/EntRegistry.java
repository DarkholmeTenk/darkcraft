package io.darkcraft.mod.common.registries;

import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;

import cpw.mods.fml.common.registry.EntityRegistry;

public class EntRegistry
{
	public static void registerEntities()
	{
		EntityRegistry.registerModEntity(EntitySpellProjectile.class, "dcSpellProjectile", 0, DarkcraftMod.i, 80, 5, true);
	}
}

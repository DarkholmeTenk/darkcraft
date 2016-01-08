package io.darkcraft.mod.client;

import io.darkcraft.mod.client.renderer.EntitySpellProjectileRenderer;
import io.darkcraft.mod.client.renderer.ItemStaffRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicFieldMeasurerRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicVortexCrystalRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicVortexRenderer;
import io.darkcraft.mod.client.renderer.tileent.TechGeneratorRenderer;
import io.darkcraft.mod.common.CommonProxy;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.tileent.MagicFieldMeasurer;
import io.darkcraft.mod.common.magic.tileent.MagicVortex;
import io.darkcraft.mod.common.magic.tileent.MagicVortexCrystal;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.tech.tileent.TechGenerator;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		MinecraftForgeClient.registerItemRenderer(ItemBlockRegistry.itemStaff, new ItemStaffRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicFieldMeasurer.class, new MagicFieldMeasurerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicVortex.class, new MagicVortexRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicVortexCrystal.class, new MagicVortexCrystalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TechGenerator.class, new TechGeneratorRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new EntitySpellProjectileRenderer());
	}
}

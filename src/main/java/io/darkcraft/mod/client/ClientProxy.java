package io.darkcraft.mod.client;

import io.darkcraft.mod.client.renderer.EntitySpellProjectileRenderer;
import io.darkcraft.mod.client.renderer.ItemStaffRenderer;
import io.darkcraft.mod.common.CommonProxy;
import io.darkcraft.mod.common.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		MinecraftForgeClient.registerItemRenderer(ItemBlockRegistry.itemStaff, new ItemStaffRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new EntitySpellProjectileRenderer());
	}
}

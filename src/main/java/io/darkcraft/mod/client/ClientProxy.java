package io.darkcraft.mod.client;

import io.darkcraft.mod.client.renderer.EntitySpellProjectileRenderer;
import io.darkcraft.mod.client.renderer.ItemStaffRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicFieldMeasurerRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicStaffChangerRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicVortexCrystalRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicVortexRenderer;
import io.darkcraft.mod.client.renderer.tileent.SpellCreatorRenderer;
import io.darkcraft.mod.client.renderer.tileent.TechGeneratorRenderer;
import io.darkcraft.mod.common.CommonProxy;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.gui.client.SpellCreationGui;
import io.darkcraft.mod.common.magic.tileent.MagicFieldMeasurer;
import io.darkcraft.mod.common.magic.tileent.MagicStaffChanger;
import io.darkcraft.mod.common.magic.tileent.MagicVortex;
import io.darkcraft.mod.common.magic.tileent.MagicVortexCrystal;
import io.darkcraft.mod.common.magic.tileent.SpellCreator;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.tech.tileent.TechGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
		ClientRegistry.bindTileEntitySpecialRenderer(MagicStaffChanger.class, new MagicStaffChangerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(SpellCreator.class, new SpellCreatorRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new EntitySpellProjectileRenderer());
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		switch(id)
		{
			case 1397: return new SpellCreationGui((SpellCreator)te);
			//case 1397: return new SpellCreationGui((SpellCreator)te);
		}
		return null;
	}
}

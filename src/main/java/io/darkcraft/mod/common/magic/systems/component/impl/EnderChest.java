package io.darkcraft.mod.common.magic.systems.component.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.INoAreaComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

import skillapi.api.implement.ISkill;

public class EnderChest implements IComponent, INoAreaComponent
{
	@Override
	public String id()
	{
		return "enderchest";
	}

	@Override
	public String getUnlocalisedName()
	{
		return "darkcraft.component.enderchest";
	}

	@Override
	public ResourceLocation getIcon()
	{
		return MagicalRegistry.componentTex;
	}

	private final UVStore uv = new UVStore(0.3, 0.4, 0.2, 0.3);

	@Override
	public UVStore getIconLocation()
	{
		return uv;
	}

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.conjuration;
	}

	@Override
	public double getCost()
	{
		return 400;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config)
	{
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if((caster == null) || !caster.isCaster(ent) || !(ent instanceof EntityPlayerMP)) return;
		EntityPlayerMP pl = (EntityPlayerMP) ent;
		InventoryEnderChest chest = pl.getInventoryEnderChest();
		if(chest != null)
			pl.displayGUIChest(chest);
	}

	@Override
	public boolean applyToEnt()
	{
		return true;
	}

	@Override
	public boolean applyToBlock()
	{
		return false;
	}

	@Override
	public ResourceLocation getProjectileTexture()
	{
		return MagicalRegistry.projectileTex;
	}

	private static final UVStore[] uvs = new UVStore[] { new UVStore(0.2, 0.3, 0, 0.1) };

	@Override
	public UVStore getProjectileLocation(int f)
	{
		return uvs[f % uvs.length];
	}
}

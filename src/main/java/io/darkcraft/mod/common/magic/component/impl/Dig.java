package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Dig implements IComponent
{

	@Override
	public String id()
	{
		return "dig";
	}

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.alteration;
	}

	@Override
	public double getCost()
	{
		return 1;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore bp, int magnitude, int duration)
	{
		if(bp == null) return;
		Block b = bp.getBlock();
		if(b == null) return;
		if(b.getBlockHardness(bp.getWorldObj(), bp.x, bp.y, bp.z) < 50)
		{
			ArrayList<ItemStack> drops = b.getDrops(bp.getWorldObj(), bp.x, bp.y, bp.z, bp.getMetadata(), 0);
			bp.setToAir();
			if(drops != null)
			{
				SimpleDoubleCoordStore center = bp.getCenter();
				for(ItemStack is : drops)
				{
					WorldHelper.dropItemStack(is, center);
				}
			}
		}
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration)
	{
	}

	@Override
	public boolean applyToEnt()
	{
		return false;
	}

	@Override
	public boolean applyToBlock()
	{
		return true;
	}

	@Override
	public ResourceLocation getProjectileTexture()
	{
		return null;
	}

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.dig";}

	@Override
	public ResourceLocation getIcon(){return MagicalRegistry.componentTex;}

	private final UVStore uv = new UVStore(0.0,0.1,0.1,0.2);
	@Override
	public UVStore getIconLocation(){return uv;}

}

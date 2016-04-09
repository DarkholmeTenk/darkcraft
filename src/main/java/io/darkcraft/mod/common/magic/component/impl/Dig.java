package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.component.IMagnitudeComponent;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Dig implements IComponent, IMagnitudeComponent
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
		return 6;
	}

	protected boolean canBreak(int magnitude, float hardness)
	{
		if(hardness < 3) return true;
		if((magnitude >= 2) && (hardness < 50)) return true;
		if((magnitude >= 3) && (hardness < 100)) return true;
		return false;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore bp, int side, int magnitude, int duration)
	{
		if(bp == null) return;
		Block b = bp.getBlock();
		if(b == null) return;
		if(canBreak(magnitude, b.getBlockHardness(bp.getWorldObj(), bp.x, bp.y, bp.z)))
		{
			ArrayList<ItemStack> drops = b.getDrops(bp.getWorldObj(), bp.x, bp.y, bp.z, bp.getMetadata(), 0);
			bp.setToAir();
			if((drops != null) && ServerHelper.isServer())
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
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.1,0.2,0,0.1)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.dig";}

	@Override
	public ResourceLocation getIcon(){return MagicalRegistry.componentTex;}

	private final UVStore uv = new UVStore(0.0,0.1,0.1,0.2);
	@Override
	public UVStore getIconLocation(){return uv;}

	@Override
	public int getMinMagnitude()
	{
		return 1;
	}

	@Override
	public int getMaxMagnitude()
	{
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public double getCostMag(int magnitude, double oldCost)
	{
		switch(magnitude)
		{
			case 1: return oldCost;
			case 2: return oldCost * 2;
			case 3: return oldCost * 5;
			default: return oldCost;
		}
	}

}

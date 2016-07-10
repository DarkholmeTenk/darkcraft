package io.darkcraft.mod.common.magic.systems.component.impl;

import java.util.ArrayList;
import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.DelayedItemHandler;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDescriptiveMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Dig implements IComponent, IDescriptiveMagnitudeComponent
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
		return 5;
	}

	protected boolean canBreak(int magnitude, float hardness)
	{
		if(hardness < 0) return false;
		switch(magnitude)
		{
			case 5: return true;
			case 4: return hardness <= 50;
			case 3: return hardness <= 5;
			case 2: return hardness <= 3;
			default: return hardness < 3;
		}
	}

	public ArrayList<ItemStack> getDrops(Block b, SimpleCoordStore bp, EntityPlayer pl)
	{
		return b.getDrops(bp.getWorldObj(), bp.x, bp.y, bp.z, bp.getMetadata(), 0);
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore bp, int side, int magnitude, int duration, int config)
	{
		if(bp == null) return;
		Block b = bp.getBlock();
		if(b == null) return;
		if(canBreak(magnitude, b.getBlockHardness(bp.getWorldObj(), bp.x, bp.y, bp.z)))
		{
			EntityPlayer pl = caster instanceof PlayerCaster ? ((PlayerCaster)caster).getCaster() : null;
			ArrayList<ItemStack> drops = getDrops(b,bp,pl);
			bp.setToAir();
			if((drops != null) && ServerHelper.isServer())
			{
				SimpleDoubleCoordStore center = bp.getCenter();
				if(pl != null)
					DelayedItemHandler.addItemDrop(pl, drops);
				else
					for(ItemStack is : drops)
						WorldHelper.dropItemStack(is, center);
			}
		}
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
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
		return 5;
	}

	@Override
	public double getCostMag(int magnitude, double oldCost)
	{
		switch(magnitude)
		{
			case 1: return oldCost;
			case 2: return oldCost * 2;
			case 3: return oldCost * 3;
			case 4: return oldCost * 5;
			case 5: return oldCost * 10;
			default: return oldCost;
		}
	}

	@Override
	public void getDescription(List<String> strings, int magnitude)
	{
		strings.add("dc.dig.description");
		strings.add("dc.dig.magnitude."+magnitude+".description");
	}

}

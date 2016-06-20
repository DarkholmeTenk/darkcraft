package io.darkcraft.mod.common.magic.systems.effects;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EffectWaterWalk extends AbstractDarkcraftEffect
{
	private double lastY = -1;
	public EffectWaterWalk(ICaster _caster, EntityLivingBase ent, int magnitude, int duration)
	{
		super("waterwalk", _caster, ent, magnitude, duration-1, true, true, 1);
	}

	@Override
	public void apply()
	{
		Entity ent = getEntity();
		if(ent == null) return;
		boolean inWater = false;
		SimpleDoubleCoordStore pos = new SimpleDoubleCoordStore(ent).translate(0, -ent.yOffset-0.01, 0);
		for(SimpleCoordStore scs : SimpleCoordStore.getTouching(pos, true))
		{
			Block b = scs.getBlock();
			if(b == null) continue;
			if((b!= null) && (b.getMaterial() == Material.water))
			{
				inWater = true;
				break;
			}
		}
		if(!inWater) return;
		if(ent.isInsideOfMaterial(Material.water) || ent.isSneaking())
		{
			lastY = -1;
			return;
		}
		ent.onGround = true;
		ent.isCollided = true;
		ent.isCollidedVertically = true;
		ent.fallDistance = 0;
		ent.isAirBorne = false;
		if(ent.motionY < 0) ent.motionY = 0.0;
		lastY = Math.max(lastY,ent.posY);
	}

	private static final UVStore uv = new UVStore(0.1,0.2,0.1,0.2);
	@Override
	public UVStore getIconLocation()
	{
		return uv;
	}

}

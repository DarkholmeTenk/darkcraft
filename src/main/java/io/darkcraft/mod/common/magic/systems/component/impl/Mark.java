package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IConfigurableComponent;
import io.darkcraft.mod.common.magic.systems.component.INoAreaComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Mark implements IComponent, IConfigurableComponent, INoAreaComponent
{

	@Override
	public String id(){ return "mark"; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component.mark"; }

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uv = new UVStore(0.4,0.5,0.3,0.4);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.mysticism; }

	@Override
	public double getCost()
	{
		return 65;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase e = (EntityLivingBase) ent;
		if(caster instanceof EntityCaster)
		{
			EntityCaster ec = (EntityCaster) caster;
			if(!Helper.isCaster(ec, e)) return;
			NBTTagCompound nbt = ec.getExtraData();
			SimpleDoubleCoordStore markLoc = new SimpleDoubleCoordStore(e);
			nbt.setTag("markLoc"+config, markLoc.writeToNBT());
			if(caster instanceof PlayerCaster)
				MessageHelper.sendMessage(((PlayerCaster) caster).getCaster(), "dc.message.mark.success " + config);
		}
	}

	@Override
	public boolean applyToEnt(){ return true; }

	@Override
	public boolean applyToBlock(){ return false; }

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.2,0.3,0,0.1)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

	@Override
	public int getMinConfig()
	{
		return 0;
	}

	@Override
	public int getMaxConfig()
	{
		return 3;
	}

	@Override
	public String getConfigDescription(int val)
	{
		return "dc.mark.slot " + val;
	}

}

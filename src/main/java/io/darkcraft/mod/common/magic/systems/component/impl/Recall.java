package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.TeleportHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IConfigurableComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import skillapi.api.implement.ISkill;

public class Recall implements IComponent, IConfigurableComponent
{
	public final boolean crossDimensional;
	public Recall(boolean cd)
	{
		crossDimensional = cd;
	}

	@Override
	public String id(){ return crossDimensional? "recall.cd" : "recall"; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component." +id(); }

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uvN = new UVStore(0.4,0.5,0.2,0.3);
	private final UVStore uvC = new UVStore(0.5,0.6,0.2,0.3);
	@Override
	public UVStore getIconLocation(){ return crossDimensional ? uvC : uvN; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.conjuration; }

	@Override
	public double getCost()
	{
		return crossDimensional ? 300 : 65;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	private void transfer(ICaster caster, EntityLivingBase e, SimpleDoubleCoordStore markLoc)
	{
		if(crossDimensional || (markLoc.world == WorldHelper.getWorldID(e)))
		{
			TeleportHelper.teleportEntity(e, markLoc);
		}
		else
		{
			if(caster instanceof PlayerCaster)
				MessageHelper.sendMessage(((PlayerCaster) caster).getCaster(), "dc.recall.crossdim");
			Helper.playFizzleNoise(new SimpleDoubleCoordStore(e));
		}
	}

	private void respawnAttempt(PlayerCaster pc,EntityLivingBase ent)
	{
		EntityPlayer pl = pc.getCaster();
		World w = pl.getEntityWorld();
		int respawnDim = w.provider.getRespawnDimension((EntityPlayerMP) pl);
		w = WorldHelper.getWorld(respawnDim);
		ChunkCoordinates spawn = pl.getBedLocation(respawnDim);
		if(spawn == null)
			spawn = w.provider.getRandomizedSpawnPoint();
		if(spawn != null)
		{
			while(!(w.isAirBlock(spawn.posX, spawn.posY, spawn.posZ) && w.isAirBlock(spawn.posX, spawn.posY+1, spawn.posZ)))
				spawn.posY++;
			SimpleDoubleCoordStore sdcs = new SimpleDoubleCoordStore(respawnDim, spawn.posX+0.5, spawn.posY, spawn.posZ+0.5);
			transfer(pc, ent, sdcs);
			return;
		}
		Helper.playFizzleNoise(new SimpleDoubleCoordStore(ent));
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(ServerHelper.isClient()) return;
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase e = (EntityLivingBase) ent;
		if(caster instanceof EntityCaster)
		{
			EntityCaster ec = (EntityCaster) caster;
			if(!(MagicConfig.recallOthers || Helper.isCaster(ec, e))) return;
			NBTTagCompound nbt = ec.getExtraData();
			if((config == 4) && (ec instanceof PlayerCaster))
			{
				respawnAttempt((PlayerCaster)ec, e);
			}
			else if((config == 5) && nbt.hasKey("markLocDeath"))
			{
				SimpleDoubleCoordStore markLoc = SimpleDoubleCoordStore.readFromNBT(nbt.getCompoundTag("markLocDeath"));
				transfer(caster, e,markLoc);
			}
			else if(nbt.hasKey("markLoc" + config))
			{
				SimpleDoubleCoordStore markLoc = SimpleDoubleCoordStore.readFromNBT(nbt.getCompoundTag("markLoc"+config));
				transfer(caster, e,markLoc);
			}
			else
			{
				if(caster instanceof PlayerCaster)
					MessageHelper.sendMessage(((PlayerCaster) caster).getCaster(), "dc.recall.missing " + config);
				Helper.playFizzleNoise(new SimpleDoubleCoordStore(e));
			}
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
		return 5;
	}

	@Override
	public String getConfigDescription(int val)
	{
		if(val <= 3)
			return "dc.recall.slot " + val;
		if(val == 4)
			return "dc.recall.spawn";
		if(val == 5)
			return "dc.recall.death";
		return "";
	}

}

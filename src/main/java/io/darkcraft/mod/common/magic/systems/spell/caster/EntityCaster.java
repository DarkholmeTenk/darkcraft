package io.darkcraft.mod.common.magic.systems.spell.caster;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;

import io.darkcraft.darkcore.mod.abstracts.AbstractEntityDataStore;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RaytraceHelper;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.event.spell.SpellPreCastEvent;
import io.darkcraft.mod.common.magic.systems.spell.CastType;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import skillapi.api.implement.ISkill;
import skillapi.api.internal.ISkillHandler;

public class EntityCaster extends AbstractEntityDataStore implements ICaster
{
	private final WeakReference<EntityLivingBase> caster;
	private NBTTagCompound extraData = new NBTTagCompound();
	private NBTTagCompound extraDataTransmittable = new NBTTagCompound();

	public EntityCaster(EntityLivingBase ent)
	{
		super(ent,"darkcraft.entitycaster");
		caster = new WeakReference(ent);
	}

	public EntityCaster(EntityLivingBase pl, String string)
	{
		super(pl,string);
		caster = new WeakReference(pl);
	}

	public EntityLivingBase getCaster()
	{
		return caster.get();
	}

	public ISkillHandler getHandler()
	{
		return SkillRegistry.api.getSkillHandler(getCaster());
	}

	@Override
	public double getMana()
	{
		return 100;
	}

	@Override
	public double getMaxMana()
	{
		return 100;
	}

	@Override
	public double addMana(double amount, boolean sim)
	{
		return 0;
	}

	@Override
	public boolean useMana(double amount, boolean sim)
	{
		return true;
	}

	private double height()
	{
		EntityLivingBase ent = caster.get();
		if(ent == null) return 0;
		if(ent instanceof EntityPlayer)
			return ((EntityPlayer)ent).eyeHeight;
		else
			return ent.height / 2;
	}

	public SimpleDoubleCoordStore getSpellCreationPos()
	{
		EntityLivingBase ent = caster.get();
		if(ent == null) return null;
		double dist = 1;
		double pitchRads = Math.toRadians(ent.rotationPitch + 90);
		double yawRads = Math.toRadians(ent.rotationYaw + 90);
		double hDist = dist * Math.sin(pitchRads);
		double posY = ent.posY + height() + (dist * Math.cos(pitchRads));
		double posX = ent.posX + (hDist * Math.cos(yawRads));
		double posZ = ent.posZ + (hDist * Math.sin(yawRads));
		return new SimpleDoubleCoordStore(ent.worldObj, posX, posY, posZ);
	}

	public void setVelocity(EntitySpellProjectile sp)
	{
		EntityLivingBase ent = caster.get();
		if(ent == null) return;
		Vec3 vec = MathHelper.getVecBetween(new SimpleDoubleCoordStore(ent).translate(0, height(), 0), new SimpleDoubleCoordStore(sp));
		vec = vec.normalize();
		double speed = MagicConfig.projectileSpeed;
		sp.motionX = vec.xCoord * speed;
		sp.motionY = vec.yCoord * speed;
		sp.motionZ = vec.zCoord * speed;
		sp.velocityChanged = true;
	}

	public void applyXP(Map<ISkill,Double> xpMap)
	{
		ISkillHandler handler = SkillRegistry.api.getSkillHandler(getCaster());
		if(handler != null)
		{
			for(int i = 0; i < Math.min(xpMap.size(), MagicConfig.maxSkillsXPFromSpell); i++)
			{
				Entry<ISkill,Double> maxEntry = null;
				for(Entry<ISkill, Double> entry : xpMap.entrySet())
					if((maxEntry == null) || (entry.getValue() > maxEntry.getValue()))
						maxEntry = entry;
				double xp = Math.max(maxEntry.getValue(), 5);
				double mult = 2.5 - (2*(handler.getLevelPercent(maxEntry.getKey())));
				xp *= mult;
				handler.addXP(maxEntry.getKey(), xp);
				maxEntry.setValue(0.0);
			}
		}
	}

	private void castProjectile(Spell spell)
	{
		//if(castTouch(spell)) return;
		SimpleDoubleCoordStore dcs = getSpellCreationPos();
		if(dcs == null) return;
		EntitySpellProjectile esp = new EntitySpellProjectile(this, spell, dcs);
		setVelocity(esp);
		dcs.getWorldObj().spawnEntityInWorld(esp);
	}

	private boolean castTouch(Spell spell)
	{
		EntityLivingBase c = getCaster();
		if(c == null) return false;
		MovingObjectPosition pos = RaytraceHelper.rayTrace(c, MagicConfig.touchCastDistance, MagicConfig.traceLiquids, Entity.class);
		if(pos == null)
			return false;
		if(pos.typeOfHit == MovingObjectType.BLOCK)
			spell.apply(this, new SimpleCoordStore(c.worldObj, pos), pos.sideHit);
		else
			spell.apply(this, pos.entityHit);
		return true;
	}

	private void castSelf(Spell spell)
	{
		spell.apply(this, getCaster());
	}

	private boolean doCast(Spell spell, boolean useMana)
	{
		double cost = spell.getCost(this);
		SpellPreCastEvent spce = new SpellPreCastEvent(spell, this, cost);
		MinecraftForge.EVENT_BUS.post(spce);
		if(!spce.isCanceled())
			return useMana ? useMana(spce.getCost(), false) : true;
		return false;
	}

	@Override
	public void cast(Spell spell, boolean useMana)
	{
		EntityLivingBase ent = getCaster();
		if(ent == null) return;
		if(doCast(spell,useMana))
		{
			if(spell.type == CastType.SELF)
				castSelf(spell);
			else
			{
				if(!castTouch(spell))
					if(spell.type == CastType.PROJECTILE)
						castProjectile(spell);
			}
		}
	}

	@Override
	public void cast(Spell spell, SimpleCoordStore block, int side, boolean useMana)
	{
		if(block == null) return;
		if(spell.type == CastType.SELF) return;
		if(doCast(spell,useMana))
			spell.apply(this, block, side);
	}

	@Override
	public void cast(Spell spell, Entity ent, boolean useMana)
	{
		if(ent == null) return;
		if((spell.type == CastType.SELF) && !(ent.equals(getCaster()))) return;
		if(doCast(spell, useMana))
			spell.apply(this, ent);
	}

	@Override
	public SimpleDoubleCoordStore getCoords()
	{
		EntityLivingBase ent = getCaster();
		if(ent != null)
			return new SimpleDoubleCoordStore(ent);
		return null;
	}

	@Override
	public void init(Entity entity, World world){}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(!extraData.hasNoTags())
			nbt.setTag("dcCasterExDa", extraData);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("dcCasterExDa"))
			extraData = nbt.getCompoundTag("dcCasterExDa");
		else if(nbt.hasKey("extraData"))
			extraData = nbt.getCompoundTag("extraData");
		else if(!extraData.hasNoTags())
			extraData = new NBTTagCompound();
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		if(!extraDataTransmittable.hasNoTags())
			nbt.setTag("dcCasterExDaT", extraDataTransmittable);
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		if(nbt.hasKey("dcCasterExDaT"))
			extraDataTransmittable = nbt.getCompoundTag("dcCasterExDaT");
		else if(nbt.hasKey("extraDataT"))
			extraDataTransmittable = nbt.getCompoundTag("extraDataT");
		else if(!extraDataTransmittable.hasNoTags())
			extraDataTransmittable = new NBTTagCompound();
	}

	@Override
	public boolean notifyArea(){return false;}

	public NBTTagCompound getExtraData()
	{
		return extraData;
	}

	public NBTTagCompound getExtraDataT()
	{
		return extraDataTransmittable;
	}
}

package io.darkcraft.mod.common.magic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.event.caster.PlayerCasterManaRegenEvent;
import io.darkcraft.mod.common.magic.event.spell.SpellPreCastEvent;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.staff.StaffHelper;
import io.darkcraft.mod.common.magic.items.staff.StaffHelperFactory;
import io.darkcraft.mod.common.magic.systems.effects.AbstractDamageResistEffect;
import io.darkcraft.mod.common.magic.systems.effects.EffectSoulTrap;
import io.darkcraft.mod.common.magic.systems.effects.SSEffectManaRegen;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.network.PlayerCasterPacketHandler;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.SkillRegistry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import skillapi.api.implement.ISkill;
import skillapi.api.internal.events.EntitySkillChangeEvent;

public class MagicEventHandler
{
	public static MagicEventHandler i;
	{
		i = this;
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);;
	}

	public static final Set<PlayerCaster> updateQueue = Collections.synchronizedSet(new HashSet());
	@SubscribeEvent
	public void tickHandler(TickEvent event)
	{
		if(event.phase != Phase.END) return;
		if(ServerHelper.isIntegratedClient()) return;
		if((event.type == Type.SERVER) || (event.type == Type.CLIENT))
		{
 			PlayerCaster.tickAll();
			synchronized(updateQueue)
			{
				Iterator<PlayerCaster> iter = updateQueue.iterator();
				while(iter.hasNext())
				{
					PlayerCaster pc = iter.next();
					EntityPlayer pl = pc.getCaster();
					if(!(pl instanceof EntityPlayerMP))
					{
						if(pl != null)
							System.err.println("Player no EntityPlayerMP");
						iter.remove();
						continue;
					}
					EntityPlayerMP epl = (EntityPlayerMP) pl;
					if((epl.playerNetServerHandler == null) || (epl.playerNetServerHandler.netManager == null)) continue;
					pc.sendUpdate();
					iter.remove();
				}
			}
		}
	}

	@SubscribeEvent
	public void entityConstruction(EntityConstructing event)
	{
		Entity ent = event.entity;
		if((ent instanceof EntityPlayer) && !(ent instanceof FakePlayer))
			ent.registerExtendedProperties("dcPC", new PlayerCaster((EntityPlayer) ent));
	}

	private float handleEvent(EntityLivingBase ent, DamageSource ds, float amount)
	{
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		for(AbstractEffect e : ees.getEffects())
		{
			if(e instanceof AbstractDamageResistEffect)
			{
				AbstractDamageResistEffect adre = (AbstractDamageResistEffect) e;
				amount = adre.getNewDamage(ds, amount);
				if(amount <= 0)
					return 0;
			}
		}
		return amount;
	}

	@SubscribeEvent
	public void entityDamage(LivingHurtEvent event)
	{
		event.ammount = handleEvent(event.entityLiving, event.source, event.ammount);
	}

	@SubscribeEvent
	public void entityAttacked(LivingAttackEvent event)
	{
		float dam = handleEvent(event.entityLiving, event.source, event.ammount);
		if(dam <= 0)
			event.setCanceled(true);
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void entityDeath(LivingDeathEvent event)
	{
		if(event.isCanceled()) return;
		EntityLivingBase ent = event.entityLiving;
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		if(ees.hasEffect("darkcraft.soultrap"))
		{
			EffectSoulTrap st = (EffectSoulTrap) ees.getEffect("darkcraft.soultrap");
			ICaster cast = st.caster;
			if(cast != null)
				SoulGem.fill(cast, ent);
		}
		if(ent instanceof EntityPlayer)
		{
			EntityPlayer pl = (EntityPlayer)ent;
			PlayerCaster pc = Helper.getPlayerCaster(pl);
			new SimpleDoubleCoordStore(pl).writeToNBT(pc.getExtraData(),"markLocDeath");
		}
	}

	@SubscribeEvent
	public void handleInteract(PlayerInteractEvent event)
	{
		EntityPlayer pl = event.entityPlayer;
		if(pl instanceof FakePlayer) return;
		if((event.action != Action.LEFT_CLICK_BLOCK) && MagicConfig.castWithHand)
		{
			if(pl.getHeldItem() != null) return;
			if(!pl.isSneaking()) return;
			PlayerCaster pc = Helper.getPlayerCaster(pl);
			if(pc == null) return;
			Spell sp = pc.getCurrentSpell();
			if(sp == null) return;
			event.setCanceled(true);
			if(ServerHelper.isClient())
			{
				String un = PlayerHelper.getUsername(pl);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("pln", un);
				nbt.setString("rca", "yup");
				DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.disc);
				DarkcoreMod.networkChannel.sendToServer(dp);
			}
			if(ServerHelper.isClient())return;
			pc.cast(sp, true);
		}
	}

	@SubscribeEvent
	public void handleSkillChange(EntitySkillChangeEvent event)
	{
		EntityLivingBase ent = event.ent;
		if(!(ent instanceof EntityPlayer))return;
		boolean f = false;
		for(ISkill sk : SkillRegistry.magicSkills)
			if(sk == event.skill)
				f = true;
		if(!f) return;
		PlayerCaster pc = Helper.getPlayerCaster((EntityPlayer) ent);
		pc.updateMaxMana();
	}

	@SubscribeEvent
	public void handleManaRegen(PlayerCasterManaRegenEvent event)
	{
		PlayerCaster pc = event.playerCaster;
		EntityPlayer pl;
		if((pc == null) || ((pl = pc.getCaster()) == null)) return;
		EntityEffectStore ees = EffectHandler.getEffectStore(pl);
		if(ees == null) return;
		AbstractEffect eff = ees.getEffect("darkcraft.ssmanaregen");
		if(eff == null) return;
		SSEffectManaRegen ssemr = (SSEffectManaRegen) eff;
		int s = ssemr.magnitude+1;
		double m = 1 + (s /2.5);
		event.regenAmount *= m;
	}

	@SubscribeEvent
	public void handleStaffCast(SpellPreCastEvent event)
	{
		if(!(event.caster instanceof PlayerCaster)) return;
		PlayerCaster pc = (PlayerCaster) event.caster;
		EntityPlayer pl = pc.getCaster();
		if(pl == null) return;
		StaffHelper staff = StaffHelperFactory.getHelper(pl.getHeldItem());
		if(staff == null) return;
		event.setCost(event.getCost() * 0.75);
	}
}

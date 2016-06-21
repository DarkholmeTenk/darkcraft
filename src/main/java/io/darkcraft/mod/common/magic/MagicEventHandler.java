package io.darkcraft.mod.common.magic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.event.caster.PlayerCasterManaRegenEvent;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.systems.effects.AbstractDarkcraftEffect;
import io.darkcraft.mod.common.magic.systems.effects.EffectSoulTrap;
import io.darkcraft.mod.common.magic.systems.effects.SSEffectManaRegen;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.network.PlayerCasterPacketHandler;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
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
		if(ent instanceof EntityPlayer)
			ent.registerExtendedProperties("dcPC", new PlayerCaster((EntityPlayer) ent));
	}

	@SubscribeEvent
	public void entityDamage(LivingHurtEvent event)
	{
		EntityLivingBase ent = event.entityLiving;
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		DamageSource ds = event.source;
		if(ds== DamageSource.fall)
		{
			if(ees.hasEffect("darkcraft.fly"))
				event.setCanceled(true);
			int ff = AbstractDarkcraftEffect.getMagnitude(ees, "darkcraft.featherfall");
			if((ff >= 5) || (event.ammount < (ff*2)))
				event.setCanceled(true);
			else if(ff > 0)
				event.ammount -= (event.ammount * ff) / 5;
		}
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
	}

	@SubscribeEvent
	public void handleInteract(PlayerInteractEvent event)
	{
		EntityPlayer pl = event.entityPlayer;
		if((event.action != Action.LEFT_CLICK_BLOCK) && MagicConfig.castWithHand)
		{
			if(pl.getHeldItem() != null) return;
			if(!pl.isSneaking()) return;
			PlayerCaster pc = Helper.getPlayerCaster(pl);
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
}

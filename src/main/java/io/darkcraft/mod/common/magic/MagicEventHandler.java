package io.darkcraft.mod.common.magic;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.component.impl.effects.EffectSoulTrap;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.network.PlayerCasterPacketHandler;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.SkillRegistry;

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
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import skillapi.api.implement.ISkill;
import skillapi.api.internal.events.EntitySkillChangeEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;

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
			if(ees.hasEffect("darkcraft.fly"))
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
	}

	@SubscribeEvent
	public void handleInteract(PlayerInteractEvent event)
	{
		EntityPlayer pl = event.entityPlayer;
		if((event.action != Action.LEFT_CLICK_BLOCK) && MagicConfig.castWithHand)
		{
			if(pl.getHeldItem() != null) return;
			if(!pl.isSneaking()) return;
			if(ServerHelper.isClient() && (event.action == Action.RIGHT_CLICK_AIR))
			{
				String un = PlayerHelper.getUsername(pl);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("pln", un);
				nbt.setString("rca", "yup");
				DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.disc);
				DarkcoreMod.networkChannel.sendToServer(dp);
			}
			if(ServerHelper.isClient())return;
			PlayerCaster pc = Helper.getPlayerCaster(pl);
			Spell sp = pc.getCurrentSpell();
			if(sp != null)
			{
				pc.cast(sp, true);
				event.setCanceled(true);
			}
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
}

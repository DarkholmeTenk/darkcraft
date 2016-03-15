package io.darkcraft.mod.common.magic;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.network.PlayerCasterPacketHandler;
import io.darkcraft.mod.common.registries.MagicConfig;

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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
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
	public void tickHandler(ServerTickEvent event)
	{
		if((event.phase == Phase.END) && (event.type == Type.SERVER))
		{
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
				DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.pcDisc);
				DarkcoreMod.networkChannel.sendToServer(dp);
			}
			if(ServerHelper.isClient())return;
			PlayerCaster pc = Helper.getPlayerCaster(pl);
			Spell sp = pc.getCurrentSpell();
			if(sp != null)
			{
				pc.cast(sp);
				event.setCanceled(true);
			}
		}
	}
}

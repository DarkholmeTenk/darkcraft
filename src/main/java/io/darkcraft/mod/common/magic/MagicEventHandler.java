package io.darkcraft.mod.common.magic;

import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MagicEventHandler
{
	{
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);;
	}

	@SubscribeEvent
	public void entityConstruction(EntityConstructing event)
	{
		Entity ent = event.entity;
		if(ent instanceof EntityPlayer)
			ent.registerExtendedProperties("dcPC", new PlayerCaster((EntityPlayer) ent));
	}

	public void entityDamage(LivingHurtEvent event)
	{
		EntityLivingBase ent = event.entityLiving;
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		DamageSource ds = event.source;
		if(ds== DamageSource.fall)
			if(ees.hasEffect("darkcraft.fly"))
				event.setCanceled(true);
	}
}

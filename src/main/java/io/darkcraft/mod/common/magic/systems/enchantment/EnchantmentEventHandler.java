package io.darkcraft.mod.common.magic.systems.enchantment;

import java.util.WeakHashMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class EnchantmentEventHandler
{
	public static final EnchantmentEventHandler eeh = new EnchantmentEventHandler();
	static
	{
		MinecraftForge.EVENT_BUS.register(eeh);
		FMLCommonHandler.instance().bus().register(eeh);
	}

	@SubscribeEvent
	public void tooltipHandler(ItemTooltipEvent event)
	{
		ItemStack is = event.itemStack;
		Enchantment e = EnchantmentHelper.getEnchantment(is);
		if(e != null)
		{
			event.toolTip.add("Enchantment: " + e.getName());
		}
	}

	private WeakHashMap<EntityPlayer, ArmorEnchantment[]> armorEnchMap = new WeakHashMap();
	private ArmorEnchantment[] getArmorEnchArr(EntityPlayer pl, int size)
	{
		if(!armorEnchMap.containsKey(pl))
			armorEnchMap.put(pl, new ArmorEnchantment[size]);
		return armorEnchMap.get(pl);
	}

	@SubscribeEvent
	public void tickHandler(TickEvent e)
	{
		if(ServerHelper.isClient()) return;
		if(e.phase != Phase.END) return;
		if(e.type != Type.SERVER) return;
		for(EntityPlayer pl : PlayerHelper.getAllPlayers())
		{
			ItemStack[] armor = pl.inventory.armorInventory;
			ArmorEnchantment[] enchArr = getArmorEnchArr(pl, armor.length);
			for(int i = 0; i < armor.length; i++)
			{
				ArmorEnchantment ae = EnchantmentHelper.getArmorEnchantment(armor[i]);
				if(ae != enchArr[i])
				{
					if((enchArr[i] != null) && enchArr[i].isApplied(pl))
						enchArr[i].remove(pl);
					if(ae != null)
						if(!ae.isApplied(pl))
							ae.apply(pl);
					enchArr[i] = ae;
				}
			}
		}
	}
}

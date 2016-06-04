package io.darkcraft.mod.common.tech.network;

import gnu.trove.map.hash.THashMap;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;

import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class NetworkHelper
{
	public static boolean canReach(SimpleCoordStore a, SimpleCoordStore b)
	{
		if((a == null) || (b == null)) return false;
		return a.distance(b) <= 16;
	}

	private static Map<Integer,NetworkRegistry> netRegs = new THashMap();
	public static NetworkRegistry getNR(int world)
	{
		if(ServerHelper.isClient()) return null;
		if(!netRegs.containsKey(world))
		{
			NetworkRegistry nr = new NetworkRegistry(world);
			nr.load();
			nr.save();
			netRegs.put(world, nr);
			return nr;
		}
		return netRegs.get(world);
	}

	public static void clearNRs()
	{
		netRegs.clear();
	}

	@SubscribeEvent
	public void tickEvent(WorldTickEvent event)
	{
		if((event.phase == Phase.END) && !ServerHelper.isIntegratedClient())
		{
			int w = WorldHelper.getWorldID(event.world);
			if(netRegs.containsKey(w))
				netRegs.get(w).tick();
		}
	}
}

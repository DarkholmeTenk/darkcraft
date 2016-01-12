package io.darkcraft.mod.common.magic.field;

import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.registries.MagicConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class MagicFieldFactory
{
	public static final MagicFieldFactory		factory				= new MagicFieldFactory();
	private static ExecutorService				executor			= Executors.newCachedThreadPool();
	private static HashMap<Integer, MagicField>	fieldMap			= new HashMap();
	private static ArrayList<MagicFieldTicker>	fieldTickerList		= new ArrayList();
	private static ArrayList<MagicFieldTicker>	fieldTickerCallList	= new ArrayList();
	private int									tt					= 0;

	private MagicFieldFactory()
	{
	}

	public static void clear()
	{
		fieldMap.clear();
		fieldTickerList.clear();
		factory.tt = 0;
	}

	private static MagicField getNewMagicField(int dim)
	{
		MagicField field = new MagicField(dim);
		if(ServerHelper.isServer())
		{
			field.load();
			field.save();
			field.sendUpdate();
		}

		fieldMap.put(dim, field);
		if(ServerHelper.isClient())
			return field;
		MagicFieldTicker ticker = new MagicFieldTicker(field);
		fieldTickerList.add(ticker);
		return field;
	}

	public static MagicField getMagicField(int dim)
	{
		if(ServerHelper.isClient())
			throw new RuntimeException("Client attempted to get Magic Field!");
		if (fieldMap.containsKey(dim))
			return fieldMap.get(dim);
		else
			return getNewMagicField(dim);
	}

	public static MagicField getMagicField(TileEntity te)
	{
		return getMagicField(WorldHelper.getWorldID(te));
	}

	public static MagicField getMagicField(World w)
	{
		return getMagicField(WorldHelper.getWorldID(w));
	}

	public static MagicField getMagicField(Entity ent)
	{
		return getMagicField(WorldHelper.getWorldID(ent));
	}

	@SubscribeEvent
	public void tick(ServerTickEvent event)
	{
		if (event.phase != Phase.END) return;
		if ((++tt % MagicConfig.fieldTicks) == 0)
		{
			fieldTickerCallList.clear();
			for (MagicFieldTicker ticker : fieldTickerList)
				if (ticker.shouldTick()) fieldTickerCallList.add(ticker);
			try
			{
				if (fieldTickerCallList.size() > 0) executor.invokeAll(fieldTickerCallList);
			}
			catch (InterruptedException e)
			{

			}
		}
		if((tt % 40) == 0)
		{
			for(MagicField f : fieldMap.values())
			{
				f.sendUpdate();
				f.save();
			}
		}
	}
}

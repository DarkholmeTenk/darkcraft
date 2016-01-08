package io.darkcraft.mod.common.magic.field;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.registries.MagicConfig;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public class MagicFieldTicker implements Callable<Boolean>
{
	public final MagicField toTick;

	public MagicFieldTicker(MagicField _toTick)
	{
		toTick = _toTick;
	}

	public boolean shouldTick()
	{
		return toTick.getSize() > 0;
	}

	@Override
	public Boolean call() throws Exception
	{
		synchronized(toTick.field)
		{
			if(toTick.field.size() == 0)
				return false;
			Iterator<Map.Entry<SimpleCoordStore, Double>> iter = toTick.field.entrySet().iterator();
			while(iter.hasNext())
			{
				Map.Entry<SimpleCoordStore, Double> entry = iter.next();
				if(entry.getValue() == null)
				{
					iter.remove();
					continue;
				}
				double v = entry.getValue();

				v = (v * MagicConfig.fieldDecay) - MagicConfig.fieldDecrease;

				if((v < MagicConfig.fieldDecrease) || (v < 0.1))
					iter.remove();
				else
					entry.setValue(v);
			}
		}
		toTick.markDirty();
		return true;
	}

}

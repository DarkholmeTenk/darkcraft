package io.darkcraft.mod.common.magic.systems.symbolic.impl.selectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.RaytraceHelper;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicRune;
import io.darkcraft.mod.common.magic.systems.symbolic.impl.modifiers.Area;
import io.darkcraft.mod.common.magic.systems.symbolic.type.Modifier;
import io.darkcraft.mod.common.magic.systems.symbolic.type.Selector.EntitySelector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class PlayerSelector extends EntitySelector
{
	private static final int RADIUS_PER_LEVEL = 4;
	
	private int level = 0;

	@Override
	public String id()
	{
		return "humanus";
	}
	
	public int radius()
	{
		return (level + 1) * RADIUS_PER_LEVEL;
	}

	private int lastQuery = -100;
	private List<EntityLivingBase> players = new ArrayList<>();
	private List<EntityLivingBase> unmod = Collections.unmodifiableList(players);
	
	@Override
	public List<EntityLivingBase> getAffectedEntities(MagicRune rune)
	{
		World w = rune.getWorldObj();
		if(lastQuery != -100 && w != null && rune.tt > lastQuery + 5)
		{
			lastQuery = rune.tt;
			players.clear();
			SimpleCoordStore coords = rune.coords();
			SimpleDoubleCoordStore center = coords.getCenter();
			double radius = radius();
			AxisAlignedBB aabb = coords.getAABB(1).expand(radius, radius, radius);
			List<?> ents = w.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			for(Object o : ents)
			{
				if(!(o instanceof EntityPlayer)) continue;
				EntityPlayer player = (EntityPlayer) o;
				MovingObjectPosition mop = RaytraceHelper.rayTraceBlocks(w, center.vec3(), player.getPosition(1f), false);
				if(mop == null)
					players.add(player);
			}
		}
		return unmod;
	}

	@Override
	public boolean addModifier(Modifier modifier)
	{
		if(modifier instanceof Area)
			level++;
		else
			return false;
		return true;
	}

}

package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.spell.Spell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerCaster extends EntityCaster implements IExtendedEntityProperties
{
	private List<Spell> knownSpells = new ArrayList<Spell>();
	private Set<IComponent> knownComponents = new HashSet<IComponent>();

	public PlayerCaster(EntityPlayer pl)
	{
		super(pl);
	}

	public void learnSpell(Spell spell)
	{

	}

	@Override
	public void saveNBTData(NBTTagCompound nbt)
	{
		synchronized(knownSpells)
		{
			int i = 0;
			for(Spell s : knownSpells)
				if(s != null)
				{
					NBTTagCompound snbt = new NBTTagCompound();
					s.writeToNBT(snbt);
					nbt.setTag("ks"+(i++), snbt);
				}
		}
		synchronized(knownComponents)
		{
			int i = 0;
			for(IComponent comp : knownComponents)
			{
				if(comp == null) continue;
				String id = comp.id();
				nbt.setString("kc"+(i++), id);
			}
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt)
	{
		synchronized(knownSpells)
		{
			int i = 0;
			knownSpells.clear();
			while(nbt.hasKey("ks"+i))
			{
				NBTTagCompound snbt = nbt.getCompoundTag("ks"+i);
				Spell s = Spell.readFromNBT(snbt);
				if(s != null)
					knownSpells.add(s);
				i++;
			}
		}
		synchronized(knownComponents)
		{
			int i = 0;
			knownComponents.clear();
		}
	}

	@Override
	public void init(Entity entity, World world)
	{

	}

}

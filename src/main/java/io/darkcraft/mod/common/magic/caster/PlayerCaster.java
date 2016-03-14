package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.magic.SpellPartRegistry;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.network.PlayerCasterPacketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerCaster extends EntityCaster implements IExtendedEntityProperties
{
	private List<Spell> knownSpells = new ArrayList<Spell>();
	private List<Spell> unmodSpells = Collections.unmodifiableList(knownSpells);
	private Set<IComponent> knownComponents = new HashSet<IComponent>();
	private int currentSpell = 0;

	public PlayerCaster(EntityPlayer pl)
	{
		super(pl);
	}

	private void sortSpells()
	{
		Collections.sort(knownSpells, Spell.SpellNameComparator.withSkill);
	}

	public void learnSpell(Spell spell)
	{
		synchronized(knownSpells)
		{
			knownSpells.add(spell);
			for(ComponentInstance ci : spell.components)
				knownComponents.add(ci.component);
			sortSpells();
		}
		sendUpdate();
	}

	@Override
	public EntityPlayer getCaster()
	{
		EntityLivingBase ent = super.getCaster();
		if(ent != null)
			return (EntityPlayer)ent;
		return null;
	}

	public Set<IComponent> getKnownComponents()
	{
		EntityPlayer ent = getCaster();
		if((ent != null) && ent.capabilities.isCreativeMode)
			return SpellPartRegistry.getAllComponents();
		return knownComponents;
	}

	public boolean allComponentsKnown(Spell spell)
	{
		Set<IComponent> kc = getKnownComponents();
		for(ComponentInstance ci : spell.components)
			if(!kc.contains(ci.component))
				return false;
		return true;
	}

	public List<Spell> getKnownSpells()
	{
		return unmodSpells;
	}

	public void setCurrentSpell(int index)
	{
		currentSpell = index;
		if(ServerHelper.isClient())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("pln", PlayerHelper.getUsername(getCaster()));
			nbt.setInteger("curSpellIndex", currentSpell);
			DarkcoreMod.networkChannel.sendToServer(new DataPacket(nbt,PlayerCasterPacketHandler.pcDisc));
		}
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
			sortSpells();
		}
		synchronized(knownComponents)
		{
			int i = 0;
			knownComponents.clear();
			while(nbt.hasKey("kc"+i))
				knownComponents.add(SpellPartRegistry.getComponent(nbt.getString("kc"+(i++))));
		}
	}

	private void sendUpdate()
	{
		EntityPlayer pl = getCaster();
		if(pl == null) return;
		if(ServerHelper.isClient())
		{
			String un = PlayerHelper.getUsername(pl);

		}
		else if(ServerHelper.isServer())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			saveNBTData(nbt);
			DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.pcDisc);
			DarkcoreMod.networkChannel.sendTo(dp,(EntityPlayerMP) pl);
		}
	}

	@Override
	public void init(Entity entity, World world)
	{

	}

}

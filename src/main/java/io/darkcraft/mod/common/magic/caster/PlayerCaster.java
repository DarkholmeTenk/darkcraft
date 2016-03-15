package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.magic.MagicEventHandler;
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
	private int currentSpell = -1;
	private int[] hotkeys = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

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
		if(spell == null)
		{
			System.err.println("Attempt to learn null spell");
			return;
		}
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
		if(currentSpell == index) return;
		currentSpell = index;
		if(ServerHelper.isClient())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("pln", PlayerHelper.getUsername(getCaster()));
			nbt.setInteger("curSpellIndex", currentSpell);
			DarkcoreMod.networkChannel.sendToServer(new DataPacket(nbt,PlayerCasterPacketHandler.pcDisc));
		}
	}

	public Spell getCurrentSpell()
	{
		if((currentSpell < 0) || (currentSpell>=knownSpells.size())) return null;
		return knownSpells.get(currentSpell);
	}

	public Spell getSpell(int index)
	{
		if(knownSpells.size() == 0) return null;
		if(index < 0 ) return getSpell(index+knownSpells.size());
		if(index >= knownSpells.size()) return getSpell(index - knownSpells.size());
		return knownSpells.get(index);
	}

	public int getIndex(Spell spell)
	{
		return knownSpells.indexOf(spell);
	}

	public char getHotkey(int index)
	{
		for(int i = 0; i < 10; i++)
			if(hotkeys[i] == index)
				return (char) ('0'+i);
		return '-';
	}

	public int getHotkeyIndex(int slot)
	{
		return hotkeys[slot];
	}

	public void setHotkey(int index, char c)
	{
		int slot = c-'0';
		if((slot < 0) || (slot > 9)) return;
		if(hotkeys[slot]==index) return;
		for(int i = 0; i < 10; i++)
			if(i == slot)
				hotkeys[i] = index;
			else if(hotkeys[i] == index)
				hotkeys[i] = -1;
		sendUpdate();
	}

	public void removeIndex(int index)
	{
		EntityPlayer pl = getCaster();
		if(pl == null) return;
		String un = PlayerHelper.getUsername(pl);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("pln", un);
		nbt.setInteger("rem", index);
		DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.pcDisc);
		DarkcoreMod.networkChannel.sendToServer(dp);
	}

	public int getCurrentSpellIndex()
	{
		return currentSpell;
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
		nbt.setInteger("cs", currentSpell);
		nbt.setIntArray("hotkeys", hotkeys);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt)
	{
		if(nbt.hasKey("rem"))
		{
			int x = nbt.getInteger("rem");
			if((x >= 0) && (x < knownSpells.size()))
			{
				knownSpells.remove(x);
				for(int i = 0; i < 10; i++)
				{
					if(hotkeys[i] == x)
						hotkeys[i] = -1;
					else if(hotkeys[i] > x)
						hotkeys[i]--;
				}
				if(currentSpell == x)
					currentSpell = -1;
				else if(currentSpell > x)
					currentSpell--;
				sendUpdate();
			}
		}
		else
		{
			currentSpell = nbt.hasKey("cs") ? nbt.getInteger("cs") : -1;
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
			if(nbt.hasKey("hotkeys"))
				hotkeys = nbt.getIntArray("hotkeys");
		}
	}

	public void sendUpdate()
	{
		EntityPlayer pl = getCaster();
		if(pl == null) return;
		if(ServerHelper.isClient() || pl.worldObj.isRemote)
		{
			String un = PlayerHelper.getUsername(pl);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("pln", un);
			nbt.setInteger("curSpellIndex", currentSpell);
			DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.pcDisc);
			DarkcoreMod.networkChannel.sendToServer(dp);
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
		if(ServerHelper.isServer())
			MagicEventHandler.updateQueue.add(this);
	}

}

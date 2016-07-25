package io.darkcraft.mod.common.magic.systems.spell.caster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.magic.MagicEventHandler;
import io.darkcraft.mod.common.magic.event.caster.PlayerCasterManaRegenEvent;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.SpellPartRegistry;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.network.PlayerCasterPacketHandler;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import skillapi.api.implement.ISkill;
import skillapi.api.internal.ISkillHandler;

public class PlayerCaster extends EntityCaster<EntityPlayer> implements IExtendedEntityProperties
{
	private static Set<PlayerCaster> registeredCasters = Collections.newSetFromMap(new WeakHashMap<PlayerCaster,Boolean>());
	public static void tickAll()
	{
		synchronized(registeredCasters)
		{
			for(PlayerCaster pc : registeredCasters)
				pc.tick();
		}
	}

	private List<Spell> knownSpells = new ArrayList<Spell>();
	private List<Spell> unmodSpells = Collections.unmodifiableList(knownSpells);
	private Set<IComponent> knownComponents = new HashSet<IComponent>();
	private int currentSpell = -1;
	private int[] hotkeys = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public PlayerCaster(EntityPlayer pl)
	{
		super(pl, "dcPC");
		if(ServerHelper.isServer())
			spellUpdateID = 1;
	}

	private void sortSpells()
	{
		/*Spell[] spells = new Spell[hotkeys.length];
		for(int i = 0; i < spells.length; i++)
			if(hotkeys[i] != -1)
				spells[i] = knownSpells.get(hotkeys[i]);
			else
				spells[i] = null;
		Collections.sort(knownSpells, Spell.SpellNameComparator.withSkill);
		for(int i = 0; i < spells.length; i++)
			if(spells[i] != null)
				hotkeys[i] = knownSpells.indexOf(spells[i]);*/
		spellUpdateID++;
	}

	public void learnComponent(IComponent c)
	{
		knownComponents.add(c);
		sendUpdate();
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
				learnComponent(ci.component);
			spellUpdateID++;
			sortSpells();
		}
		sendUpdate();
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
			DarkcoreMod.networkChannel.sendToServer(new DataPacket(nbt,PlayerCasterPacketHandler.disc));
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

	public void setHotkeys(int[] nhk)
	{
		for(int i = 0; (i<10)&&(i<nhk.length);i++)
			hotkeys[i]=nhk[i];
	}

	public void removeIndex(int index)
	{
		EntityPlayer pl = getCaster();
		if(pl == null) return;
		String un = PlayerHelper.getUsername(pl);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("pln", un);
		nbt.setInteger("rem", index);
		DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.disc);
		DarkcoreMod.networkChannel.sendToServer(dp);
		synchronized(knownSpells)
		{
			handleRemoval(index);
		}
	}

	public int getCurrentSpellIndex()
	{
		return currentSpell;
	}

	@Override
	public void saveNBTData(NBTTagCompound lnbt)
	{
		writeToNBT(lnbt);
		writeTransmittable(lnbt);
		NBTTagCompound nbt = new NBTTagCompound();
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
			nbt.setInteger("spellUpdateID", spellUpdateID);
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
		nbt.setDouble("maxMana", maxMana);
		nbt.setDouble("mana", mana);
		lnbt.setTag("dcpc", nbt);
	}

	private void handleRemoval(int x)
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
	}

	private int spellUpdateID = -1;
	@Override
	public void loadNBTData(NBTTagCompound lnbt)
	{
		if(lnbt.hasKey("rem"))
		{
			int x = lnbt.getInteger("rem");
			if((x >= 0) && (x < knownSpells.size()))
			{
				handleRemoval(x);
				spellUpdateID++;
			}
		}
		else if(lnbt.hasKey("dcpc"))
		{
			super.loadNBTData(lnbt);
			NBTTagCompound nbt = lnbt.getCompoundTag("dcpc");
			currentSpell = nbt.hasKey("cs") ? nbt.getInteger("cs") : -1;
			synchronized(knownSpells){ synchronized(knownComponents)
			{
				int i = 0,j = 0;
				if(spellUpdateID != nbt.getInteger("ksid"))
				{
					while(nbt.hasKey("ks"+i))
					{
						NBTTagCompound snbt = nbt.getCompoundTag("ks"+i);
						Spell s = Spell.readFromNBT(snbt);
						if(s != null)
						{
							if(knownSpells.size() <= j)
								knownSpells.add(s);
							else if(!s.equals(knownSpells.get(j)))
								knownSpells.set(j, s);
							j++;
						}
						else if(knownSpells.size() >= j)
							knownSpells.remove(j);
						i++;
					}
					for(;i<knownSpells.size();i++)
						knownSpells.remove(i);
					spellUpdateID = nbt.getInteger("ksid");
				}
				i = 0;
				knownComponents.clear();
				while(nbt.hasKey("kc"+i))
					knownComponents.add(SpellPartRegistry.getComponent(nbt.getString("kc"+(i++))));
				if(nbt.hasKey("hotkeys"))
					hotkeys = nbt.getIntArray("hotkeys");
				sortSpells();
			}}
			maxMana = nbt.getDouble("maxMana");
			mana = nbt.getDouble("mana");
		}
	}

	@Override
	public boolean sendUpdate()
	{
		EntityPlayer pl = getCaster();
		if(pl == null) return false;
		if(ServerHelper.isClient() || pl.worldObj.isRemote)
		{
			String un = PlayerHelper.getUsername(pl);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("pln", un);
			nbt.setInteger("curSpellIndex", currentSpell);
			nbt.setIntArray("hotkeys", hotkeys);
			DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.disc);
			DarkcoreMod.networkChannel.sendToServer(dp);
		}
		else if(ServerHelper.isServer())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			saveNBTData(nbt);
			DataPacket dp = new DataPacket(nbt,PlayerCasterPacketHandler.disc);
			DarkcoreMod.networkChannel.sendTo(dp,(EntityPlayerMP) pl);
		}
		return false;
	}

	@Override
	public void init(Entity entity, World world)
	{
		if(ServerHelper.isServer())
			MagicEventHandler.updateQueue.add(this);
		synchronized(registeredCasters)
		{
			registeredCasters.add(this);
		}
	}

	private double mana = 100;
	private double maxMana = 100;
	@Override
	public double getMana()
	{
		return mana;
	}

	public double getManaRegen()
	{
		if(MagicConfig.regenPercent)
			return getMaxMana() * MagicConfig.manaRegenRate;
		return MagicConfig.manaRegenRate;
	}

	@Override
	public double getMaxMana()
	{
		return maxMana;
	}

	public void updateMaxMana()
	{
		EntityPlayer pl = getCaster(); if(pl == null) return;
		ISkillHandler sh = SkillRegistry.api.getSkillHandler(pl);
		double tl = 0;
		for(ISkill sk : SkillRegistry.magicSkills)
			tl += Math.pow(sh.getLevelPercent(sk), 2.5);
		tl /= SkillRegistry.magicSkills.length;
		maxMana = (tl * (MagicConfig.maxMana - MagicConfig.minMana)) + MagicConfig.minMana;
		sendUpdate();
	}

	@Override
	public boolean useMana(double amount, boolean sim)
	{
		EntityPlayer pl = getCaster();
		if((pl != null) && pl.capabilities.isCreativeMode) return true;
		boolean r = amount <= getMana();
		if(r && !sim) mana -= amount;
		if(ServerHelper.isServer() && !sim)
			sendUpdate();
		return r;
	}

	@Override
	public double addMana(double amount, boolean sim)
	{
		EntityPlayer pl = getCaster();
		if(pl == null) return amount;
		double a = MathHelper.clamp(amount, 0, getMaxMana() - getMana());
		if(!sim)
			mana += a;
		return amount - a;
	}

	public void setMana(double amount)
	{
		mana = MathHelper.clamp(amount,0,getMaxMana());
	}

	private void regenMana()
	{
		double manaRegen = Math.min(getMaxMana()-mana, Math.max(1,getManaRegen()));
		PlayerCasterManaRegenEvent ev = new PlayerCasterManaRegenEvent(this, manaRegen);
		MinecraftForge.EVENT_BUS.post(ev);
		mana = MathHelper.clamp(mana + ev.regenAmount, 0, getMaxMana());
	}

	int tt = 0;
	public void tick()
	{
		tt++;
		if((tt % 20) != 0) return;
		if(maxMana < MagicConfig.minMana) updateMaxMana();
		regenMana();
	}

}

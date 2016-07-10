package io.darkcraft.mod.common.network;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.component.IConfigurableComponent;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class SpellSelectionPacketHandler implements IDataPacketHandler
{
	public static String disc = "darkcraft.spellselection";

	@Override
	public void handleData(NBTTagCompound data)
	{
		if(ServerHelper.isClient()) return;
		int type = data.getInteger("type");
		EntityPlayer pl = PlayerHelper.getPlayer(data.getString("player"));
		if(pl == null) return;
		switch(type)
		{
			case 0: configChange(pl, data); break;
		}
	}

	private void configChange(EntityPlayer pl, NBTTagCompound data)
	{
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		Spell sp = pc.getSpell(data.getInteger("spellSlot"));
		if(sp == null) return;
		ComponentInstance ci = sp.components[data.getInteger("ciSlot") % sp.components.length];
		if(ci.component instanceof IConfigurableComponent)
		{
			IConfigurableComponent cc = (IConfigurableComponent) ci.component;
			ci.config = MathHelper.clamp(data.getInteger("newVal"), cc.getMinConfig(), cc.getMaxConfig());
		}
	}

	private static NBTTagCompound getNBT(EntityPlayer pl, int type)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("player", PlayerHelper.getUsername(pl));
		nbt.setInteger("type", type);
		return nbt;
	}

	public static void sendConfigChange(EntityPlayer pl, int spellSlot, int ciSlot, int newValue)
	{
		NBTTagCompound nbt = getNBT(pl, 0);
		nbt.setInteger("spellSlot", spellSlot);
		nbt.setInteger("ciSlot", ciSlot);
		nbt.setInteger("newVal", newValue);
		DarkcoreMod.networkChannel.sendToServer(new DataPacket(nbt, disc));
	}
}

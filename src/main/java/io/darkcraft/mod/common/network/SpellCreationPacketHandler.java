package io.darkcraft.mod.common.network;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.gui.server.SpellCreationContainer;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.magic.tileent.SpellCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SpellCreationPacketHandler implements IDataPacketHandler
{
	public final static byte spellCreationDisc = 40;

	@Override
	public void handleData(NBTTagCompound data)
	{
		if(ServerHelper.isServer())
			handleClientPacket(data);
		else
			handleServerPacket(data);
	}

	private void handleServerPacket(NBTTagCompound data)
	{
		// TODO Auto-generated method stub

	}

	//Server responds to client request to create spell
	private void handleClientPacket(NBTTagCompound data)
	{
		SimpleCoordStore pos = SimpleCoordStore.readFromNBT(data);
		String un = data.getString("plname");
		EntityPlayer pl = PlayerHelper.getPlayer(un);
		Spell spell = Spell.readFromNBT(data);
		if((pos == null) || (pl == null) || (spell == null))return;
		TileEntity te = pos.getTileEntity();
		if(!(te instanceof SpellCreator) || !((SpellCreator)te).isValidStructure() || !((SpellCreator)te).isUser(pl))
		{
			MessageHelper.sendMessage(pl, "Invalid spell creator");
			return;
		}
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		if(!pc.allComponentsKnown(spell))
		{
			MessageHelper.sendMessage(pl, "You do not know all of the components of this spell");
			return;
		}
		((SpellCreator)te).setSpell(spell);
	}

	public static DataPacket getDataPacket(SpellCreationContainer container)
	{
		String un = PlayerHelper.getUsername(Minecraft.getMinecraft().thePlayer);
		SimpleCoordStore pos = container.te.coords();
		NBTTagCompound nbt = new NBTTagCompound();
		pos.writeToNBT(nbt);
		Spell spell = new Spell(container.name,container.spellSoFar.components);
		spell.writeToNBT(nbt);
		nbt.setString("plname", un);
		return new DataPacket(nbt,spellCreationDisc);
	}
}

package io.darkcraft.darkcraft.mod.common.command;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import io.darkcraft.darkcore.mod.abstracts.AbstractCommand;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcraft.mod.common.spellsystem.BaseSpell;

public class CreateSpellCommand extends AbstractCommand
{

	@Override
	public String getCommandName()
	{
		return "darkcraftcreatespell";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return null;
	}

	@Override
	public void addAliases(List<String> list)
	{
		list.add("dccreatespell");
	}
	
	private void sortStuff(EntityPlayer pl, String sStr, String eStr, String mStr)
	{
		BaseSpell b = BaseSpell.readFromStrings(sStr, eStr, mStr);
		if(b == null)
			return;
		NBTTagCompound nbt = new NBTTagCompound();
		b.writeToNBT(nbt);
		ItemStack is = pl.getHeldItem();
		if(is.stackTagCompound == null)
			is.stackTagCompound = new NBTTagCompound();
		is.stackTagCompound.setTag("currentSpell", nbt);
	}

	@Override
	public void commandBody(ICommandSender sender, String[] astring)
	{
		if(astring.length < 2)
			return;
		//2 args: shapes, effects
		if(astring.length == 2)
		{
			if(!isPlayer(sender))
				return;
			sortStuff((EntityPlayer)sender,astring[0],astring[1],null);
		}
		if(astring.length == 3)
		{
			EntityPlayer pl = ServerHelper.getPlayer(astring[0]);
			if(pl != null)
				sortStuff(pl,astring[1],astring[2],null);
			else if(sender instanceof EntityPlayer)
				sortStuff((EntityPlayer)sender,astring[0],astring[1],astring[2]);
		}
	}

}

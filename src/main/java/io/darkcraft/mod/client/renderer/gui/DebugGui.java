package io.darkcraft.mod.client.renderer.gui;

import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiList;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiOpenable;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftLabel;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonCross;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonTick;
import io.darkcraft.mod.client.renderer.gui.system.spells.ComponentInstanceMod;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableBackground;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.SpellPartRegistry;
import net.minecraft.inventory.Container;

public class DebugGui extends DarkcraftGui
{

	public DebugGui(Container ct)
	{
		super(ct, new ScalableBackground(512,512));
		DarkcraftGuiList list = new DarkcraftGuiList(20,20, 100,128);
		for(int i = 0; i < 64; i++)
			list.addElement(new DarkcraftLabel(0,0,84,String.format("TestLabel%02d", i)));
		addElement(list);
		addElement(new ButtonCross(this));
		addElement(new ButtonTick(this));
		DarkcraftGuiOpenable openable = new DarkcraftGuiOpenable(135, 20, new DarkcraftLabel(0,0,256,"Test openable"));
		DarkcraftGuiList list2 = new DarkcraftGuiList(5,20, 230, 200);
		for(IComponent c : SpellPartRegistry.getAllComponents())
			list2.addElement(new ComponentInstanceMod(0,0,198,c));
		openable.addElement(list2);
		openable.addElement(new DarkcraftLabel(5,222,120, "Below list"));
		addElement(openable);
	}

	@Override
	public void clickableClicked(IClickable c, String id)
	{
		if(id.equals("tick"))
		{
			DarkcraftGui subGui = new DarkcraftGui(inventorySlots, new ScalableBackground(256,1000));
			subGui.addElement(new ButtonCross(subGui));
			subGui.addElement(new DarkcraftLabel(20,20,216,"This is a test lol"));
			openSubGui(subGui);
		}
		else
			super.clickableClicked(c, id);
	}

}

package io.darkcraft.mod.client.renderer.gui;

import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.daedric.DaedricTextfield;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonCross;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonTick;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableBackground;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableInternal;
import io.darkcraft.mod.common.magic.gui.ChalkContainer;
import io.darkcraft.mod.common.network.ChalkGuiPacketHandler;

public class ChalkGui extends DarkcraftGui
{
	private ChalkContainer cc;

	private DaedricTextfield textField = new DaedricTextfield(18,19,new ScalableInternal(476,20));

	public ChalkGui(ChalkContainer cont)
	{
		super(cont, new ScalableBackground(512,58));
		cc = cont;
		inventoryGui = false;
		textField.text = cont.chalk;
		addElement(textField);
		addElement(new ButtonTick(this));
		addElement(new ButtonCross(this));
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if("tick".equals(id))
		{
			ChalkGuiPacketHandler.updateChalk(cc.pl, textField.text);
			close();
		}
		super.clickableClicked(c, id, button);
	}
}

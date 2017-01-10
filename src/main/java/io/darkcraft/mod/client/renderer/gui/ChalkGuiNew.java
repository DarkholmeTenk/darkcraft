package io.darkcraft.mod.client.renderer.gui;

import io.darkcraft.mod.client.renderer.gui.chalk.ChalkBackground;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableBackground;

public class ChalkGuiNew extends DarkcraftGui
{

	public ChalkGuiNew()
	{
		super(null, new ScalableBackground(300,300));
		addElement(new ChalkBackground(50,50));
	}

}

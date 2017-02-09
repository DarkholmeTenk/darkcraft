package io.darkcraft.mod.client.renderer.gui;

import io.darkcraft.mod.client.renderer.gui.chalk.ChalkBackground;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonTick;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableBackground;
import io.darkcraft.mod.common.magic.gui.ChalkContainerNew;

public class ChalkGuiNew extends DarkcraftGui
{
	private ChalkBackground background;
	private ChalkContainerNew container;

	public ChalkGuiNew(ChalkContainerNew container)
	{
		super(null, new ScalableBackground(300,300));
		this.container = container;
		addElement(background = new ChalkBackground(50,30, container));
		background.registerThings(this);
		addElement(new ButtonTick(this));
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if("tick".equals(id))
		{
			container.update(background.getText());
			close();
		}
		else
			super.clickableClicked(c, id, button);
	}

}

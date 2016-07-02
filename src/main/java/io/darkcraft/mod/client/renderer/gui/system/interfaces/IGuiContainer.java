package io.darkcraft.mod.client.renderer.gui.system.interfaces;

import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;

public interface IGuiContainer
{
	public void addElement(AbstractGuiElement e);

	public void clickableClicked(IClickable c, String id);

	public void typableChanged(ITypable t);

	public void recalc();
}
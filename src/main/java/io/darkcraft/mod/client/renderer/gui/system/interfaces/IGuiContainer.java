package io.darkcraft.mod.client.renderer.gui.system.interfaces;

import io.darkcraft.darkcore.mod.datastore.WindowSpaceStore;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;

public interface IGuiContainer<T extends AbstractGuiElement>
{
	public void addElement(T e);

	public void removeElement(T e);

	public T getHovered(int mouseX, int mouseY);

	public void clickableClicked(IClickable c, String id);

	public void typableChanged(ITypable t);

	public void recalc();

	public WindowSpaceStore getWindowSpace(T e);
}

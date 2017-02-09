package io.darkcraft.mod.client.renderer.gui.chalk;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement.ChangeWatcher;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiTextfield;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;

public class ChalkButton extends AbstractGuiElement implements IClickable, ChangeWatcher
{
	private static final ResourceLocation texture = new ResourceLocation(DarkcraftMod.modName, "textures/chalk/glow.png");
	private static final UVStore text = new UVStore(0,1, 0,0.5);
	private static final UVStore textSel = new UVStore(0,1, 0.5,1);

	private static ChalkButton selected;

	private String currentText = "";
	private DarkcraftGuiTextfield textField;

	public ChalkButton(int _x, int _y, String text)
	{
		super(_x, _y, 40, 40);
		selected = null;
		currentText = text;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		UVStore uv = null;
		if(this == selected)
			uv = textSel;
		else if(!currentText.isEmpty())
			uv = text;
		if(uv != null)
		{
			RenderHelper.bindTexture(texture);
			RenderHelper.uiFace(0, 0, 40, 40, 0, uv, true);
		}
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		selected = this;
		if(textField != null)
			textField.text = currentText;
		triggerChange();
		return true;
	}

	@Override
	public void change(Object source)
	{
		if((source == textField) && (selected == this))
		{
			currentText = textField.text;
			triggerChange();
		}
	}

	public void setField(DarkcraftGuiTextfield field)
	{
		textField = field;
		textField.registerWatcher(this);
	}

	public String getString()
	{
		return currentText;
	}
}

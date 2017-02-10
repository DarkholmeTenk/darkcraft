package io.darkcraft.mod.client.renderer.gui.chalk;

import com.google.common.base.Strings;

import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiTextfield;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.HorizontalScrollbar;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableInternal;
import io.darkcraft.mod.common.magic.gui.ChalkContainerNew;
import io.darkcraft.mod.common.magic.systems.symbolic.ChalkType;

public class ChalkBackground extends AbstractGuiElement
{
	private ChalkType type;
	private final ChalkContainerNew container;

	private HorizontalScrollbar scrollbar;
	private DarkcraftGuiTextfield field;
	private ChalkButton[] buttons = {};

	public ChalkBackground(int _x, int _y, ChalkContainerNew container)
	{
		super(_x, _y, 200, 200);
		type = ChalkType.WHITE;
		this.container = container;
		for(ChalkType temp : ChalkType.values())
			if(temp.getLevel() >= container.defaultStrings.length)
			{
				type = temp;
				break;
			}
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		RenderHelper.bindTexture(type.getTexture());
		RenderHelper.uiFace(0, 0, 200, 200, 0, UVStore.defaultUV, true);
	}

	public void setLevel(ChalkType type)
	{
		if((type == null) || (type.ordinal() > container.maxChalkLevel.ordinal()))
			type = ChalkType.WHITE;
		if((parent != null) && (type != this.type))
		{
			this.type = type;
			refreshGui();
		}
	}

	public void registerThings(DarkcraftGui gui)
	{
		if(container.maxChalkLevel != ChalkType.WHITE)
		{
			scrollbar = new HorizontalScrollbar(x, y + h, h, 1);
			scrollbar.setMinMax(0, container.maxChalkLevel.ordinal());
			scrollbar.setValue(type.ordinal());
			scrollbar.registerWatcher(new ChangeWatcher()
			{
				@Override
				public void change(Object bar)
				{
					setLevel(ChalkType.values()[scrollbar.asInt()]);
				}
			});
			gui.addElement(scrollbar);
		}
		if(scrollbar != null)
			gui.addElement(field = new DarkcraftGuiTextfield(x,y+h+scrollbar.h,new ScalableInternal(200,scrollbar.h+2)));
		else
			gui.addElement(field = new DarkcraftGuiTextfield(x,y+h,new ScalableInternal(200,18)));
		refreshGui();
	}

	private void refreshGui()
	{
		if(parent == null)
			return;
		for(ChalkButton button : buttons)
		{
			field.removeWatcher(button);
			parent.removeElement(button);
		}
		buttons = new ChalkButton[type.getLevel()];
		Pair<Integer,Integer>[] locations = type.getPositions();
		for(int i = 0; i < buttons.length; i++)
		{
			int x = 80 + (locations[i].a * 40);
			int y = 80 - (locations[i].b * 40);
			if(i < container.defaultStrings.length)
				buttons[i] = new ChalkButton(this.x + x,this.y + y, Strings.nullToEmpty(container.defaultStrings[i]));
			else
				buttons[i] = new ChalkButton(this.x + x, this.y + y, "");
			buttons[i].setField(field);
			parent.addElement(buttons[i]);
		}
		field.text = "";
	}

	public ChalkType getType()
	{
		return type;
	}

	public String[] getText()
	{
		String[] text = new String[type.getLevel()];
		for(int i = 0; i < text.length; i++)
		{
			ChalkButton button = buttons[i];
			if(button != null)
				text[i] = Strings.nullToEmpty(button.getString());
			else
				text[i] = "";
		}
		return text;
	}
}

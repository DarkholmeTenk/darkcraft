package io.darkcraft.mod.client.renderer.gui.system.spells;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StatCollector;

public class DarkcraftGuiSpellComponent extends AbstractGuiElement implements IClickable
{
	public boolean showIcon = true;
	public Colour labelColour = Colour.white;
	public boolean shadow = true;
	public final String componentName;
	public final IComponent component;

	public DarkcraftGuiSpellComponent(int _x, int _y, int width, IComponent comp)
	{
		super(_x, _y, width, 16);
		component = comp;
		componentName = StatCollector.translateToLocal(comp.getUnlocalisedName());
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(showIcon)
		{
			RenderHelper.bindTexture(component.getIcon());
			RenderHelper.uiFace(0, 0, 16, 16, 0, component.getIconLocation(), true);
		}
		int x = showIcon ? 18 : 2;
		FontRenderer fr = RenderHelper.getFontRenderer();
		fr.drawString(fr.trimStringToWidth(componentName, w-x-2), x, 3, labelColour.asInt, shadow);
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		parent.clickableClicked(this, "component." + component.id(), button);
		return true;
	}

}

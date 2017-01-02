package io.darkcraft.mod.client.renderer.gui.spellcreator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StatCollector;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.client.renderer.gui.DarkcraftWrappingLabel;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiList;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftLabel;
import io.darkcraft.mod.client.renderer.gui.system.daedric.DaedricLabel;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonCross;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonTick;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.HorizontalScrollbar;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.SkillIcon;
import io.darkcraft.mod.client.renderer.gui.system.spells.ComponentIcon;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableBackground;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDescriptiveMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.component.INoAreaComponent;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;

import skillapi.api.implement.ISkill;

public class ComponentCreationWidget extends DarkcraftGui
{
	private static final int labelX = 20;
	private static final int sliderX = 86;
	private static final int sliderW = 320;
	private static final int descX = 410;
	private static final int descW = 120;

	private final SpellCreationGuiNew parent;
	private final IComponent component;

	private final HorizontalScrollbar magnitude;
	private final HorizontalScrollbar duration;
	private final HorizontalScrollbar area;

	private DarkcraftLabel magnitudeLabel;
	private DarkcraftLabel durationLabel;
	private DarkcraftLabel areaLabel;
	private final DarkcraftLabel costLabel;
	private final DarkcraftLabel baseCostLabel;
	private DarkcraftGuiList<DarkcraftLabel> descriptors;

	private ComponentInstance ci;

	public ComponentCreationWidget(SpellCreationGuiNew parent, IComponent component)
	{
		super(null, new ScalableBackground(512, component instanceof IDescriptiveMagnitudeComponent ? 260 : 160));
		this.parent = parent;
		this.component = component;

		addElement(new ButtonCross(this));
		addElement(new ButtonTick(this));

		addElement(new ComponentIcon(labelX, labelX, 32, 32, component));
		String name = StatCollector.translateToLocal(component.getUnlocalisedName());
		addElement(new DaedricLabel(labelX+36, labelX+6	, name));
		addElement(new DarkcraftLabel(labelX+36, labelX+22, 150, name));

		if(component.getMainSkill() != null)
		{
			ISkill skill = component.getMainSkill();
			String skillName = StatCollector.translateToLocal(skill.getName());
			addElement(new DaedricLabel(guiW-labelX-36-LetterRenderer.width(skillName), labelX+6, skillName));
			addElement(new DarkcraftLabel(guiW-labelX-36-RenderHelper.getFontRenderer().getStringWidth(skillName), labelX+22, skillName));
			addElement(new SkillIcon(guiW-labelX-32,labelX, 32, 32, skill));
		}

		int sliderY = 42;
		if(component instanceof IMagnitudeComponent)
		{
			sliderY += 20;
			IMagnitudeComponent imc = (IMagnitudeComponent)component;
			addElement(new DaedricLabel(labelX,sliderY+2,"Magnitude:"));
			addElement(magnitude = new HorizontalScrollbar(sliderX,sliderY,sliderW, 1));
			magnitude.setMinMax(imc.getMinMagnitude(), imc.getMaxMagnitude());
			addElement(magnitudeLabel = new DarkcraftLabel(descX, sliderY+5, descW, ""));
		}
		else
			magnitude = null;

		if(component instanceof IDurationComponent)
		{
			sliderY += 20;
			IDurationComponent imc = (IDurationComponent)component;
			addElement(new DaedricLabel(labelX,sliderY+2,"Duration:"));
			addElement(duration = new HorizontalScrollbar(sliderX,sliderY,sliderW, 1));
			duration.setMinMax(imc.getMinDuration(), imc.getMaxDuration());
			addElement(durationLabel = new DarkcraftLabel(descX, sliderY+5, descW, ""));
		}
		else
			duration = null;

		if(!(component instanceof INoAreaComponent))
		{
			sliderY += 20;
			addElement(new DaedricLabel(labelX,sliderY+2,"Area"));
			addElement(area = new HorizontalScrollbar(sliderX,sliderY,sliderW, 1));
			area.setMinMax(0,5);
			addElement(areaLabel = new DarkcraftLabel(descX, sliderY+5, descW, ""));
		}
		else
			area = null;

		if(component instanceof IDescriptiveMagnitudeComponent)
			addElement(descriptors = new DarkcraftGuiList<>(labelX, 124, guiW-(labelX * 2), 100));

		addElement(new DaedricLabel(labelX, guiH-32, "Cost:"));
		addElement(costLabel = new DarkcraftLabel(labelX+40, guiH - 28, 100,""));
		addElement(new DaedricLabel(labelX+144, guiH-32, "Base Cost:"));
		addElement(baseCostLabel = new DarkcraftLabel(labelX+214, guiH-28,100,""));

		refresh();
	}

	private void refresh()
	{
		int mag = magnitude == null ? 0 : (int) magnitude.asInt();
		int dur = duration == null ? 0 : (int) duration.asInt();
		int are = area == null ? 0 : (int) area.asInt();
		if(magnitudeLabel != null)
			magnitudeLabel.text = ""+mag;
		if(durationLabel != null)
			durationLabel.text = MathHelper.getTimeString(dur);
		if(areaLabel != null)
			areaLabel.text = are + " block radius";
		if(descriptors != null)
		{
			List<String> terms = new ArrayList<>();
			((IDescriptiveMagnitudeComponent)component).getDescription(terms, mag);
			descriptors.clear();
			for(String s : terms)
				descriptors.addElement(new DarkcraftWrappingLabel(2,0, descriptors.iW, StatCollector.translateToLocal(s)));
		}
		ci = new ComponentInstance(component, mag, dur, are);
		costLabel.text = String.format("%.1f", ci.getCost(parent.pc, parent.pcSkills));
		baseCostLabel.text = String.format("%.1f", ci.getCost(null, null));
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if((id != null) && id.startsWith("scroll"))
			refresh();
		else if((id != null) && id.equals("tick"))
		{
			if(parent != null)
				parent.addComponent(ci);
			close();
		}
		else
			super.clickableClicked(c, id, button);
	}

	public ComponentInstance getCI()
	{
		return ci;
	}
}

package io.darkcraft.mod.common.magic.gui;

import java.util.List;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.component.IDurationComponent;
import io.darkcraft.mod.common.magic.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.component.INoAreaComponent;
import io.darkcraft.mod.common.magic.spell.CastType;
import io.darkcraft.mod.common.magic.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.magic.tileent.SpellCreator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import skillapi.api.implement.ISkill;

public class SpellCreationContainer extends Container
{
	public static final Spell dummySpell = new Spell("Dummy",new ComponentInstance[]{});
	public static final int GUI_ID = 1397;
	public final SpellCreator te;

	public CastType castType = CastType.PROJECTILE;
	public Spell spellSoFar = dummySpell;
	public IComponent selectedComponent = null;
	public ComponentInstance selectedComponentInstance = null;
	public float magnitude;
	public float duration;
	public float area;
	public int kcScroll = 0;
	public List<IComponent> knownComponents;
	public int selectedLine = -1;
	public String name = "";
	public ISkill mainSkill = null;

	public void mainBoxClicked(int x, int y)
	{
		if((y >= 83) && (y <355) && (x >= 30) && (x < 155))
		{
			int index = (y - 83) / 11;
			if((index >= 0) && (index < knownComponents.size()))
			{
				selectedComponent = knownComponents.get(index);
				magnitude = 0;
				duration = 0;
				area = 0;
				updateCI();
			}
		}
		if((x >= 89) && (x <= 155) && (y >= 358) && (y <= 372))
			castType = CastType.values()[(castType.ordinal() + 1) % CastType.values().length];
		if((x >= 162) && (x <= 372) && (y >= 82) && (y <= 354))
		{
			int index = (y - 82) / 11;
			removeCI(index);
		}
	}

	public void sliderBoxClicked(int x, int y)
	{
		if((x >= 78) && (x <=232))
		{
			int yx = y-36;
			int line = yx / 17;
			if((line < 0) || (line > 2)) return;
			float pos = 0;
			if(line == 0) pos=magnitude;
			if(line == 1) pos=duration;
			if(line == 2) pos=area;
			pos = 80+(pos*150);
			if((x < (pos-3)) || (x > (pos+9)))return;
			if((line == 0) && !(selectedComponent instanceof IMagnitudeComponent)) return;
			if((line == 1) && !(selectedComponent instanceof IDurationComponent)) return;
			if((line == 2) && (selectedComponent instanceof INoAreaComponent)) return;
			selectedLine = line;
		}
		if((y >= 84) && (y <= 100))
		{
			if((x >= 284) && (x <= 300))
				addCI();
			else if((x >= 0) && (x <= 16))
				selectedComponent = null;
		}
	}

	public void moveSelectedSlider(int x, int y)
	{
		float np = Math.min(1, Math.max(0,(x-80)/150f));
		if(selectedLine == 0) magnitude = np;
		if(selectedLine == 1) duration = np;
		if(selectedLine == 2) area = np;
	}

	public String getAreaString()
	{
		int areas = getArea();
		return ""+areas;
	}

	public String getMagnitudeString()
	{
		int mag = getMagnitude();
		return ""+mag;
	}

	public int getArea()
	{
		int max = 5;
		return MathHelper.round(max * area);
	}

	public int getMagnitude()
	{
		if(!(selectedComponent instanceof IMagnitudeComponent)) return 0;
		IMagnitudeComponent c = (IMagnitudeComponent) selectedComponent;
		return MathHelper.round(((c.getMaxMagnitude() - c.getMinMagnitude()) * magnitude) + c.getMinMagnitude());
	}

	public int getDuration()
	{
		if(!(selectedComponent instanceof IDurationComponent)) return 0;
		IDurationComponent c = (IDurationComponent) selectedComponent;
		return MathHelper.round(((c.getMaxDuration() - c.getMinDuration()) * duration) + c.getMinDuration());
	}

	public String getDurationString()
	{
		int seconds = getDuration();
		return MathHelper.getTimeString(seconds);
	}

	public void updateCI()
	{
		int mag = getMagnitude();
		int dur = getDuration();
		int area = getArea();
		selectedComponentInstance = new ComponentInstance(selectedComponent,mag,dur,area);
	}

	public void addCI()
	{
		ComponentInstance[] ciA = new ComponentInstance[spellSoFar.components.length + 1];
		for(int i = 0; i < spellSoFar.components.length; i++)
			ciA[i] = spellSoFar.components[i];
		ciA[spellSoFar.components.length] = selectedComponentInstance;
		spellSoFar = new Spell(spellSoFar.name,ciA);
		selectedComponent = null;
		updateSpell();
	}

	public void removeCI(int index)
	{
		if((index < 0) || (index >= spellSoFar.components.length)) return;
		ComponentInstance[] ciA = new ComponentInstance[spellSoFar.components.length - 1];
		int j = 0;
		for(int i = 0; i < spellSoFar.components.length; i++)
			if(i != index)
				ciA[j++] = spellSoFar.components[i];
		spellSoFar = new Spell(spellSoFar.name,ciA);
		updateSpell();
	}

	public void updateSpell()
	{
		mainSkill = spellSoFar.getMainSkill();
	}

	public SpellCreationContainer(SpellCreator _te)
	{
		te = _te;
	}

	@Override
	public void onContainerClosed(EntityPlayer pl)
	{
		te.closeGui();
	}

	@Override
	public boolean canInteractWith(EntityPlayer pl)
	{
		return true;
	}

}

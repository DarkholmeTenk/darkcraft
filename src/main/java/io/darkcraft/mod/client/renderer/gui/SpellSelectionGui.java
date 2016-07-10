package io.darkcraft.mod.client.renderer.gui;

import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiList;
import io.darkcraft.mod.client.renderer.gui.system.daedric.DaedricButton;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonCross;
import io.darkcraft.mod.client.renderer.gui.system.spells.SpellHover;
import io.darkcraft.mod.client.renderer.gui.system.spells.SpellItem;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableBackground;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;

public class SpellSelectionGui extends DarkcraftGui
{
	private final PlayerCaster player;
	private final DarkcraftGuiList<SpellItem> list;
	private SpellItem currentItem;
	private Spell currentSpell;
	private SpellHover hover = new SpellHover(272,0,240,256);
	private Comparator<SpellItem> currentComp = SpellItem.skillcompSorter;
	private Colour defaultColour = Colour.white;
	private Colour selectedColour = new Colour(0.2f, 0.3f, 1f);
	private Colour hoveredColour = new Colour(0.7f, 0.3f,0.2f);

	public SpellSelectionGui(PlayerCaster pc)
	{
		super(null, new ScalableBackground(256,350), 512, 350);
		sorter = new HotkeySorter(pc);
		player = pc;
		currentSpell = player.getCurrentSpell();
		hover.setSpell(pc, currentSpell);
		list = new DarkcraftGuiList(24,40,256-48,guiH-64);
		for(Spell s : player.getKnownSpells())
		{
			SpellItem si = new SpellItem(0,0,256-64,pc,s);
			if(s == currentSpell)
				setCurrent(si);
			list.addElement(si);
		}
		list.sort(currentComp);
		addElement(hover);
		addElement(list);
		addElement(new DaedricButton("sortHot",26,20,"H"));
		addElement(new DaedricButton("sortSkill",36,20,"S"));
		addElement(new DaedricButton("sortName",46,20,"Name"));
		addElement(new DaedricButton("sortCost",172,20,"Cost"));
		addElement(new ButtonCross(this));
		inventoryGui = false;
	}

	private void setCurrent(SpellItem si)
	{
		hover.setSpell(player, si == null ? null : si.spell);
		if(si == null)
		{
			if(currentItem != null)
				currentItem.colour = Colour.white;
			currentItem = null;
			currentSpell = null;
		}
		else
		{
			if(currentItem != null)
				currentItem.colour = Colour.white;
			currentItem = si;
			currentSpell = si.spell;
			si.colour = new Colour(0.3f, 0.5f, 1);
		}
	}

	private void sort(Comparator c)
	{
		currentComp = c;
		list.sort(c);
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if((c == list) && (button == 0))
		{
			player.setCurrentSpell(-1);
			setCurrent(null);
		}
		if("spellitem".equals(id))
		{
			SpellItem si = (SpellItem) c;
			if((button == 1) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				player.removeIndex(player.getIndex(si.spell));
				if(currentItem == si)
					setCurrent(null);
				list.removeElement(si);
			}
			else if(button == 0)
			{
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				{
					player.setCurrentSpell(-1);
					setCurrent(null);
				}
				else
				{
					player.setCurrentSpell(player.getIndex(si.spell));
					setCurrent(si);
				}
			}
		}
		if("sortHot".equals(id))
			sort(sorter);
		if("sortSkill".equals(id))
			if(currentComp == SpellItem.skillcompSorter)
				sort(SpellItem.skillSorter);
			else
				sort(SpellItem.skillcompSorter);
		if("sortName".equals(id))
			sort(SpellItem.nameSorter);
		if("sortCost".equals(id))
			sort(SpellItem.costSorter);
	}

	@Override
	public void hoverChanged(AbstractGuiElement newHover)
	{
		if(lastHovered instanceof SpellItem)
			((SpellItem) lastHovered).colour = currentItem == lastHovered ? selectedColour : defaultColour;

		if(newHover instanceof SpellItem)
		{
			hover.setSpell(player, ((SpellItem) newHover).spell);
			((SpellItem) newHover).colour = hoveredColour;
		}
		else if(hover.getSpell() != currentSpell)
			hover.setSpell(player, currentItem == null ? null : currentItem.spell);
	}

	@Override
	protected void keyTyped(char c, int i)
	{
		if((c >= '1') && (c <='9'))
		{
			if(lastHovered instanceof SpellItem)
				player.setHotkey(player.getIndex(((SpellItem) lastHovered).spell), c);
		}
		else
			super.keyTyped(c, i);
	}

	public final HotkeySorter sorter;
	private class HotkeySorter implements Comparator<SpellItem>
	{
		private final PlayerCaster pc;
		private HotkeySorter(PlayerCaster _pc)
		{
			pc = _pc;
		}

		@Override
		public int compare(SpellItem a, SpellItem b)
		{
			int as = pc.getHotkey(pc.getIndex(a.spell));
			int bs = pc.getHotkey(pc.getIndex(b.spell));
			if(as == bs) return SpellItem.skillcompSorter.compare(a, b);
			if(as == '-') return 1;
			if(bs == '-') return -1;
			return Integer.compare(as, bs);
		}
	};
}

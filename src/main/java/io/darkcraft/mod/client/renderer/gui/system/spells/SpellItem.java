package io.darkcraft.mod.client.renderer.gui.system.spells;

import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import net.minecraft.client.gui.FontRenderer;

public class SpellItem extends AbstractGuiElement implements IClickable
{
	private PlayerCaster pc;
	public final Spell spell;
	private final SpellIcon icon;
	private final String spellStr;
	private final String costStr;
	private final double cost;
	public Colour colour = Colour.white;
	public boolean shadow = true;

	public SpellItem(int _x, int _y, int width, PlayerCaster _pc, Spell sp)
	{
		super(_x, _y, width, 12);
		spell = sp;
		icon = new SpellIcon(10,2,8,8,sp);
		pc = _pc;
		FontRenderer fr = RenderHelper.getFontRenderer();
		int cw = 48;
		spellStr = fr.trimStringToWidth(sp.name, width - 20 - cw);
		cost = spell.getCost(pc);
		costStr = fr.trimStringToWidth(String.format("%.1f", cost), cw-2);
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		parent.clickableClicked(this, "spellitem", button);
		return true;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		GL11.glColor3f(1,1,1);
		GL11.glPushMatrix();
		GL11.glTranslatef(icon.x, icon.y, 0);
		icon.render(pticks, mouseX, mouseY);
		GL11.glPopMatrix();
		FontRenderer fr = RenderHelper.getFontRenderer();
		char slot = pc.getHotkey(pc.getIndex(spell));
		if(slot != '-')
			fr.drawString(""+slot, 2, 2, colour.asInt, shadow);
		fr.drawString(spellStr, 20, 2, colour.asInt, shadow);
		fr.drawString(costStr, w-47, 2, colour.asInt, shadow);
		GL11.glColor3f(1,1,1);
	}



	public static Comparator<SpellItem> nameSorter = new Comparator<SpellItem>(){
		@Override
		public int compare(SpellItem a, SpellItem b)
		{
			return a.spellStr.compareToIgnoreCase(b.spellStr);
		}
	};

	public static Comparator<SpellItem> costSorter = new Comparator<SpellItem>(){
		@Override
		public int compare(SpellItem a, SpellItem b)
		{
			return Double.compare(a.cost, b.cost);
		}
	};

	public static Comparator<SpellItem> skillSorter = new Comparator<SpellItem>(){
		@Override
		public int compare(SpellItem a, SpellItem b)
		{
			int c = a.spell.getMainSkill().getName().compareTo(b.spell.getMainSkill().getName());
			if(c == 0)
				return nameSorter.compare(a, b);
			return c;
		}
	};

	public static Comparator<SpellItem> skillcompSorter = new Comparator<SpellItem>(){
		@Override
		public int compare(SpellItem a, SpellItem b)
		{
			int c = a.spell.getMainSkill().getName().compareTo(b.spell.getMainSkill().getName());
			if(c == 0)
			{
				String n1 = a.spell.mostExpensiveComponent.component.getUnlocalisedName();
				String n2 = b.spell.mostExpensiveComponent.component.getUnlocalisedName();
				int c2 = n1.compareTo(n2);
				if(c2 == 0)
					return nameSorter.compare(a, b);
				return c2;
			}
			return c;
		}
	};
}

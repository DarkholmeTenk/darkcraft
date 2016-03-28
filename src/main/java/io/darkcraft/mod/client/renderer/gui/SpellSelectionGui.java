package io.darkcraft.mod.client.renderer.gui;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.ClientHelper;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.spell.Spell;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class SpellSelectionGui extends GuiScreen
{
	private static ResourceLocation guiBackground = new ResourceLocation(DarkcraftMod.modName, "textures/gui/spellselection.png");
	private static UVStore guiUV = new UVStore(0,0.5,0,1);
	private static UVStore hovTop = new UVStore(0.5,1,0,0.046875);
	private static UVStore hovMid = new UVStore(0.5,1,0.046875,0.203125);
	private static UVStore hovBot = new UVStore(0.5,1,0.203125,0.25);
	private int size = -1;
    private double sr;
    private int guiLeft;
    private int guiTop;
    private int scroll = 0;
    private FontRenderer fr;
    private EntityPlayer pl;
    private PlayerCaster pc;
    private int hover = -1;

    {
    	//width = 256;
    	//height = 512;
    	fr = Minecraft.getMinecraft().fontRenderer;
    	pl = Minecraft.getMinecraft().thePlayer;
    	pc = Helper.getPlayerCaster(pl);
    }

    private void setSize()
    {
    	size = Math.min(512, (height/64)*64);
    	sr = size / 512.0;
    	guiLeft = (int) ((width - (256*sr))/2);
    	guiTop = (int) ((height - (512*sr)) / 2);
    }

    private void drawSpell(int i, Spell sp)
    {
    	if(pc.getHotkey(scroll+i) != '-')
    		fr.drawString(""+pc.getHotkey(scroll+i), 0, 4, 16777215);
    	GL11.glColor3f(1, 1, 1);
    	ClientHelper.renderSpellIcon(sp, 8, 0, 16, 16);
    	fr.drawString(sp.name, 26, 4, i==hover?2179839:sp==pc.getCurrentSpell()?16716800:16777215);
    	String c = String.format("%8.1f", sp.getCost(pc));
    	int w = fr.getStringWidth(c);
    	fr.drawString(c, 214-w, 4, 16777215);
    }

    private void drawSpells()
    {
    	GL11.glPushMatrix();
    	List<Spell> spl = pc.getKnownSpells();
    	for(int i = 0; i <20; i++)
    	{
    		int index = scroll + i;
    		if(index >= spl.size()) break;
    		Spell s = spl.get(index);
    		drawSpell(i,s);
    		GL11.glTranslatef(0, 18, 0);
    	}
    	GL11.glPopMatrix();
    }

    private void drawHover(int x, int y)
    {
    	if(hover < 0) return;
    	List<Spell> spl = pc.getKnownSpells();
		int index = hover + scroll;
		if(index >= spl.size()) return;
		Spell spell = spl.get(index);
    	RenderHelper.bindTexture(guiBackground);
    	{
	    	int l = MathHelper.ceil(spell.components.length / 4.0f);
	    	RenderHelper.uiFace(x, y, 256, 24, 0, hovTop, true);
	    	for(int i = 0; i < l; i++)
	    		RenderHelper.uiFace(x, y+24+(80*i), 256, 80, 0, hovMid, true);
	    	RenderHelper.uiFace(x, y+24+(80*l), 256, 24, 0, hovBot, true);
    	}
    	GL11.glTranslatef(x+20, y+20, 1);
    	fr.drawString("Name: " + spell.name, 0, 0, 16777215);
    	fr.drawString("Cost:", 0, 12, 16777215);
    	String c = String.format("%8.1f",spell.getCost(pc));
    	fr.drawString(c, 95-fr.getStringWidth(c), 12, 16777215);
    	fr.drawString("Base Cost:", 100, 12, 16777215);
    	c = String.format("%8.1f",spell.getCost(null));
    	fr.drawString(c, 200-fr.getStringWidth(c), 12, 16777215);
    	String skillName = spell.getMainSkill() != null ? spell.getMainSkill().getName() : "Null";
    	fr.drawString("Skill: " + StatCollector.translateToLocal(skillName), 100, 24, 16777215);
    	fr.drawString("Components:", 0, 24, 16777215);
    	int i = 0;
    	for(ComponentInstance ci : spell.components)
    	{
    		RenderHelper.bindTexture(ci.component.getIcon());
    		RenderHelper.uiFace(0, 36+(i*18), 16, 16, 1, ci.component.getIconLocation(), true);
    		String name = StatCollector.translateToLocal(ci.toString());
    		fr.drawString(name, 20, 40+(i*18), 16777215);
    		i++;
    	}
    }

	@Override
	public void drawScreen(int a, int b, float f)
    {
		if(size == -1)
			setSize();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslated(guiLeft, guiTop, 0);
		GL11.glScaled(sr, sr, 1);
		RenderHelper.bindTexture(guiBackground);
		RenderHelper.uiFace(0, 0, 256, 512, 0, guiUV, true);
		GL11.glPushMatrix();{
			GL11.glTranslated(20, 51, 1);
			a = (int) ((a-guiLeft)/sr);
			b = (int) ((b-guiTop)/sr);
			if((a > 20) && (a < 236) && (b > 51) && (b < 493))
				hover = (b-50)/18;
			else
				hover = -1;
			drawSpells();
			GL11.glColor3f(1, 1, 1);
    	}GL11.glPopMatrix();
    	GL11.glTranslatef(0, 0, 5);
		drawHover(280,0);
		GL11.glPopMatrix();
    }

	@Override
	protected void mouseClicked(int x, int y, int c)
    {
		if(c == 0)
		{
			if(hover > -1)
			{
				List<Spell> spl = pc.getKnownSpells();
				int index = hover + scroll;
				if(index < spl.size())
				{
					pc.setCurrentSpell(index);
				}
			}
		}
		if(c == 1)
		{
			if(isShiftKeyDown())
			{
				if(hover > -1)
				{
					List<Spell> spl = pc.getKnownSpells();
					int index = hover + scroll;
					if(index < spl.size())
					{
						pc.removeIndex(index);
					}
				}
			}
		}
    }

	@Override
	protected void keyTyped(char c, int i)
    {
		if((c>='0') && (c<='9'))
		{
			if(hover > -1)
			{
				List<Spell> spl = pc.getKnownSpells();
				int index = hover + scroll;
				if(index < spl.size())
				{
					pc.setHotkey(index, c);
				}
			}
		}
		super.keyTyped(c, i);
    }

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}

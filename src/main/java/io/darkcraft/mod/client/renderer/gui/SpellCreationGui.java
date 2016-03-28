package io.darkcraft.mod.client.renderer.gui;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.component.IDurationComponent;
import io.darkcraft.mod.common.magic.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.gui.SpellCreationContainer;
import io.darkcraft.mod.common.magic.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.tileent.SpellCreator;
import io.darkcraft.mod.common.network.SpellCreationPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import skillapi.api.implement.ISkillIcon;

public class SpellCreationGui extends GuiContainer
{
    public static final int GUI_ID = 1397;

    private final SpellCreationContainer cont;
    private static ResourceLocation guiBackground = new ResourceLocation(DarkcraftMod.modName, "textures/gui/spellcreation.png");
    private static ResourceLocation guiWindow = new ResourceLocation(DarkcraftMod.modName, "textures/gui/spellcreationbox.png");
    private int size = -1;
    private double sr;
    private FontRenderer fr;
    private final GuiTextField textField = new GuiTextField(fr, 0, 0, 172, 11);

    public SpellCreationGui(SpellCreator _te)
    {
    	super(new SpellCreationContainer(_te));
    	cont = (SpellCreationContainer) inventorySlots;
    	xSize = 400;
    	ySize = 400;
    	PlayerCaster pc = Helper.getPlayerCaster(Minecraft.getMinecraft().thePlayer);
    	fr = Minecraft.getMinecraft().fontRenderer;
    	if(pc != null)
    		cont.knownComponents = Helper.sortComponents(pc.getKnownComponents());
    }

    private void face(Tessellator tess, float x, float y, float w, float h, UVStore uv, boolean start)
    {
    	if(start) tess.startDrawingQuads();
    	tess.addVertexWithUV(x, y, zLevel, uv.u, uv.v);
    	tess.addVertexWithUV(x+w, y, zLevel, uv.U, uv.v);
    	tess.addVertexWithUV(x+w, y+h, zLevel, uv.U, uv.V);
    	tess.addVertexWithUV(x, y+h, zLevel, uv.u, uv.V);
    	if(start) tess.draw();
    }

    private void drawKnownComponents(Tessellator tess)
    {
    	int left = 30;
    	for(int i = 0; i<10;i++)
    	{
    		int top = 83+(11*i);
    		int index = i + cont.kcScroll;
    		if(index >= cont.knownComponents.size()) return;
    		IComponent comp = cont.knownComponents.get(index);
    		RenderHelper.bindTexture(comp.getIcon());
    		face(tess,left,top,10,10,comp.getIconLocation(), true);
    		String n = StatCollector.translateToLocal(comp.getUnlocalisedName());
    		n = fr.trimStringToWidth(n, 110);
    		fr.drawString(n, left+12, top+2, 16777215);
    	}
    }

    private void drawSlider(Tessellator tess, int x, int y, float val)
    {
    	int size = 150;
    	face(tess,x,y,size,12,sliderSize,true);
    	face(tess,(x+(size*val))-3,y,12,12,sliderButtonSize,true);
    }

    private final static UVStore sliderScreenSize = new UVStore(0,0.75,0,0.8);
    private final static UVStore sliderButtonSize = new UVStore(0.345,0.375,0.904,1);
    private final static UVStore sliderSize = new UVStore(0.375,0.75,0.904,1);
    private void drawSliderBox(Tessellator tess)
    {
    	GL11.glPushMatrix();
    	RenderHelper.bindTexture(guiWindow);
    	GL11.glTranslated(50, 150, 1);
    	face(tess,0,0,300,100,sliderScreenSize,true);
    	if(cont.selectedComponent instanceof IMagnitudeComponent) drawSlider(tess,80,36,cont.magnitude);
    	if(cont.selectedComponent instanceof IDurationComponent) drawSlider(tess,80,53,cont.duration);
    	drawSlider(tess,80,70,cont.area);
    	if(cont.selectedComponent instanceof IMagnitudeComponent) fr.drawString(cont.getMagnitudeString(), 240, 36, 16777215);
    	if(cont.selectedComponent instanceof IDurationComponent) fr.drawString(cont.getDurationString(), 240, 53, 16777215);
    	fr.drawString(cont.getAreaString(), 240, 70, 16777215);

    	fr.drawString(String.format("%.1f",cont.selectedComponentInstance.cost), 215, 21, 16777215);
    	String n = fr.trimStringToWidth(StatCollector.translateToLocal(cont.selectedComponent.getUnlocalisedName()),120);
    	fr.drawString(n, 65, 21, 16777215);
    	GL11.glPopMatrix();
    }

    private final static UVStore bgSize = new UVStore(0,1,0,0.8);
    @Override
	public void drawScreen(int a, int b, float f)
    {
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	drawDefaultBackground();
    	if(size == -1)
    	{
	    	size = Math.min(400, (height/100)*100);
	    	sr = size / 400.0;
	    	guiLeft = (width - size)/2;
	    	guiTop = (height - size) / 2;
    	}
    	GL11.glPushMatrix();
    	GL11.glTranslated(guiLeft, guiTop, 0);
    	if(sr != 1)
    		GL11.glScaled(sr, sr, 1);
    	Tessellator tess = Tessellator.instance;
    	RenderHelper.bindTexture(guiBackground);
    	GL11.glDisable(GL11.GL_CULL_FACE);
    	face(tess, 0,0,400,400,bgSize, true);
    	drawKnownComponents(tess);
    	drawName(tess);
    	drawCurrentComponents(tess);
    	drawExtraInfo(tess);
    	if(cont.selectedComponent != null)
    		drawSliderBox(tess);
    	drawErrors(tess);
    	GL11.glPopMatrix();
    }

    private void drawErrors(Tessellator tess)
	{
		if(cont.spellSoFar.components.length == 0)
			fr.drawString("No components", 290, 39, 16711680);
		if((cont.name == null) || (cont.name.length() < 3))
			fr.drawString("Name too short", 290, 50, 16711680);
	}

	private void drawName(Tessellator tess)
	{
		fr.drawString(cont.name, 102, 49, 16777215);
	}

	private void drawCurrentComponents(Tessellator tess)
	{
		int x = 162;
		int t = 82;
		int i = 0;
		for(ComponentInstance ci : cont.spellSoFar.components)
		{
			int y = ((i++)*11) + t;
			RenderHelper.bindTexture(ci.component.getIcon());
			face(tess,x,y,10,10,ci.component.getIconLocation(), true);
			String d = ci.toString();
			d = fr.trimStringToWidth(d, 198);
			fr.drawString(d, x+11, y, 16777215);
		}
	}

	private void drawExtraInfo(Tessellator tess)
	{
		fr.drawString(String.format("%6.1f",cont.spellSoFar.getCost(null)), 284, 361, 16777215);
		fr.drawString(cont.castType.name(), 91, 361, 16777215);
		if(cont.mainSkill != null)
		{
			ISkillIcon icon = cont.mainSkill.getIcon(null);
			RenderHelper.bindTexture(icon.getResourceLocation());
			face(tess,358,64,16,16,icon.getUV(),true);
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int c)
    {
    	if(c != 0) return;
    	x = (int)((x - guiLeft) / sr);
    	y = (int)((y - guiTop) / sr);
    	if((x < 0) || (x > 400)) return;
    	if((y < 0) || (y > 400)) return;
    	if(cont.selectedComponent == null)
    	{
    		if((y >= 384) && (y <= 400))
    		{
    			if((x >= 0) && (x <= 16))
    				mc.thePlayer.closeScreen();
    			else if((x >= 384) && (x <= 400))
    			{
    				if((cont.name != null) && (cont.name.length() >= 3) && (cont.spellSoFar.components.length > 0))
    				{
    					DarkcoreMod.networkChannel.sendToServer(SpellCreationPacketHandler.getDataPacket(cont));
    					mc.thePlayer.closeScreen();
    				}
    			}
    		}
    		cont.mainBoxClicked(x, y);
    	}
    	else
    	{
    		x -= 50;
    		y-= 150;
    		cont.sliderBoxClicked(x, y);
    	}
    }

    @Override
	protected void mouseClickMove(int x, int y, int c, long time)
    {
    	if(c != 0) return;
    	if((cont.selectedComponent != null) && (cont.selectedLine != -1))
    	{
    		x = (int)((x - guiLeft) / sr) - 50;
    		y = (int)((y - guiTop) / sr) - 150;
    		cont.moveSelectedSlider(x,y);
    	}
    }

    @Override
	protected void mouseMovedOrUp(int x, int y, int c)
    {
    	if(c == 0)
    	{
    		if(cont.selectedLine != -1)
    		{
    			cont.selectedLine = -1;
    			cont.updateCI();
    		}
    	}
    }

    @Override
	protected void keyTyped(char c, int i)
    {
    	if(i == 1)
    	{
    		if(cont.selectedComponent != null)
    			cont.selectedComponent = null;
    		else
    			mc.thePlayer.closeScreen();
    	}
    	if(cont.selectedComponent == null)
    	{
    		switch(i)
    		{
    			case 14: if(cont.name.length() > 0) cont.name = cont.name.substring(0,cont.name.length() - 1); return;
    		}
    		switch(c)
    		{
    			case 211: case 199: case 203: case 205: case 207: return;
    		}
    		if(!ChatAllowedCharacters.isAllowedCharacter(c)) return;
	    	cont.name += c;
    	}
    }

    @Override
	public boolean doesGuiPauseGame()
    {
    	return false;
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		// TODO Auto-generated method stub

	}
}

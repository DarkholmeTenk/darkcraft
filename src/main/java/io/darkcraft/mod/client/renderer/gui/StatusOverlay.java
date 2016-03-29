package io.darkcraft.mod.client.renderer.gui;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.ClientHelper;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StatusOverlay
{
	public static StatusOverlay i = new StatusOverlay();
	private ResourceLocation overlayTex = new ResourceLocation(DarkcraftMod.modName,"textures/gui/overlay.png");
	private UVStore eBar = new UVStore(0,0.125,0,1);
	private UVStore fBar = new UVStore(0.125,0.25,0,0.953125);
	private UVStore sCon = new UVStore(0.625,1,0,0.1975);
	private int w;
	private int h;

	private void renderStatus(EntityPlayer pl, PlayerCaster pc, Tessellator tess)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(w, h, 0);
		RenderHelper.uiFace(-10, -130, 8, 128, 1, eBar, true);
		int h = (int) (128*(pc.getMana()/pc.getMaxMana()));
		if(h > 0)
		{
			UVStore uv = new UVStore(fBar.u,fBar.U,fBar.V-(h/128.0),fBar.V);
			RenderHelper.uiFace(-10, -5-h, 8, h, 2, uv, true);
		}
		GL11.glPopMatrix();
	}

	private void renderSpell(EntityPlayer pl, PlayerCaster pc, Tessellator tess)
	{
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		GL11.glTranslatef(w, h, 0);
		RenderHelper.uiFace(-34, -36, 24, 24, 1, sCon, true);
		if(pc.getCurrentSpell() != null)
		{
			Spell sp = pc.getCurrentSpell();
			ClientHelper.renderSpellIcon(sp, -30, -32, 16, 16);
			String s = sp.name;
			int l = fr.getStringWidth(s);
			fr.drawString(s, -14-l, -12, 16777215, true);
			if(pl.isSneaking() && Helper.canCast(pl))
			{
				int index = pc.getCurrentSpellIndex();
				ClientHelper.renderSpellIcon(pc.getSpell(index-1), -50, -32, 16, 16);
				ClientHelper.renderSpellIcon(pc.getSpell(index+1), -30, -52, 16, 16);
			}
		}
		String mana = String.format("%.0f/%.0f",pc.getMana(),pc.getMaxMana());
		int l = fr.getStringWidth(mana);
		fr.drawString(mana, -2-l, -142, 16777215, true);
		GL11.glPopMatrix();
	}

	@SubscribeEvent
	public void handlerEvent(RenderGameOverlayEvent event)
	{
		if(event.type != ElementType.HOTBAR) return;
		EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		if((pl == null) || (pc == null)) return;
		Tessellator tess = Tessellator.instance;
		w = event.resolution.getScaledWidth();
		h = event.resolution.getScaledHeight();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.bindTexture(overlayTex);
		renderStatus(pl,pc,tess);
		renderSpell(pl,pc,tess);
	}

	@SubscribeEvent
	public void mouseEvent(MouseEvent event)
	{
		EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		if(pl == null) return;
		if(Helper.canCast(pl))
			if(pl.isSneaking())
			{
				if(event.dwheel > 0)
					pc.setCurrentSpell(MathHelper.cycle(pc.getCurrentSpellIndex()+1,0,pc.getKnownSpells().size()-1));
				else if(event.dwheel < 0)
					pc.setCurrentSpell(MathHelper.cycle(pc.getCurrentSpellIndex()-1,0,pc.getKnownSpells().size()-1));
				if(event.dwheel != 0)
					event.setCanceled(true);
			}
	}
}

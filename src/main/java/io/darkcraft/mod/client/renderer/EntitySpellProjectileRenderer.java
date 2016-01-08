package io.darkcraft.mod.client.renderer;

import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class EntitySpellProjectileRenderer extends RenderEntity
{
    @Override
	public void doRender(Entity ent, double x, double y, double z, float yaw, float pitch)
    {
    	if(!(ent instanceof EntitySpellProjectile)) return;
    	EntitySpellProjectile esp = (EntitySpellProjectile) ent;
    	Spell spell = esp.spell;
    	if(spell != null)
    		bindTexture(spell.getTexture());
    	GL11.glPushMatrix();
    	GL11.glTranslated(esp.posX-RenderManager.renderPosX, esp.posY-RenderManager.renderPosY, esp.posZ-RenderManager.renderPosZ);
    	GL11.glRotated(RenderManager.instance.playerViewY, 0, -1, 0);
    	GL11.glRotated(RenderManager.instance.playerViewX, 1, 0, 0);
    	Tessellator tess = Tessellator.instance;
    	tess.startDrawingQuads();
    	tess.addVertexWithUV(-0.5, 0.5, 0, 0, 1);
    	tess.addVertexWithUV(0.5, 0.5, 0, 1, 1);
    	tess.addVertexWithUV(0.5, -0.5, 0, 1, 0);
    	tess.addVertexWithUV(-0.5, -0.5, 0, 0, 0);
    	tess.draw();
    	GL11.glPopMatrix();
    }
}

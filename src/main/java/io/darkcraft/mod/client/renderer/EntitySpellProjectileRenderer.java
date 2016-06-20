package io.darkcraft.mod.client.renderer;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class EntitySpellProjectileRenderer extends RenderEntity
{
    @Override
	public void doRender(Entity ent, double x, double y, double z, float yaw, float pitch)
    {
    	if(!(ent instanceof EntitySpellProjectile)) return;
    	EntitySpellProjectile esp = (EntitySpellProjectile) ent;
    	Spell spell = esp.spell;
    	UVStore uv = null;
    	long t = RenderHelper.getTime();
    	int f = (int) (t / 250);
    	float ang = (t / 4f) % 360;
    	if(spell != null)
    	{
    		bindTexture(spell.getTexture());
    		uv = spell.getTextureLocation(f);
    	}
    	else
    	{
    		bindTexture(MagicalRegistry.damage.getProjectileTexture());
    		uv = MagicalRegistry.damage.getProjectileLocation(f);
    	}
    	if(uv == null) uv = UVStore.defaultUV;
    	GL11.glPushMatrix();
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslated(x, y, z);
    	GL11.glRotated(RenderManager.instance.playerViewY, 0, -1, 0);
    	GL11.glRotated(RenderManager.instance.playerViewX, 1, 0, 0);
    	GL11.glRotated(ang, 0, 0, 1);
    	RenderHelper.uiFace(-0.5f, -0.5f, 1, 1, 0, uv, true);
    	//RenderHelper.face(-0.5f, -0.5f, 0, 1, 1, uv, true);
    	GL11.glPopMatrix();
    }
}

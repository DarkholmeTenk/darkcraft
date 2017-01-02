package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.common.registries.SkillRegistry;

import skillapi.api.implement.ISkill;
import skillapi.api.implement.ISkillIcon;

public class SkillIcon extends AbstractGuiElement
{
	private ISkill skill;

	public SkillIcon(int _x, int _y, int width, int height, ISkill skill)
	{
		super(_x, _y, width, height);
		this.skill = skill;
	}

	public void setSkill(ISkill skill)
	{
		this.skill = skill;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		if(skill == null) return;
		GL11.glColor3f(1, 1, 1);
		ISkillIcon skillIcon = skill.getIcon(SkillRegistry.api.getSkillHandler(Minecraft.getMinecraft().thePlayer));
		RenderHelper.bindTexture(skillIcon.getResourceLocation());
		RenderHelper.uiFace(0, 0, w, h, 0, skillIcon.getUV(), true);
	}

}

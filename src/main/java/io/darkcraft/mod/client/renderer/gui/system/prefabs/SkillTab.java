package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import java.util.Objects;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiTabbed.Tab;

import skillapi.api.implement.ISkill;

public class SkillTab extends Tab
{
	private final static ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName, "textures/gui/emptybutton24.png");
	private final static UVStore uv = UVStore.defaultUV;

	private ISkill skill;
	private final SkillIcon skillIcon;

	public SkillTab(ISkill skill)
	{
		super(skill.getID(), rl, uv);
		this.skill = skill;
		skillIcon = new SkillIcon(0,0,16,16,skill);
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		super.render(pticks, mouseX, mouseY);
		GL11.glTranslated(4, 4, 0);
		skillIcon.render(pticks, mouseX, mouseY);
	}

	@Override
	public int hashCode()
	{
		return skill.getID().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof SkillTab)) return false;
		SkillTab other = (SkillTab) obj;
		return Objects.equals(skill, other.skill);
	}

}

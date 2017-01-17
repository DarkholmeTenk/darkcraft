package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import java.util.Objects;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.Tab;

import skillapi.api.implement.ISkill;

public abstract class SkillTab<T extends AbstractGuiElement> extends Tab<T> implements Comparable<SkillTab<?>>
{
	private final static ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName, "textures/gui/emptybutton24.png");
	private final static UVStore upUV = new UVStore(0,0.5,0,1);
	private final static UVStore downUV = new UVStore(0.5,1,0,1);

	protected final ISkill skill;
	private final SkillIcon skillIcon;

	public SkillTab(ISkill skill)
	{
		super(skill.getID(), rl, upUV, rl, downUV);
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

	@Override
	public int compareTo(SkillTab<?> o)
	{
		if(o.skill == skill)
			return 0;
		if(skill == null)
			return -1;
		if((o == null) || (o.skill == null))
			return 1;
		return skill.getID().compareTo(o.skill.getID());
	}

}

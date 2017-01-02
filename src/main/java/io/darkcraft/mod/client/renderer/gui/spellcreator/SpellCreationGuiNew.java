package io.darkcraft.mod.client.renderer.gui.spellcreator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.client.renderer.gui.DarkcraftWrappingLabel;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftEnumSetButton;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGui;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiList;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiTextfield;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftLabel;
import io.darkcraft.mod.client.renderer.gui.system.daedric.DaedricLabel;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonCross;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonTick;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.SkillIcon;
import io.darkcraft.mod.client.renderer.gui.system.spells.ComponentInstanceLabel;
import io.darkcraft.mod.client.renderer.gui.system.spells.DarkcraftGuiSpellComponent;
import io.darkcraft.mod.client.renderer.gui.system.spells.SpellIcon;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableBackground;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableInternal;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.blocks.tileent.SpellCreator;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.spell.CastType;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.network.SpellCreationPacketHandler;
import io.darkcraft.mod.common.registries.SkillRegistry;

import skillapi.api.implement.ISkill;
import skillapi.api.internal.ISkillHandler;

public class SpellCreationGuiNew extends DarkcraftGui
{
	private final SpellCreator te;
	private final SpellCreationGuiNew self = this;

	private final DarkcraftGuiList<DarkcraftGuiSpellComponent> componentList;
	public final PlayerCaster pc;
	public final ISkillHandler pcSkills;

	private final DarkcraftGuiList<ComponentInstanceLabelClickable> ciList;
	private Spell spellSoFar = null;
	private final DarkcraftLabel cost;
	private final DarkcraftLabel baseCost;
	private final SpellIcon spellIcon;
	private final SkillIcon skillIcon;
	private final DaedricLabel skillNameD;
	private final DarkcraftLabel skillNameR;
	private final DarkcraftWrappingLabel errors;

	private final DarkcraftEnumSetButton<CastType> castType;
	private final DarkcraftGuiTextfield name;

	public SpellCreationGuiNew(SpellCreator te)
	{
		super(null, new ScalableBackground(544,400));
		this.te = te;
		pc = Helper.getPlayerCaster(Minecraft.getMinecraft().thePlayer);
		pcSkills = SkillRegistry.api.getSkillHandler(Minecraft.getMinecraft().thePlayer);

		addElement(new ButtonCross(this));
		addElement(new ButtonTick(this));
		addElement(castType = new DarkcraftEnumSetButton<>(guiW - 80 - 20,80,80,16,CastType.class));
		addElement(componentList = new DarkcraftGuiList<DarkcraftGuiSpellComponent>(20,100, 200,280)
				{

					@Override
					public void clickableClicked(IClickable c, String id, int button)
					{
						if(c instanceof DarkcraftGuiSpellComponent)
						{
							DarkcraftGuiSpellComponent guiComp = (DarkcraftGuiSpellComponent) c;
							IComponent comp = guiComp.component;
							openSubGui(new ComponentCreationWidget(self, comp));
						}
						else
							super.clickableClicked(c, id, button);
					}
				});
		addElement(ciList = new DarkcraftGuiList<ComponentInstanceLabelClickable>(224,100, 300, 280)
				{
					@Override
					public void clickableClicked(IClickable c, String id, int button)
					{
						if(c instanceof ComponentInstanceLabelClickable)
						{
							this.removeElement((ComponentInstanceLabelClickable) c);
							refresh();
						}
						else
							super.clickableClicked(c, id, button);
					}
				});

		addElement(new DaedricLabel(20,24,"Name:"));
		addElement(name = new DarkcraftGuiTextfield(56,24,new ScalableInternal(200,13)){
			@Override
			public void keyTyped(char c, int i)
			{
				super.keyTyped(c, i);
				refresh();
			}
		});

		addElement(new DaedricLabel(52,64,"Cost:"));
		addElement(cost=new DarkcraftLabel(91,69,100,""));
		addElement(new DaedricLabel(20,84,"Base Cost:"));
		addElement(baseCost=new DarkcraftLabel(91,89,100,""));
		addElement(errors = new DarkcraftWrappingLabel(guiW-220,24,200,""));


		for(IComponent component : Helper.sortComponents(pc.getKnownComponents()))
			componentList.addElement(new DarkcraftGuiSpellComponent(0,0,componentList.w - 24,component));
		addElement(spellIcon = new SpellIcon(200,64,32,32,null));
		addElement(skillIcon = new SkillIcon(236,64,32,32,null));
		addElement(skillNameD = new DaedricLabel(270,64,100,""));
		addElement(skillNameR = new DarkcraftLabel(270,84,100,""));

		refresh();
	}

	public void addComponent(ComponentInstance ci)
	{
		ciList.addElement(new ComponentInstanceLabelClickable(0,0,ciList.iW,ci));
		refresh();
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if(id.equals("change"))
			refresh();
		else if(id.equals("tick"))
		{
			if(spellSoFar.isValid())
			{
				NBTTagCompound nbt = new NBTTagCompound();
				SimpleCoordStore pos = te.coords();
				pos.writeToNBT(nbt);
				spellSoFar.writeToNBT(nbt);
				nbt.setString("plname", PlayerHelper.getUsername(Minecraft.getMinecraft().thePlayer));
				DataPacket dp = new DataPacket(nbt,SpellCreationPacketHandler.disc);
				DarkcoreMod.networkChannel.sendToServer(dp);
				close();
			}
		}
		else
			super.clickableClicked(c, id, button);
	}

	private void refresh()
	{
		List<ComponentInstance> cis = new ArrayList<>();
		for(ComponentInstanceLabel cil : ciList)
			cis.add(cil.compInst);
		String spellName = name.text;
		spellSoFar = new Spell(spellName, cis.toArray(new ComponentInstance[cis.size()]), castType.getValue());
		cost.text = String.format("%.1f",spellSoFar.getCost(pc));
		baseCost.text = String.format("%.1f", spellSoFar.getCost(null));
		String errorsText = "";
		if(spellName.length() < 3)
			errorsText += "Name must be >3 characters\n";
		if(cis.size() == 0)
			errorsText += "Must contain at least 1 component\n";
		if(errorsText == "")
		{
			errorsText = "No problems!";
			errors.colour = new Colour (0.2f,0.9f,0.1f);
		}
		else
			errors.colour = new Colour (1f, 0.1f, 0.1f);
		spellIcon.spell = spellSoFar;
		ISkill skill = spellSoFar.getMainSkill();
		String skillName = skill == null ? "" : StatCollector.translateToLocal(skill.getName());
		skillIcon.setSkill(skill);
		skillNameD.text = skillName;
		skillNameR.text = skillName;

		errors.setText(errorsText);
	}

	private static class ComponentInstanceLabelClickable extends ComponentInstanceLabel implements IClickable
	{

		public ComponentInstanceLabelClickable(int x, int y, int width, ComponentInstance ci)
		{
			super(x, y, width, ci);
		}

		@Override
		public boolean click(int button, int x, int y)
		{
			parent.clickableClicked(this, "compinst", button);
			return true;
		}

	}
}

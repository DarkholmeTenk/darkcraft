package io.darkcraft.mod.common.magic.systems.component;

/**
 * Allows for an option which can be changed without remaking the spell. Can not affect the spell cost.
 * @author dark
 *
 */
public interface IConfigurableComponent
{
	public int getMinConfig();

	public int getMaxConfig();

	public String getConfigDescription();
}

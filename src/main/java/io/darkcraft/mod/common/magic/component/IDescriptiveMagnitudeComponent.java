package io.darkcraft.mod.common.magic.component;

import java.util.List;

public interface IDescriptiveMagnitudeComponent extends IMagnitudeComponent
{
	public void getDescription(List<String> strings, int magnitude);
}

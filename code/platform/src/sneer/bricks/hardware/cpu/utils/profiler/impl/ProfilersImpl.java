package sneer.bricks.hardware.cpu.utils.profiler.impl;

import sneer.bricks.hardware.cpu.utils.profiler.Profiler;
import sneer.bricks.hardware.cpu.utils.profiler.Profilers;

public class ProfilersImpl implements Profilers {

	@Override
	public Profiler newProfiler(String name) {
		return new ProfilerImpl(name);
	}

}

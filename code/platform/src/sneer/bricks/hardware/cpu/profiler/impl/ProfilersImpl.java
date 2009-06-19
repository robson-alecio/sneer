package sneer.bricks.hardware.cpu.profiler.impl;

import sneer.bricks.hardware.cpu.profiler.Profiler;
import sneer.bricks.hardware.cpu.profiler.Profilers;

public class ProfilersImpl implements Profilers {

	@Override
	public Profiler newProfiler(String name) {
		return new ProfilerImpl(name);
	}

}

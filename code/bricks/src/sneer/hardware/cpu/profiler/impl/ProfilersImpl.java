package sneer.hardware.cpu.profiler.impl;

import sneer.hardware.cpu.profiler.Profiler;
import sneer.hardware.cpu.profiler.Profilers;

public class ProfilersImpl implements Profilers {

	@Override
	public Profiler newProfiler(String name) {
		return new ProfilerImpl(name);
	}

}

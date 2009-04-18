package sneer.hardware.cpu.profiler;

import sneer.brickness.Brick;

@Brick
public interface Profilers {

	Profiler newProfiler(String name);

}

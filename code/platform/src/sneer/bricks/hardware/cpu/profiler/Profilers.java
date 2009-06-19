package sneer.bricks.hardware.cpu.profiler;

import sneer.foundation.brickness.Brick;

@Brick
public interface Profilers {

	Profiler newProfiler(String name);

}

package sneer.bricks.hardware.cpu.utils.profiler;

import sneer.foundation.brickness.Brick;

@Brick
public interface Profilers {

	Profiler newProfiler(String name);

}

package spikes.bamboo.bricksorter;

import java.io.IOException;
import java.util.List;

import sneer.brickness.Brick;

@Brick
public interface BrickSorter {

	List<Class<?>> sort(Class<?>[] bricks) throws IOException;

}

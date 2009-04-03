package spikes.bamboo.bricksorter;

import java.io.IOException;
import java.util.List;

public interface BrickSorter {

	List<Class<?>> sort(Class<?>[] bricks) throws IOException;

}

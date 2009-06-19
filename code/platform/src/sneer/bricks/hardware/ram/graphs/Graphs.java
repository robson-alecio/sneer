package sneer.bricks.hardware.ram.graphs;

import sneer.foundation.brickness.Brick;

@Brick
public interface Graphs {

	<T> DirectedGraph<T> createDirectedGraph();

}

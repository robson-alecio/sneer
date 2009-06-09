package sneer.hardware.ram.graphs;

import sneer.brickness.Brick;

@Brick
public interface Graphs {

	<T> DirectedGraph<T> createDirectedGraph();

}

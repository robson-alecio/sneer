package sneer.roots.collections.graphs;

import sneer.brickness.Brick;

@Brick
public interface Graphs {

	<T> DirectedGraph<T> createDirectedGraph();

}

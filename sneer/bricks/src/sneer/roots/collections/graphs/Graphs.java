package sneer.roots.collections.graphs;

import sneer.container.Brick;

@Brick
public interface Graphs {

	<T> DirectedGraph<T> createDirectedGraph();

}

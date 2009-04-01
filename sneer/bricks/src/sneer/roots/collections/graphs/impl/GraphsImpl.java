package sneer.roots.collections.graphs.impl;

import sneer.roots.collections.graphs.DirectedGraph;
import sneer.roots.collections.graphs.Graphs;

class GraphsImpl implements Graphs {

	@Override
	public <T> DirectedGraph<T> createDirectedGraph() {
		return new DirectedGraphImpl<T>();
	}

}

package sneer.hardware.ram.graphs.impl;

import sneer.hardware.ram.graphs.DirectedGraph;
import sneer.hardware.ram.graphs.Graphs;

class GraphsImpl implements Graphs {

	@Override
	public <T> DirectedGraph<T> createDirectedGraph() {
		return new DirectedGraphImpl<T>();
	}

}

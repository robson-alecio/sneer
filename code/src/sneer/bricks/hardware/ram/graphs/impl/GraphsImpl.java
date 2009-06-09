package sneer.bricks.hardware.ram.graphs.impl;

import sneer.bricks.hardware.ram.graphs.DirectedGraph;
import sneer.bricks.hardware.ram.graphs.Graphs;

class GraphsImpl implements Graphs {

	@Override
	public <T> DirectedGraph<T> createDirectedGraph() {
		return new DirectedGraphImpl<T>();
	}

}

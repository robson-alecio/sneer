package sneer.roots.collections.graphs.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.roots.collections.graphs.DirectedGraph;

class DirectedGraphImpl<T> implements DirectedGraph<T> {

	private final List<T> _vertices = new ArrayList<T>();

	@Override
	public void addEdge(T from, T to) {
		addVertex(from);
		addVertex(to);
	}

	private void addVertex(T vertex) {
		if (_vertices.contains(vertex)) return;
		_vertices.add(vertex);
	}

	@Override
	public Iterable<T> anyCycle() {
		return null;
	}

	@Override
	public Iterable<T> sortedVertices() {
		return _vertices;
	}

}

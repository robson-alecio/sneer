package sneer.bricks.hardware.ram.graphs;

import java.util.Collection;

public interface DirectedGraph<T> {

	/** Adds a vertex to this graph. */
	void addVertex(T vertex);

	/** Adds vertices if not already present and creates a directed edge from vertex to its successorVertex. */
	void addEdge(T vertex, T successorVertex);

	/** Returns an empty collection if there are no cycles in this graph. If there are cycles, one of them is arbitrarily chosen and all vertices in that cycle are returned in a collection. */
	Collection<T> detectCycle();

	/** Removes from this graph and returns a "leaf" vertex, i.e. a vertex that has no successors. Returns null if there are no more vertices in this graph.
	 * Precondition: detectCycle().isEmpty() */ 
	T pluck();

}

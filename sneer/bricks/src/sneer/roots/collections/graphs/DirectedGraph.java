package sneer.roots.collections.graphs;

public interface DirectedGraph<T> {

	void addEdge(T fromVertex, T toVertex);

	/** Returns an arbitrarily chosen cycle found in this directed graph, null if there are no cycles.*/
	Iterable<T> anyCycle();

	/** Returns vertices sorted such that all predecessors come before their (direct or indirect) successors.
	 *  throws IllegalStateException if this graph has cycles. */
	Iterable<T> sortedVertices();

}

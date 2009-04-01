package sneer.roots.collections.graphs.tests;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.roots.collections.graphs.DirectedGraph;
import sneer.roots.collections.graphs.Graphs;
import static sneer.commons.environments.Environments.my;

public class GraphsTest extends BrickTest {

	@Test
	public void testSorting() {
		DirectedGraph<String> _subject = my(Graphs.class).createDirectedGraph();
		
		_subject.addEdge("A", "B");
		_subject.addEdge("B", "C");
		_subject.addEdge("A", "D");
		
		assertNull(_subject.anyCycle());
		assertSameContents(_subject.sortedVertices(), "A", "B", "C", "D");
	}
	
}

package sneer.bricks.hardware.ram.graphs.tests;

import static sneer.foundation.commons.environments.Environments.my;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import sneer.bricks.hardware.ram.graphs.DirectedGraph;
import sneer.bricks.hardware.ram.graphs.Graphs;
import sneer.foundation.brickness.testsupport.BrickTest;

public class GraphsTest extends BrickTest {

	DirectedGraph<String> _subject = my(Graphs.class).createDirectedGraph();

	@Test
	public void noVertices() {
		assertTrue(_subject.detectCycle().isEmpty());
		assertNull(_subject.pluck());
	}

	@Test
	public void twoEdges() {
		_subject.addEdge("B", "C");  // B -> C
		_subject.addEdge("A", "B");  // A -> B -> C
		assertTrue(_subject.detectCycle().isEmpty());
		assertEquals("C", _subject.pluck());
		assertEquals("B", _subject.pluck());
		assertEquals("A", _subject.pluck());
	}

	@Test
	public void manyEdges() {
		_subject.addEdge("A", "B");
		_subject.addEdge("C", "D");
		_subject.addEdge("B", "C");
		_subject.addEdge("A", "E");
		_subject.addEdge("B", "F");
		_subject.addEdge("C", "F");
		_subject.addVertex("G");
		
		assertTrue(_subject.detectCycle().isEmpty());
		
		assertPluckedInAnyOrder("D", "E", "F", "G"); //They are all leaf vertices.
		assertEquals("C", _subject.pluck());
		assertEquals("B", _subject.pluck());
		assertEquals("A", _subject.pluck());
	}

	@Test
	public void cycleDetection() {
		_subject.addEdge("A", "B"); 
		_subject.addEdge("C", "D"); 
		_subject.addEdge("B", "C");
		_subject.addEdge("A", "E");
		_subject.addEdge("D", "B"); //Forms the B, C, D cycle.
		_subject.addEdge("B", "F");
		_subject.addEdge("C", "F");
		_subject.addEdge("A2", "A");
		_subject.addEdge("A3", "A");
		
		List<String> cycle = new ArrayList<String>(_subject.detectCycle());
		Collections.sort(cycle);
		assertSameContents(cycle, "B", "C", "D");
	}

	private void assertPluckedInAnyOrder(String... vertices) {
		for (int i = 0; i < vertices.length; i++)
			assertTrue(Arrays.asList(vertices).contains(_subject.pluck()));
	}
		

	@Test(expected=IllegalStateException.class)
	public void pluckWithCycles() {
		_subject.addEdge("A", "B");
		_subject.addEdge("B", "A");
		
		_subject.pluck();
	}
	
}

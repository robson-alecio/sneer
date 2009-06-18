package spikes.bamboo.bricksorter.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.ram.graphs.DirectedGraph;
import sneer.bricks.hardware.ram.graphs.Graphs;
import sneer.foundation.brickness.BrickConventions;
import spikes.bamboo.bricksorter.BrickSorter;
import spikes.sneer.kernel.container.bytecode.dependencies.DependencyFinder;


class BrickSorterImpl implements BrickSorter {

	@Override
	public List<Class<?>> sort(Class<?>[] bricks) throws IOException {
		final DirectedGraph<Class<?>> graph = dependencyGraphFor(bricks);
		final Collection<Class<?>> cycles = graph.detectCycle();
		if (!cycles.isEmpty()) {
			throw new IllegalStateException("Cycle: " + my(Lang.class).strings().join(cycles, " <-> "));
		}
		
		final ArrayList<Class<?>> result = new ArrayList<Class<?>>(bricks.length);
		while (true) {
			final Class<?> brick = graph.pluck();
			if (brick == null)
				break;
			result.add(brick);
		}
		return result;
	}

	private DirectedGraph<Class<?>> dependencyGraphFor(Class<?>[] bricks)
			throws IOException {
		final DirectedGraph<Class<?>> graph = my(Graphs.class).createDirectedGraph();
		for (Class<?> brick : bricks) {
			graph.addVertex(brick);
			for (String dependency : dependenciesFor(brick)) {
				System.out.println(brick.getSimpleName() + " -> " + dependency);
				graph.addEdge(brick, loadClass(dependency));
			}
		}
		return graph;
	}

	private List<String> dependenciesFor(Class<?> brick) throws IOException {
		return my(DependencyFinder.class).findDependencies(implClassFor(brick));
	}

	private Class<?> loadClass(String dependency) {
		try {
			return Class.forName(dependency);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	private InputStream implClassFor(Class<?> brick) throws IOException {
		final Class<?> klass = loadClass(BrickConventions.implClassNameFor(brick.getName()));
		final String fileName = klass.getCanonicalName().replace('.', '/') + ".class";
		final URL url = klass.getResource("/" + fileName);
		return url.openStream();
	}

}

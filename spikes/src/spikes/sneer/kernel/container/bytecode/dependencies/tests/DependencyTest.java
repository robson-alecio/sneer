package spikes.sneer.kernel.container.bytecode.dependencies.tests;

import static sneer.foundation.commons.environments.Environments.my;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import sneer.foundation.brickness.testsupport.AssertUtils;
import sneer.foundation.brickness.testsupport.BrickTest;
import spikes.sneer.kernel.container.bytecode.dependencies.DependencyFinder;

public class DependencyTest extends BrickTest {
	
	@Test
	public void testFindDependencies() throws IOException {
		
		List<String> dependencies = my(DependencyFinder.class).findDependencies(classInputStreamFor(Foo.class));
		AssertUtils.assertSameContents(dependencies, "java.util.Map", "java.util.Set");
		
	}

	private InputStream classInputStreamFor(Class<?> klass) throws IOException {
		final String classFile = klass.getCanonicalName().replace('.', '/') + ".class";
		final URL url = klass.getResource("/" + classFile);
		return url.openStream();
	}
}

class Foo {
	void bar() {
		my(Map.class);
		my(Set.class);
	}
}

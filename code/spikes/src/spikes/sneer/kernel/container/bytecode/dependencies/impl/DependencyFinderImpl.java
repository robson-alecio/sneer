package spikes.sneer.kernel.container.bytecode.dependencies.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.objectweb.asm.ClassReader;

import spikes.sneer.kernel.container.bytecode.dependencies.DependencyFinder;

public class DependencyFinderImpl implements DependencyFinder {

	@Override
	public List<String> findDependencies(InputStream classInputStream) throws IOException {
		ClassReader classReader = new ClassReader(classInputStream);
		DependencyVisitor extractor = new DependencyVisitor();
		classReader.accept(extractor, 0);
		return extractor.dependencies();
	}

}

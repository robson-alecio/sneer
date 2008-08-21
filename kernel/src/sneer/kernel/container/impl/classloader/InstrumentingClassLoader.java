package sneer.kernel.container.impl.classloader;

import java.io.IOException;
import java.security.SecureClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import sneer.kernel.container.impl.classloader.enhancer.Enhancer;

@Deprecated
public class InstrumentingClassLoader extends SecureClassLoader {

	private final Enhancer _enhancer;

	public InstrumentingClassLoader(Enhancer enhancer) {
		super(null);
		_enhancer = enhancer;
	}	
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		
		try {
			final ClassReader reader = new ClassReader(name);
			final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			reader.accept(_enhancer.enhance(writer), 0);
			return defineClass(name, writer.toByteArray());
		} catch (IOException e) {
			throw new ClassNotFoundException(name, e);
		}
	}

	private Class<?> defineClass(String name, byte[] byteArray) {
		return defineClass(name, byteArray, 0, byteArray.length);
	}
}

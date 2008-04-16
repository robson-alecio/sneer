package sneer.lego.impl.classloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import sneer.lego.impl.classloader.enhancer.Enhancer;
import wheel.io.Jars;

public class BrickClassLoader extends URLClassLoader {

	private String _mainClass;

	private Enhancer _enhancer;

	public BrickClassLoader(Enhancer enhancer) {	
		super(new URL[]{}, Jars.bootstrapClassLoader());
		_enhancer = enhancer;
	}	

	public BrickClassLoader(URL[] urls) {
		super(urls, Jars.bootstrapClassLoader());
	}

	public BrickClassLoader(String mainClass, URL url) {
		this(new URL[]{url});
		_mainClass = mainClass;
	}

	public String getMainClass() {
		return _mainClass;
	}
	

//	@Override
//	public Class<?> loadClass(String name) throws ClassNotFoundException {
//		if(name.startsWith("sneer.lego")) {
//			return findClass(name); //hack for eclipse development
//		} 
//		return super.loadClass(name);
//	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			ClassReader reader = new ClassReader(name);
			ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			reader.accept(_enhancer.enhance(writer), 0);
			Class<?> result = defineClass(name, writer.toByteArray()); 
			if(!result.isInterface()) {
				System.out.println(name + " enhanced");
				return result;
			}
			System.out.println(name + " not found");
			return null; //super.loadClass(name); //hack for eclipse development
		} catch (IOException e) {
			throw new ClassNotFoundException(name, e);
		}
	}

	private Class<?> defineClass(String name, byte[] byteArray) {
		return defineClass(name, byteArray, 0, byteArray.length);
	}

}

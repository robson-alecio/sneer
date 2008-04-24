package sneer.lego.impl.classloader;

import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import sneer.lego.impl.classloader.enhancer.Enhancer;
import sneer.lego.impl.classloader.enhancer.MakeSerializable;
import sneer.lego.utils.asm.IMetaClass;

public class FileClassLoader extends SecureClassLoader {

	private List<IMetaClass> _metaClasses;
	
	private String _name;
	
	private Enhancer _enhancer;
	
	private Map<String, IMetaClass> _hash;

	public FileClassLoader(String name, List<IMetaClass> files, ClassLoader parent) {
		super(parent);
		_name = name;
		_metaClasses = files;
		_hash = computeHash(_metaClasses);
		_enhancer = new MakeSerializable();
	}
	
	private Map<String, IMetaClass> computeHash(List<IMetaClass> metaClasses)
    {
	    Map<String, IMetaClass> result = new HashMap<String, IMetaClass>();
	    for(IMetaClass meta : metaClasses) {
	        String futureName = meta.futureClassName();
	        result.put(futureName, meta);
	    }
	    return result;
    }

    @Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		// First, check if the class has already been loaded
		ClassLoader parent = getParent();
		Class<?> c = findLoadedClass(name);
		if (c == null) {
		    try {
		    	c = findClass(name);
		    } catch (ClassNotFoundException e) {
		    	if (parent != null) {
		    		c = parent.loadClass(name);
		    	} else {
		    		//c = findBootstrapClass0(name);
		    	}
		    }
		}
		if (resolve) {
		    resolveClass(c);
		}
		return c;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
	    IMetaClass meta = _hash.get(name);
	    if(meta == null)
	        throw new ClassNotFoundException("Class not found "+name);
	    
	    try {
            return defineClass(name, meta.bytes());
        } catch (IOException e) {
            throw new ClassNotFoundException("Error reading bytes from "+meta.classFile().getName());
        }
	}

	private Class<?> defineClass(String name, byte[] byteArray) {
		byteArray = enhance(byteArray);
		return defineClass(name, byteArray, 0, byteArray.length);
	}

	private byte[] enhance(byte[] byteArray) {
	    ClassReader reader = new ClassReader(byteArray);
		ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		reader.accept(_enhancer.enhance(writer), 0);
		return writer.toByteArray();
	}

	@Override
	public String toString() {
		return _name +" ("+this.getClass().getName()+")";
	}

	public void debug() {
		System.out.println(" ** "+_name+" ** ");
		for(IMetaClass metaClass : _metaClasses) {
			System.out.println(" "+metaClass.getName());
		}
	}
}

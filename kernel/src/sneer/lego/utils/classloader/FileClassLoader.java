package sneer.lego.utils.classloader;

import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.List;

import sneer.lego.utils.asm.MetaClass;

public class FileClassLoader extends SecureClassLoader {

	private List<MetaClass> _metaClasses;
	
	private String _name;

	public FileClassLoader(String name, List<MetaClass> files, ClassLoader parent) {
		super(parent);
		_name = name;
		_metaClasses = files;
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
		//System.out.println("[ECLIPSE:"+_name+"] finding class: "+name);
		for(MetaClass metaClass : _metaClasses) {
			try {
				//System.out.println(("  "+metaClass.getName()));
				if(metaClass.getName().equals(name)) {
					//System.out.println(_name + " loading class: "+name);
					return defineClass(name, metaClass.bytes());
				}
			} catch (IOException ignored) {
				//not what we want
				ignored.printStackTrace();
			}
		}
		throw new ClassNotFoundException("Class not found "+name);
	}

	private Class<?> defineClass(String name, byte[] byteArray) {
		byteArray = enhance(byteArray);
		return defineClass(name, byteArray, 0, byteArray.length);
	}

	private byte[] enhance(byte[] byteArray) {
		return byteArray;
	}

	@Override
	public String toString() {
		return _name +" ("+this.getClass().getName()+")";
	}

	public void debug() {
		System.out.println(" ** "+_name+" ** ");
		for(MetaClass metaClass : _metaClasses) {
			System.out.println(" "+metaClass.getName());
		}
	}
}

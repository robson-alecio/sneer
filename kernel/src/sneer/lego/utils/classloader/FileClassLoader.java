package sneer.lego.utils.classloader;

import java.io.File;
import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.List;

import sneer.lego.utils.asm.ClassUtils;
import sneer.lego.utils.asm.MetaClass;

public class FileClassLoader extends SecureClassLoader {

	private List<File> _files;
	
	private String _name;

	public FileClassLoader(String name, List<File> files, ClassLoader parent) {
		super(parent);
		_name = name;
		_files = files;
//		System.out.println(" ** "+name+" ** ");
//		for(File file : _files) {
//			System.out.println(" "+file);
//		}
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		//System.out.println("[ECLIPSE:"+_name+"] finding class: "+name);
		for(File file : _files) {
			try {
				MetaClass metaClass = ClassUtils.metaClass(file);
				//System.out.println(("  "+metaClass.getName()));
				if(metaClass.getName().equals(name))
					return defineClass(name, metaClass.bytes());
			} catch (IOException ignored) {
				//not what we want
				ignored.printStackTrace();
			}
		}
		throw new ClassNotFoundException("Class not found "+name);
	}

	private Class<?> defineClass(String name, byte[] byteArray) {
		return defineClass(name, byteArray, 0, byteArray.length);
	}

	

	@Override
	public String toString() {
		return _name +" ("+this.getClass().getName()+")";
	}

	void debug() {
		for(File file : _files) {
			System.out.println(file);
		}
	}
}

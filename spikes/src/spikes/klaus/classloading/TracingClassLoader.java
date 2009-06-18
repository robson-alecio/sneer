package spikes.klaus.classloading;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

import org.apache.commons.io.FileUtils;

import sneer.foundation.lang.exceptions.NotImplementedYet;

public class TracingClassLoader extends ClassLoader {

	private final File _classRoot;
	
	private int _indentLevel = 0;
	

	TracingClassLoader(File classRoot) {
		super(null);
		_classRoot = classRoot;
	}

	@Override
	public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		try {
			entering("loadClass", name, resolve);
			return doLoadClass(name, resolve);
		} finally {
			exiting("loadClass", name, resolve);
		}
	}

	private Class<?> doLoadClass(String name, boolean resolve) throws ClassNotFoundException {
	    Class<?> result = findLoadedClass(name);
	    if (result != null) return result;

	    String filename = name.replace('.', '/')+".class";
	    File file = new File(_classRoot, filename);

	    result = file.exists()
	    	? define(name, file)
	    	: findSystemClass(name);
	    
	    if (result == null)
	    	throw new ClassNotFoundException(name);

	     if (resolve) resolveClass(result);

	     return result;
	}

	
	private Class<?> define(String name, File classFile) {
		try {
			entering("define", name);
			return doDefine(name, classFile);
		} finally {
			exiting("define", name);
		}
	}

	private Class<?> doDefine(String name, File classFile) throws ClassFormatError {
		byte bytes[] = null;
	    try {
	    	bytes = FileUtils.readFileToByteArray(classFile);
	    } catch(IOException ie) {
	    	throw new NotImplementedYet();
	    }

	    return defineClass(name, bytes, 0, bytes.length);
	}

	
	
	
	private void entering(String method, Object... args) {
		System.out.println(indent() + "Entering " + method + " args: " + Arrays.toString(args));
		_indentLevel++;
	}
	
	private void exiting(String method, Object... args) {
		_indentLevel--;
		System.out.println(indent() + "Exiting " + method + " args: " + Arrays.toString(args));
	}

	
	
	private String indent() {
		String result = "";
		for (int i = 0; i < _indentLevel; i++)
			result += "  ";
		
		return result;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			entering("findClass", name);
			return super.findClass(name);
		} finally {
			exiting("findClass");
		}
		
	}

	@Override
	public URL findResource(String name) {
		try {
			entering("findResource", name);
			return super.findResource(name);
		} finally {
			exiting("findResource");
		}
		
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		try {
			entering("findResources",name);
			return super.findResources(name);
		} finally {
			exiting("findResources");
		}
		
	}

	@Override
	public synchronized void clearAssertionStatus() {
		try {
			entering("clearAssertionStatus");
			super.clearAssertionStatus();
		} finally {
			exiting("clearAssertionStatus");
		}
		
	}

	@Override
	protected Package definePackage(String name, String specTitle,
			String specVersion, String specVendor, String implTitle,
			String implVersion, String implVendor, URL sealBase)
			throws IllegalArgumentException {
		try {
			entering("definePackage",name,  specTitle,
					 specVersion,  specVendor,  implTitle,
					 implVersion,  implVendor,  sealBase);
			return super.definePackage(name,  specTitle,
					 specVersion,  specVendor,  implTitle,
					 implVersion,  implVendor,  sealBase);
		} finally {
			exiting("definePackage");
		}
		
	}

	@Override
	protected String findLibrary(String libname) {
		try {
			entering("findLibrary",libname);
			return super.findLibrary(libname);
		} finally {
			exiting("findLibrary");
		}
		
	}

	@Override
	protected Package getPackage(String libname) {
		try {
			entering("getPackage",libname);
			return super.getPackage(libname);
		} finally {
			exiting("getPackage");
		}
		
	}

	@Override
	protected Package[] getPackages() {
		try {
			entering("getPackages");
			return super.getPackages();
		} finally {
			exiting("getPackages");
		}
		
	}

	@Override
	public URL getResource(String name) {
		try {
			entering("getResource",name);
			return super.getResource(name);
		} finally {
			exiting("getResource");
		}
		
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		try {
			entering("getResourceAsStream",name);
			return super.getResourceAsStream(name);
		} finally {
			exiting("getResourceAsStream");
		}
		
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		try {
			entering("getResources",name);
			return super.getResources(name);
		} finally {
			exiting("getResources");
		}
		
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			entering("loadClass",name);
			return super.loadClass(name);
		} finally {
			exiting("loadClass");
		}
	}


	@Override
	public synchronized void setClassAssertionStatus(String className,
			boolean enabled) {
		try {
			entering("setClassAssertionStatus",className,
					enabled);
			super.setClassAssertionStatus(className,
					enabled);
		} finally {
			exiting("setClassAssertionStatus");
		}
		
	}

	@Override
	public synchronized void setDefaultAssertionStatus(boolean enabled) {
		try {
			entering("setDefaultAssertionStatus",enabled);
			super.setDefaultAssertionStatus(enabled);
		} finally {
			exiting("setDefaultAssertionStatus");
		}
		
	}

	@Override
	public synchronized void setPackageAssertionStatus(String packageName,
			boolean enabled) {
		try {
			entering("setPackageAssertionStatus",packageName,
					enabled);
			super.setPackageAssertionStatus(packageName,
					enabled);
		} finally {
			exiting("setPackageAssertionStatus");
		}
		
	}
	
}

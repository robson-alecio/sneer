package spikes.klaus.classloading;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.Manifest;

public class MyClassLoader extends URLClassLoader {

	MyClassLoader() {
		super(new URL[0], null);
	}

	private void entering(String method, Object... args) {
		System.out.println("Entering " + method + " args: " + Arrays.toString(args));
	}
	
	private void exiting(String method) {
		System.out.println("Exiting " + method);
	}

	@Override
	protected void addURL(URL url) {
		try {
			entering("addURL",url);
			super.addURL(url);
		} finally {
			exiting("addURL");
		}
		
	}

	@Override
	protected Package definePackage(String name, Manifest man, URL url)
			throws IllegalArgumentException {
		try {
			entering("definePackage",name, man, url);
			return super.definePackage(name, man, url);
		} finally {
			exiting("definePackage");
		}
		
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
	protected PermissionCollection getPermissions(CodeSource codesource) {
		try {
			entering("getPermissions",codesource);
			return super.getPermissions(codesource);
		} finally {
			exiting("getPermissions");
		}
		
	}

	@Override
	public URL[] getURLs() {
		try {
			entering("getURLs");
			return super.getURLs();
		} finally {
			exiting("getURLs");
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
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		try {
			entering("loadClass",name, resolve);
			return super.loadClass(name, resolve);
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

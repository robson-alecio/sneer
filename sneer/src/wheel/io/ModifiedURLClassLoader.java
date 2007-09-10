package wheel.io;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;


public class ModifiedURLClassLoader extends URLClassLoader {

	public ModifiedURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	public void addURL(URL url) { //exposes the method avoiding all reflective stuff to invoke.	
		super.addURL(url);
	}

	public void addPath(String path) {
		try {
			super.addURL(new File(path).toURI().toURL());
		} catch (Exception e) {
			Log.log(e);
			e.printStackTrace();
		}
	}

}

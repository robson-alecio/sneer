package spikes.klaus.classloading;

import java.net.URL;
import java.net.URLClassLoader;

public class MyClassLoader extends URLClassLoader {

	private String _mainClass;
	
	public MyClassLoader(URL[] urls) {
		super(urls);
	}

	public MyClassLoader(String mainClass, URL url) {
		this(new URL[]{url});
		_mainClass = mainClass;
	}
	
	public String getMainClass() {
		return _mainClass;
	}
}

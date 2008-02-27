package sneer.lego;

import java.net.URL;
import java.net.URLClassLoader;

public class BrickClassLoader extends URLClassLoader {

	private String _mainClass;
	
	public BrickClassLoader(URL[] urls) {
		super(urls);
	}

	public BrickClassLoader(String mainClass, URL url) {
		this(new URL[]{url});
		_mainClass = mainClass;
	}
	
	public String getMainClass() {
		return _mainClass;
	}
}

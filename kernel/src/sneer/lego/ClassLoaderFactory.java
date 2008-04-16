package sneer.lego;

import java.net.URL;

public interface ClassLoaderFactory {

	ClassLoader sneerApi();

	ClassLoader brickClassLoader(String impl, URL url);
}

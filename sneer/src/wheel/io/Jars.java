package wheel.io;

import java.net.MalformedURLException;
import java.net.URL;



public class Jars {

	public static void runAllowingForClassGC(URL jar, String classToInstantiate) throws Exception {
		ModifiedURLClassLoader loader = createGarbageCollectableClassLoader(jar);
		loader.loadClass(classToInstantiate).newInstance();
	}
	
	
	public static ModifiedURLClassLoader createGarbageCollectableClassLoader(URL jar) throws Exception {
		return new ModifiedURLClassLoader(new URL[]{jar}, bootstrapClassLoader());
	}
	
	
	private static ClassLoader bootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}


	public static URL jarGiven(Class<?> clazz) {
		URL url = clazz.getResource(clazz.getSimpleName() + ".class");
		String fullPath = url.getPath();
		String path = fullPath.substring(0, fullPath.indexOf("!"));
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e); 
		}
	}
	

}

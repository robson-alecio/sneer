package spikes.sneer.kernel.container.utils;

public class ObjectUtils
{
	
	private static ClassLoader currentClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static Class<?> loadClass(String className) { 	
		return loadClass(className, currentClassLoader());
	}
	
	public static Class<?> loadClass(String className, ClassLoader cl) {
		try {
			return cl.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
    public static Object getInstance(String className) {
    	return getInstance(className, currentClassLoader());
	}

    public static Object getInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error creating object of class ["+clazz.getName()+"]",e);
        }
    }
    
    public static Object getInstance(String className, ClassLoader cl) {
    	try {
    		Class<?> clazz = cl.loadClass(className);
    		Object obj = clazz.newInstance();
    		return obj;
    	} catch (Exception e) {
    		throw new RuntimeException("Error creating object of class ["+className+"]",e);
    	}
    }
}

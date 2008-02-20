package spikes.lego.utils;

public class ObjectUtils
{
	
	public static ClassLoader currentClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static Class<?> loadClass(String className) { 	
		return loadClass(className, currentClassLoader());
	}
	
	public static Class<?> loadClass(String className, ClassLoader cl) {
		Class<?> clazz;
        try {
	        clazz = cl.loadClass(className);
	        return clazz;
        } catch (ClassNotFoundException e) {
        	throw new RuntimeException("Error loading class ["+className+"]",e);
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

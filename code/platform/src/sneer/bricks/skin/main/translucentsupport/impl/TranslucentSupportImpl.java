package sneer.bricks.skin.main.translucentsupport.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.GraphicsConfiguration;
import java.awt.Shape;
import java.awt.Window;
import java.lang.reflect.Method;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.skin.main.translucentsupport.TranslucentSupport;

public class TranslucentSupportImpl implements TranslucentSupport {
	
    public Object _perpixelTransparent; 
    public Object _perpixelTranslucent;
    public Object _translucent;
    
    TranslucentSupportImpl(){
    	Class<?> awtUtilitiesClass = utilitiesClass();
    	if(awtUtilitiesClass==null) return;
    	
    	Class<?> translucencyClass = translucencyClass();
    	if(translucencyClass==null) return;
    	
    	if (translucencyClass.isEnum()) {
    		Object[] constants = translucencyClass.getEnumConstants();
    		if (constants != null) {
    			_perpixelTransparent = constants[0];
    			_translucent = constants[1];
    			_perpixelTranslucent = constants[2];
    		}
    	}
    }
    
    @Override public Object perpixelTransparent() { return _perpixelTransparent; }
    @Override public Object perpixelTranslucent() { return _perpixelTranslucent; }
    @Override public Object translucent() { return _translucent; }

    @Override public boolean isTranslucencySupported(Object kind) {
		return isSupported(kind, "isTranslucencySupported", translucencyClass());
	}

    @Override public boolean isTranslucencyCapable(GraphicsConfiguration gc) {
		return isSupported(gc, "isTranslucencyCapable", GraphicsConfiguration.class);
	}

    @Override public void setWindowShape(Window window, Shape shape) {
		set(window, shape, "setWindowShape", Window.class, Shape.class);
	}

    @Override public void setWindowOpacity(Window window, float opacity) {
		set(window, opacity, "setWindowOpacity", Window.class, float.class);
	}

    @Override public void setWindowOpaque(Window window, boolean opaque) {
		set(window, opaque, "setWindowOpaque", Window.class, boolean.class);
	}
    
    private Class<?> utilitiesClass() {return classForName("com.sun.awt.AWTUtilities");}    
    private Class<?> translucencyClass() {return classForName("com.sun.awt.AWTUtilities$Translucency");	}

    private boolean isSupported(Object kind, String methodName, Class<?>...parametersClass) {
		try {
			Method method = utilitiesClass().getMethod(methodName, parametersClass);
            return (Boolean) method.invoke(null, kind);
		} catch (Throwable ignore) {
			return false;
		}
	}
    
    private void set(Window window, Object value, String methodName, Class<?>...types) {
    	Method method;
		try {
			method = utilitiesClass().getMethod(methodName, types);
            method.invoke(null, window, value);
        } catch (Exception ex) {
        	my(Logger.class).log(ex, "Error when trying invoke method '{}'.", methodName);
        } 
    }

    private Class<?> classForName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
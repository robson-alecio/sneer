package sneer.bricks.skin.main.translucentsupport.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.GraphicsConfiguration;
import java.awt.Shape;
import java.awt.Window;
import java.lang.reflect.Method;

import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.skin.main.translucentsupport.TranslucentSupport;

public class TranslucentSupportImpl implements TranslucentSupport {
	
	private Object _perpixelTransparent; 
    private Object _perpixelTranslucent;
    private Object _translucent;
	private Light _light;
	private boolean _isDisabled = false;
    
    TranslucentSupportImpl(){
    	_light = my(BlinkingLights.class).prepare(LightType.WARN);

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
    	if(_isDisabled) return;
    	
    	Method method;
		try {
			method = utilitiesClass().getMethod(methodName, types);
            method.invoke(null, window, value);
        } catch (Throwable throwable) {
        	my(BlinkingLights.class).turnOnIfNecessary(_light, "Translucent Windows Disabled", 
        		   "Translucent windows are supported only on JavaSE release 6u10 or superior." +
        		   " Check your JVM and your video aceleration.", throwable, 30000);
        	_isDisabled = true;
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
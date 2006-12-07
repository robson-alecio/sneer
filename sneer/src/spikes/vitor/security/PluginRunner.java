package spikes.vitor.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginRunner extends Thread {
	Class<?> _clazz;
	ThreadGroup group;
	Method method;
	
	public PluginRunner (ThreadGroup pluginGroup, Class<?> clazz) {
		super (pluginGroup, clazz.getName());
		this._clazz = clazz;
		this.group = pluginGroup;
		
		try {
			method = _clazz.getMethod("run", new Class[] { });
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void invokeSneerMainMethod() {
		try {
			method.invoke(null, new Object[] { });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void run() {
		invokeSneerMainMethod();
	}
}

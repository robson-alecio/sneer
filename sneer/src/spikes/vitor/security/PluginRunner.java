package spikes.vitor.security;

import java.lang.reflect.InvocationTargetException;

public class PluginRunner extends Thread {
	Class<?> clazz;
	ThreadGroup group;
	
	public PluginRunner (ThreadGroup pluginGroup, Class clazz) {
		super (pluginGroup, clazz.getName());
		this.clazz = clazz;
		this.group = pluginGroup;
	}
	
	public void invokeSneerMainMethod() {
		try {
			clazz.getMethod("run", new Class[] { }).invoke(null, new Object[] { });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		invokeSneerMainMethod();
	}
}

package spikes.vitor.security;

import java.lang.reflect.InvocationTargetException;

public class PluginRunner extends Thread {
	Class<?> _clazz;
	ThreadGroup group;
	
	public PluginRunner (ThreadGroup pluginGroup, Class<?> clazz) {
		super (pluginGroup, clazz.getName());
		this._clazz = clazz;
		this.group = pluginGroup;
	}
	
	public void invokeSneerMainMethod() {
		try {
			_clazz.getMethod("run", new Class[] { }).invoke(null, new Object[] { });
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
	
	@Override
	public void run() {
		invokeSneerMainMethod();
	}
}

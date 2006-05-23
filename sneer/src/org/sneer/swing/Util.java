package org.sneer.swing;

import java.awt.Component;
import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.JComponent;

public abstract class Util {
	public static JComponent getGlassPane(Component src){
		try {
			Component c =javax.swing.SwingUtilities.getWindowAncestor(src);		
			Method m = c.getClass().getMethod("getGlassPane", new Class[0]);
			return (JComponent) m.invoke(c, new Object[0]);
		} catch (Exception ex) {
			return null;
		} 	
	}
	public static boolean setGlassPane(Component src, Component newGlassPane){
		try {
			Component c =javax.swing.SwingUtilities.getWindowAncestor(src);		
			Method m = c.getClass().getMethod("setGlassPane", new Class[]{Component.class});
			m.invoke(c, new Object[]{newGlassPane});
			return true;
		} catch (Exception ex) {
			return false;
		} 	
	}
	public static Window getWindow(Component src){
		try {
			Component c = src.getParent();					
			while(c.getParent()!=null) c=c.getParent();				
			return (Window) javax.swing.SwingUtilities.getWindowAncestor(src);	
		} catch (Exception ex) {
			return null;
		} 	
	}
}

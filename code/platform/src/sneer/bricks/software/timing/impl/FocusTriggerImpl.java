package sneer.bricks.software.timing.impl;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import org.jdesktop.animation.timing.triggers.FocusTriggerEvent;

import sneer.bricks.software.timing.Animator;
import sneer.bricks.software.timing.FocusTrigger;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class FocusTriggerImpl implements FocusTrigger{

	@Override public FocusListener onGain(Component component, Animator animator) { return onGain(component, animator, false); }
	@Override public FocusListener onGain(Component component, final Animator animator, final boolean autoReverse) {
       FocusAdapter listener = new FocusAdapter(){ @Override public void focusGained(FocusEvent e) {
        	new org.jdesktop.animation.timing.triggers.FocusTrigger(
        			delegate(animator), FocusTriggerEvent.IN, autoReverse).focusGained(e);
		}};
		component.addFocusListener(listener);
        return listener;
	}
	
	@Override public FocusListener onLost(Component component, Animator animator) { return onLost(component, animator, false); }
	@Override public FocusListener onLost(Component component, final Animator animator, final boolean autoReverse) {
		FocusAdapter listener = new FocusAdapter(){ @Override public void focusLost(FocusEvent e) {
        	new org.jdesktop.animation.timing.triggers.FocusTrigger(
        			delegate(animator), FocusTriggerEvent.OUT, autoReverse).focusLost(e);
		}};
		component.addFocusListener(listener);
        return listener;
	}
	
	@Override public WindowFocusListener onGain(Window window, Animator animator) { return onGain(window, animator, false); }
	@Override public WindowFocusListener onGain(final Window window, final Animator animator, final boolean autoReverse) {
		WindowFocusListener listener = new WindowFocusListener(){ 
			@Override public void windowGainedFocus(WindowEvent e) {
				FocusEvent event = new FocusEvent(window, FocusEvent.FOCUS_GAINED);
	        	new org.jdesktop.animation.timing.triggers.FocusTrigger(delegate(animator), FocusTriggerEvent.IN, autoReverse).focusGained(event);
	        }

		@Override public void windowLostFocus(WindowEvent e) {/* ignore*/}};
		window.addWindowFocusListener(listener);
        return listener;
	}
	
	@Override public WindowFocusListener onLost(Window window, Animator animator) { return onLost(window, animator, false); }
	@Override public WindowFocusListener onLost(final Window window, final Animator animator, final boolean autoReverse) {
		WindowFocusListener listener = new WindowFocusListener(){ 
			@Override public void windowLostFocus(WindowEvent e) {
				FocusEvent event = new FocusEvent(window, FocusEvent.FOCUS_LOST);
	        	new org.jdesktop.animation.timing.triggers.FocusTrigger(delegate(animator), FocusTriggerEvent.IN, autoReverse).focusGained(event);
	        }

		@Override public void windowGainedFocus(WindowEvent e) {/* ignore*/}};
		window.addWindowFocusListener(listener);
        return listener;
	}
	
	private org.jdesktop.animation.timing.Animator delegate(Animator animator) {
		if(!(animator instanceof AnimatorAdapter)) 
			throw new NotImplementedYet("Unsupported Animator Class Type: " + animator.getClass().getName());
		
		return ((AnimatorAdapter) animator)._delegate;
	}
}
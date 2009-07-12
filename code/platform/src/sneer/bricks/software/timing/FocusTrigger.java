package sneer.bricks.software.timing;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.awt.event.WindowFocusListener;

public interface FocusTrigger{
	FocusListener onGain(Component component,  Animator animator);
	FocusListener onGain(Component component,  Animator animator, boolean autoReverse);
	FocusListener onLost(Component component,  Animator animator);
	FocusListener onLost(Component component,  Animator animator, boolean autoReverse);
    
	WindowFocusListener onGain(Window window,  Animator animator);
	WindowFocusListener onGain(Window window,  Animator animator, boolean autoReverse);
	WindowFocusListener onLost(Window window,  Animator animator);
    WindowFocusListener onLost(Window window,  Animator animator, boolean autoReverse);
}
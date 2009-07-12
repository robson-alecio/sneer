package sneer.bricks.software.timing.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Window;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import sneer.bricks.skin.main.translucentsupport.TranslucentSupport;
import sneer.bricks.software.timing.WindowOpacitySetter;

public class WindowOpacitySetterImpl implements WindowOpacitySetter {
	
	private final TranslucentSupport _translucent = my(TranslucentSupport.class);
	
	@Override
	public <T> AnimatorAdapter animator(int duration, final Window window, final float start, final float end) {
		TimingTargetAdapter tta = new TimingTargetAdapter(){

			private final float _diff = end-start;
			
		    @Override public void begin() {  _translucent.setWindowOpacity(window, start);  }
		    @Override public void end() { _translucent.setWindowOpacity(window, end); }
		    @Override public void timingEvent(float fraction) { _translucent.setWindowOpacity(window, currentValue(fraction)); }
		    
		    private float currentValue(float fraction){
		    	return start + _diff*fraction;
		    }
		};
		return new AnimatorAdapter(new Animator(duration, tta));
	}
}

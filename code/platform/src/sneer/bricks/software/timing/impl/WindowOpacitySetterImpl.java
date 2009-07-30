package sneer.bricks.software.timing.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Window;

import org.jdesktop.animation.timing.TimingTargetAdapter;

import sneer.bricks.skin.main.translucentsupport.TranslucentSupport;
import sneer.bricks.software.timing.WindowOpacitySetter;

class WindowOpacitySetterImpl implements WindowOpacitySetter {
	
	private final TranslucentSupport _translucent = my(TranslucentSupport.class);
	
	@Override 
	public <T> sneer.bricks.software.timing.Animator animator(Window window, float start, float end, int duration) {
		return animator(window, start, end, duration, duration);
	}
	
	@Override
	public <T> sneer.bricks.software.timing.Animator animator(final Window window, final float start, final float end, int forwardDuration, int backwardDuration) {
		
		TimingTargetAdapter tta = new TimingTargetAdapter(){

			private final float _diff = end-start;
			
		    @Override public void begin() {}
		    @Override public void end() {}
		    @Override public void timingEvent(float fraction) { _translucent.setWindowOpacity(window, currentValue(fraction)); }
		    
		    private float currentValue(float fraction){
		    	return start + _diff*fraction;
		    }
		};
		return new AnimatorAdapter(tta, forwardDuration, backwardDuration);
	}
}

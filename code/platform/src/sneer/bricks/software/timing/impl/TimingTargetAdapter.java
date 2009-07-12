package sneer.bricks.software.timing.impl;

import sneer.bricks.software.timing.TimingTarget;

public class TimingTargetAdapter implements org.jdesktop.animation.timing.TimingTarget {
	
	private final TimingTarget _delegate;

	TimingTargetAdapter(TimingTarget target){
		_delegate = target;
	}
	
	@Override public void begin() {_delegate.begin(); }
	@Override public void end() {_delegate.end(); }
	@Override public void repeat() {_delegate.repeat(); }
	@Override public void timingEvent(float time) {_delegate.timingEvent(time); }
}
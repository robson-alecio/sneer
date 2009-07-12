package sneer.bricks.software.timing.impl;

import sneer.bricks.software.timing.Animator;
import sneer.bricks.software.timing.TimingTarget;

public class AnimatorAdapter implements Animator{
	
	final org.jdesktop.animation.timing.Animator _delegate ;

	AnimatorAdapter(org.jdesktop.animation.timing.Animator delegate){
		_delegate = delegate;
	}
	
	@Override public void addTarget(TimingTarget target) {_delegate.addTarget(adapt(target));}
	@Override public void removeTarget(TimingTarget target) { _delegate.removeTarget(adapt(target)); }
	
	@Override public void setStartDirection(Direction startDirection) { 
		_delegate.setStartDirection( (startDirection==Direction.BACKWARD) ? 
				org.jdesktop.animation.timing.Animator.Direction.BACKWARD :  
				org.jdesktop.animation.timing.Animator.Direction.FORWARD ); }
	
	@Override public Direction startDirection() { 
		return (_delegate.getStartDirection() == org.jdesktop.animation.timing.Animator.Direction.BACKWARD) ?
				Direction.BACKWARD : Direction.FORWARD;
	}

	@Override public float timingFraction() { return  _delegate.getTimingFraction();}

	@Override public long cycleElapsedTime() { return  _delegate.getCycleElapsedTime();}
	@Override public long cycleElapsedTime(long currentTime) { return  _delegate.getCycleElapsedTime(currentTime);}

	@Override public long totalElapsedTime() { return  _delegate.getTotalElapsedTime(); }
	@Override public long totalElapsedTime(long currentTime) {return  _delegate.getTotalElapsedTime(currentTime); }

	@Override public void pause() { _delegate.pause(); }
	@Override public void resume() {_delegate.resume();}
	@Override public void start() { _delegate.start();}
	@Override public void stop() { _delegate.stop();}
	@Override public void cancel() { _delegate.cancel();}

	private TimingTargetAdapter adapt(TimingTarget target) {
		return new TimingTargetAdapter(target);
	}
}
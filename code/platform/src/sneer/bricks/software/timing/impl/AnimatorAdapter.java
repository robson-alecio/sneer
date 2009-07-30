package sneer.bricks.software.timing.impl;

import org.jdesktop.animation.timing.Animator.Direction;

import sneer.bricks.software.timing.Animator;
import sneer.bricks.software.timing.TimingTarget;

public class AnimatorAdapter implements Animator{
	
	private final org.jdesktop.animation.timing.Animator _delegate;
	
	private final int _backwardDuration;
	private final int _forwardDuration;
	
	AnimatorAdapter(org.jdesktop.animation.timing.Animator animator) {
		_delegate = animator;
		_forwardDuration = animator.getDuration();
		_backwardDuration = animator.getDuration();
	}
	
	AnimatorAdapter(org.jdesktop.animation.timing.Animator forwardAnimator, int backwardDuration) {
		_delegate = forwardAnimator;
		_forwardDuration = forwardAnimator.getDuration();
		_backwardDuration = backwardDuration;
	}
		
	AnimatorAdapter(org.jdesktop.animation.timing.TimingTargetAdapter tta, int duration) {
		this(tta, duration, duration);
	}
	
	AnimatorAdapter(org.jdesktop.animation.timing.TimingTargetAdapter tta, int forwardDuration, int backwardDuration) {
		_forwardDuration = forwardDuration;
		_backwardDuration = backwardDuration;
		_delegate= new org.jdesktop.animation.timing.Animator(forwardDuration, tta);
	}

	@Override public void addTarget(TimingTarget target) { _delegate.addTarget(adapt(target)); }
	@Override public void removeTarget(TimingTarget target) {  _delegate.removeTarget(adapt(target)); }
	@Override public void pause() {  _delegate.pause(); }
	@Override public void resume() { _delegate.resume(); }
	@Override public void stop() {  _delegate.stop(); }
	
	@Override public void playForward(){ play(Direction.FORWARD , _forwardDuration); }
	@Override public void playBackward() { play(Direction.BACKWARD, _backwardDuration); }

	private void play(Direction direction, int duration) {
		_delegate.pause();
		float fraction = _delegate.getTimingFraction();
		
		_delegate.stop();
		_delegate.setStartDirection(direction);
		_delegate.setDuration(duration);
		_delegate.setStartFraction(fraction);
		_delegate.start();
	}

	private TimingTargetAdapter adapt(TimingTarget target) {
		return new sneer.bricks.software.timing.impl.TimingTargetAdapter(target);
	}
}
package sneer.bricks.software.timing.impl;

import sneer.bricks.software.timing.Animator;
import sneer.bricks.software.timing.PropertySetter;
import sneer.bricks.software.timing.TimingFramework;
import sneer.bricks.software.timing.WindowOpacitySetter;

class TimingFrameworkImpl implements TimingFramework{
	
	@Override public PropertySetter property() { return _propertySetter; }
	@Override public WindowOpacitySetter windowOpacity() { return _windowOpacitySetter; }
	
	private final WindowOpacitySetter _windowOpacitySetter =  new WindowOpacitySetterImpl();

	private final PropertySetter _propertySetter =  new PropertySetter(){

		@Override
		public <T> Animator animator(int duration, Object object, String propertyName, T... params) {
			return new AnimatorAdapter(newAnimator(duration, object, propertyName, params));
		}
		
		@Override
		public <T> Animator animator(int forwardDuration, int backwardDuration, Object object, String propertyName, T... params) {
			return new AnimatorAdapter(newAnimator(forwardDuration, object, propertyName, params), backwardDuration);
		}
		
		private  <T>  org.jdesktop.animation.timing.Animator newAnimator(int duration, Object object, String propertyName, T... params) {
			return org.jdesktop.animation.timing.interpolation.PropertySetter.createAnimator(duration, object,  propertyName, params);
		}
	};	
}

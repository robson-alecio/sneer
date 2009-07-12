package sneer.bricks.software.timing.impl;

import sneer.bricks.software.timing.Animator;
import sneer.bricks.software.timing.FocusTrigger;
import sneer.bricks.software.timing.PropertySetter;
import sneer.bricks.software.timing.TimingFramework;
import sneer.bricks.software.timing.Triggers;
import sneer.bricks.software.timing.WindowOpacitySetter;

class TimingFrameworkImpl implements TimingFramework{
	
	@Override public PropertySetter property() { return _propertySetter; }
	@Override public Triggers triggers() { return _triggers; }
	@Override public WindowOpacitySetter windowOpacity() { return _windowOpacitySetter; }
	
	private final FocusTrigger _focusTrigger = new FocusTriggerImpl();
	private final WindowOpacitySetter _windowOpacitySetter =  new WindowOpacitySetterImpl();

	private final PropertySetter _propertySetter =  new PropertySetter(){
		@Override public <T> Animator animator(int duration, Object object, String propertyName, T... params) {
			return new AnimatorAdapter(org.jdesktop.animation.timing.interpolation.PropertySetter.createAnimator(1000, object,  propertyName, params));
		}
	};
	
	private final Triggers _triggers =  new Triggers(){ @Override public FocusTrigger focus() {
		return _focusTrigger;
	}};
}

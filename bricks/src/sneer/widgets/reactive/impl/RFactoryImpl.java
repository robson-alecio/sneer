package sneer.widgets.reactive.impl;

import sneer.widgets.reactive.RFactory;
import sneer.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class RFactoryImpl implements RFactory {

	@Override
	public TextWidget newEditableLabel(Signal<String> source, Omnivore<String> setter) {
		return new REditableLabelImpl(source, setter, false);
	}

	@Override
	public TextWidget newEditableLabel(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange) {
		return new REditableLabelImpl(source, setter, notifyEveryChange);
	}

	@Override
	public TextWidget newLabel(Signal<String> source) {
		return new RLabelImpl(source);
	}

	@Override
	public TextWidget newTextField(Signal<String> source, Omnivore<String> setter) {
		return new RTextFieldImpl(source, setter, false);
	}

	@Override
	public TextWidget newTextField(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange) {
		return new RTextFieldImpl(source, setter, notifyEveryChange);
	}

}

package sneer.skin.widgets.reactive.impl;

import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextField;

import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.ListModelSetter;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public class RFactoryImpl implements RFactory {
	
	@Inject
	private static ImageFactory imageFactory;

	@Override
	public TextWidget<JLabel> newEditableLabel(Signal<String> source, Omnivore<String> setter) {
		return new REditableLabelImpl(source, setter, false);
	}

	@Override
	public TextWidget<JLabel> newEditableLabel(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange) {
		return new REditableLabelImpl(source, setter, notifyEveryChange);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source) {
		return new RLabelImpl(source);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source, Omnivore<String> setter) {
		return new RLabelImpl(source, setter, false);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange) {
		return new RLabelImpl(source, setter, notifyEveryChange);
	}

	@Override
	public TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter) {
		return new RTextFieldImpl(source, setter, false);
	}

	@Override
	public TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange) {
		return new RTextFieldImpl(source, setter, notifyEveryChange);
	}

	@Override
	public ImageWidget newImage(Signal<Image> source) {
		return new RImageImpl(source, imageFactory);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <LW extends ListWidget<?>> LW newList(ListSignal<?> source, ListModelSetter<?> setter) {
		return (LW) new RListImpl(source, setter);
	}
}

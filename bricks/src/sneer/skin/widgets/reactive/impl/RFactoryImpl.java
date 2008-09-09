package sneer.skin.widgets.reactive.impl;

import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextField;

import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public class RFactoryImpl implements RFactory {
	
	@Inject
	private static ImageFactory imageFactory;

	@Override
	public TextWidget<JTextField> newEditableLabel(Signal<String> source, Consumer<String> setter) {
		return new REditableLabelImpl(source, setter, false);
	}

	@Override
	public TextWidget<JTextField> newEditableLabel(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing) {
		return new REditableLabelImpl(source, setter, notifyOnlyWhenDoneEditing);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source) {
		return new RLabelImpl(source);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source, Omnivore<String> setter) {
		return new RLabelImpl(source, setter);
	}

	@Override
	public TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter) {
		return new RTextFieldImpl(source, setter, false);
	}

	@Override
	public TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter, boolean notifyOnlyWhenDoneEditing) {
		return new RTextFieldImpl(source, setter, notifyOnlyWhenDoneEditing);
	}

	@Override
	public ImageWidget newImage(Signal<Image> source,Omnivore<Image> setter) {
		return new RImageImpl(imageFactory, source, setter);
	}

	@Override
	public ImageWidget newImage(Signal<Image> source) {
		return new RImageImpl(imageFactory, source);
	}

	@Override
	public <T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> provider) {
		return new RListImpl<T>(source, provider);
	}
}
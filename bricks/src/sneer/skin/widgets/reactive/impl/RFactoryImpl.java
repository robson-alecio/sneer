package sneer.skin.widgets.reactive.impl;

import java.awt.Image;

import sneer.kernel.container.Inject;
import sneer.skin.image.ImageFactory;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import static wheel.lang.Types.cast;

public class RFactoryImpl implements RFactory {
	
	@Inject
	private static ImageFactory imageFactory;

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

	@Override
	public ImageWidget newImage(Signal<Image> source) {
		return new RImageImpl(source, imageFactory);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListWidget<? extends Object[]> newList(Signal<? extends Object[]> source, Consumer<? extends Object[]> setter) {
		return cast(new RListImpl(source, setter));
	}
}

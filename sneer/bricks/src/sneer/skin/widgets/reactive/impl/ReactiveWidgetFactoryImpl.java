package sneer.skin.widgets.reactive.impl;

import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.GuiThread;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

class ReactiveWidgetFactoryImpl implements ReactiveWidgetFactory {
	
	@Override
	public TextWidget<JTextField> newEditableLabel(Signal<String> source, Consumer<String> setter) {
		GuiThread.assertInGuiThread();
		return new REditableLabelImpl(source, setter, false);
	}

	@Override
	public TextWidget<JTextField> newEditableLabel(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing) {
		GuiThread.assertInGuiThread();
		return new REditableLabelImpl(source, setter, notifyOnlyWhenDoneEditing);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source) {
		GuiThread.assertInGuiThread();
		return new RLabelImpl(source);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source, Omnivore<String> setter) {
		GuiThread.assertInGuiThread();
		return new RLabelImpl(source, setter);
	}

	@Override
	public TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter) {
		GuiThread.assertInGuiThread();
		return new RTextFieldImpl(source, setter, false);
	}

	@Override
	public TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter, boolean notifyOnlyWhenDoneEditing) {
		GuiThread.assertInGuiThread();
		return new RTextFieldImpl(source, setter, notifyOnlyWhenDoneEditing);
	}

	@Override
	public ImageWidget newImage(Signal<Image> source,Omnivore<Image> setter) {
		GuiThread.assertInGuiThread();
		return new RImageImpl(source, setter);
	}

	@Override
	public ImageWidget newImage(Signal<Image> source) {
		GuiThread.assertInGuiThread();
		return new RImageImpl(source);
	}

	@Override
	public <T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> provider) {
		GuiThread.assertInGuiThread();
		return newList(source, provider, null);
	}

	@Override
	public <T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> provider, ListCellRenderer cellRenderer) {
		GuiThread.assertInGuiThread();
		return new RListImpl<T>(source, provider, cellRenderer);
	}
}
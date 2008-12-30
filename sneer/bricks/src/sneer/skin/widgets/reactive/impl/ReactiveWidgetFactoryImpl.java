package sneer.skin.widgets.reactive.impl;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListSignalModel;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import sneer.skin.widgets.reactive.WindowWidget;
import wheel.io.ui.GuiThread;
import wheel.lang.PickyConsumer;
import wheel.lang.Consumer;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

class ReactiveWidgetFactoryImpl implements ReactiveWidgetFactory {
	
	@Override
	public TextWidget<JTextField> newEditableLabel(Signal<String> source, PickyConsumer<String> setter) {
		GuiThread.assertInGuiThread();
		return new REditableLabelImpl(source, setter, NotificationPolicy.OnTyping);
	}

	@Override
	public TextWidget<JTextField> newEditableLabel(Signal<String> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy) {
		GuiThread.assertInGuiThread();
		return new REditableLabelImpl(source, setter, notificationPolicy);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source) {
		GuiThread.assertInGuiThread();
		return new RLabelImpl(source);
	}

	@Override
	public TextWidget<JLabel> newLabel(Signal<String> source, Consumer<String> setter) {
		GuiThread.assertInGuiThread();
		return new RLabelImpl(source, setter);
	}

	@Override
	public TextWidget<JTextField> newTextField(Signal<String> source, Consumer<String> setter) {
		GuiThread.assertInGuiThread();
		return new RTextFieldImpl(source, setter, NotificationPolicy.OnTyping);
	}

	@Override
	public TextWidget<JTextField> newTextField(Signal<String> source, Consumer<String> setter, NotificationPolicy notificationPolicy) {
		GuiThread.assertInGuiThread();
		return new RTextFieldImpl(source, setter, notificationPolicy);
	}

	@Override
	public TextWidget<JTextPane> newTextPane(Signal<String> source, Consumer<String> setter) {
		GuiThread.assertInGuiThread();
		return new RTextPaneImpl(source, setter, NotificationPolicy.OnTyping);
	}

	@Override
	public TextWidget<JTextPane> newTextPane(Signal<String> source, Consumer<String> setter, NotificationPolicy notificationPolicy) {
		GuiThread.assertInGuiThread();
		return new RTextPaneImpl(source, setter, notificationPolicy);
	}

	@Override
	public ImageWidget newImage(Signal<Image> source,Consumer<Image> setter) {
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
	
	@Override
	public <T> ListSignalModel<T> newListSignalModel(ListSignal<T> input, SignalChooser<T> chooser) {
		return new ListSignalModelImpl<T>(input, chooser);
	}
	
	@Override
	public WindowWidget<JFrame> newFrame(Signal<String> source) {
		GuiThread.assertInGuiThread();
		return new RFrameImpl(source);
	}

	@Override
	public WindowWidget<JFrame> newFrame(Signal<String> source, Consumer<String> setter) {
		GuiThread.assertInGuiThread();
		return new RFrameImpl(source, setter);
	}
}
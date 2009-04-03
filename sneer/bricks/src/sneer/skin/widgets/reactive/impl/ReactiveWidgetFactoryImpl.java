package sneer.skin.widgets.reactive.impl;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import sneer.skin.widgets.reactive.WindowWidget;
import wheel.io.ui.GuiThread;
import wheel.lang.PickyConsumer;
import wheel.reactive.lists.ListSignal;

class ReactiveWidgetFactoryImpl implements ReactiveWidgetFactory {
	
	@Override
	public TextWidget<JLabel> newLabel(Signal<?> source) {
		GuiThread.assertInGuiThread();
		return new RLabelImpl(source);
	}
	

	@Override
	public ImageWidget newImage(Signal<Image> source) {
		GuiThread.assertInGuiThread();
		return new RImageImpl(source);
	}
	
	
	@Override
	public <T> ListModel newListSignalModel(ListSignal<T> input, SignalChooser<T> chooser) {
		return new ListSignalModel<T>(input, chooser);
	}
	
	
	@Override
	public WindowWidget<JFrame> newFrame(Signal<?> source) {
		GuiThread.assertInGuiThread();
		return new RFrameImpl(source);
	}

	
	@Override
	public TextWidget<JTextField> newEditableLabel(Signal<?> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy) {
		GuiThread.assertInGuiThread();
		return new REditableLabelImpl(source, setter, notificationPolicy);
	}
	@Override public TextWidget<JTextField> newEditableLabel(Signal<?> source, PickyConsumer<String> setter) { return newEditableLabel(source, setter, NotificationPolicy.OnTyping);}
	
	
	@Override
	public TextWidget<JTextField> newTextField(Signal<?> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy) {
		GuiThread.assertInGuiThread();
		return new RTextFieldImpl(source, setter, notificationPolicy);
	}
	@Override public TextWidget<JTextField> newTextField(Signal<?> source, PickyConsumer<String> setter) { return newTextField(source, setter, NotificationPolicy.OnTyping); }

	
	@Override
	public TextWidget<JTextPane> newTextPane(Signal<?> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy) {
		GuiThread.assertInGuiThread();
		return new RTextPaneImpl(source, setter, notificationPolicy);
	}
	@Override public TextWidget<JTextPane> newTextPane(Signal<?> source, PickyConsumer<String> setter) { return newTextPane(source, setter, NotificationPolicy.OnTyping); }
	
	
	@Override
	public <T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> provider, ListCellRenderer cellRenderer) {
		GuiThread.assertInGuiThread();
		return new RListImpl<T>(source, provider, cellRenderer);
	}
	@Override public <T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> provider) { return newList(source, provider, null); }
}
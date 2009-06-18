package sneer.bricks.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.PickyConsumer;

@Brick
public interface ReactiveWidgetFactory {

	ImageWidget newImage(Signal<Image> source);
	Widget<JFrame> newFrame(Signal<?> title);
	
	TextWidget<JLabel> newLabel(Signal<?> source);
	TextWidget<JLabel> newLabel(Signal<String> source, String synthName);
	
	TextWidget<JTextField> newEditableLabel(Signal<?> source, PickyConsumer<? super String> setter);
	TextWidget<JTextField> newEditableLabel(Signal<?> source, PickyConsumer<? super String> setter, NotificationPolicy notificationPolicy);
	
	TextWidget<JTextField> newTextField(Signal<?> source, PickyConsumer<? super String> setter);
	TextWidget<JTextField> newTextField(Signal<?> source, PickyConsumer<? super String> setter, NotificationPolicy notificationPolicy);
	
	TextWidget<JTextPane> newTextPane(Signal<?> source, PickyConsumer<? super String> setter);
	TextWidget<JTextPane> newTextPane(Signal<?> source, PickyConsumer<? super String> setter, NotificationPolicy notificationPolicy);

	<T> ListWidget<T> newList(ListSignal<T> source);
	<T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> labelProvider);
	<T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> labelProvider,	ListCellRenderer cellRenderer);
	<T> ListModel newListSignalModel(ListSignal<T> input, SignalChooser<T> chooser);
}
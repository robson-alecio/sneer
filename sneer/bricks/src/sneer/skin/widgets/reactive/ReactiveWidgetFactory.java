package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.commons.lang.Functor;
import wheel.lang.PickyConsumer;
import wheel.reactive.lists.ListSignal;

public interface ReactiveWidgetFactory extends OldBrick {

	ImageWidget newImage(Signal<Image> source);
	WindowWidget<JFrame> newFrame(Signal<?> source);
	TextWidget<JLabel> newLabel(Signal<?> source);
	
	TextWidget<JTextField> newEditableLabel(Signal<?> source, PickyConsumer<String> setter);
	TextWidget<JTextField> newEditableLabel(Signal<?> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy);
	<T> TextWidget<JTextField> newEditableLabel(Signal<?> source, PickyConsumer<T> setter, Functor<String, T> parser);
	<T> TextWidget<JTextField> newEditableLabel(Signal<?> source, PickyConsumer<T> setter, Functor<String, T> parser, NotificationPolicy notificationPolicy);
	
	TextWidget<JTextField> newTextField(Signal<?> source, PickyConsumer<String> setter);
	TextWidget<JTextField> newTextField(Signal<?> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy);
	<T> TextWidget<JTextField> newTextField(Signal<?> source, PickyConsumer<T> setter, Functor<String, T> parser);
	<T> TextWidget<JTextField> newTextField(Signal<?> source, PickyConsumer<T> setter, Functor<String, T> parser, NotificationPolicy notificationPolicy);
	
	TextWidget<JTextPane> newTextPane(Signal<?> source, PickyConsumer<String> setter);
	TextWidget<JTextPane> newTextPane(Signal<?> source, PickyConsumer<String> setter, NotificationPolicy notificationPolicy);
	<T> TextWidget<JTextPane> newTextPane(Signal<?> source, PickyConsumer<T> setter, Functor<String, T> parser);
	<T> TextWidget<JTextPane> newTextPane(Signal<?> source, PickyConsumer<T> setter, Functor<String, T> parser, NotificationPolicy notificationPolicy);

	<T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> labelProvider);
	<T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> labelProvider,	ListCellRenderer cellRenderer);
	<T> ListModel newListSignalModel(ListSignal<T> input, SignalChooser<T> chooser);
}
package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

import sneer.pulp.reactive.signalchooser.SignalChooser;
import wheel.lang.PickyConsumer;
import wheel.lang.Consumer;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface ReactiveWidgetFactory {

	ImageWidget newImage(Signal<Image> source);
	ImageWidget newImage(Signal<Image> source, Consumer<Image> setter);
	
	TextWidget<JLabel> newLabel(Signal<String> source);
	TextWidget<JLabel> newLabel(Signal<String> source, Consumer<String> setter);
	
	TextWidget<JTextField> newEditableLabel(Signal<String> source, PickyConsumer<String> setter);
	TextWidget<JTextField> newEditableLabel(Signal<String> source, PickyConsumer<String> setter, boolean notifyOnlyWhenDoneEditing);
	
	TextWidget<JTextField> newTextField(Signal<String> source, Consumer<String> setter);
	TextWidget<JTextField> newTextField(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing);
	
	TextWidget<JTextPane> newTextPane(Signal<String> source, Consumer<String> setter);
	TextWidget<JTextPane> newTextPane(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing);

	<T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> labelProvider);
	<T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> labelProvider,	ListCellRenderer cellRenderer);
	<T> ListSignalModel<T> newListSignalModel(ListSignal<T> input, SignalChooser<T> chooser);
	
	WindowWidget<JFrame> newFrame(Signal<String> source);
	WindowWidget<JFrame> newFrame(Signal<String> source, Consumer<String> setter);

}
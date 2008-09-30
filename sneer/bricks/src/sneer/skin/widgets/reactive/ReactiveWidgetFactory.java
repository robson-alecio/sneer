package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextField;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface ReactiveWidgetFactory {

	ImageWidget newImage(Signal<Image> source);
	ImageWidget newImage(Signal<Image> source, Omnivore<Image> setter);
	
	TextWidget<JLabel> newLabel(Signal<String> source);
	TextWidget<JLabel> newLabel(Signal<String> source, Omnivore<String> setter);
	
	TextWidget<JTextField> newEditableLabel(Signal<String> source, Consumer<String> setter);
	TextWidget<JTextField> newEditableLabel(Signal<String> source, Consumer<String> setter, boolean notifyOnlyWhenDoneEditing);
	
	TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter);
	TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter, boolean notifyOnlyWhenDoneEditing);
	
	<T> ListWidget<T> newList(ListSignal<T> source, LabelProvider<T> _labelProvider);

}
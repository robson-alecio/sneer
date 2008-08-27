package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextField;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface RFactory {

	ImageWidget newImage(Signal<Image> source);
	
	TextWidget<JLabel> newLabel(Signal<String> source);
	TextWidget<JLabel> newLabel(Signal<String> source, Omnivore<String> setter);
	TextWidget<JLabel> newLabel(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange);
	
	TextWidget<JLabel> newEditableLabel(Signal<String> source, Omnivore<String> setter);
	TextWidget<JLabel> newEditableLabel(Signal<String> source, Omnivore<String> setter,	boolean notifyEveryChange);
	
	TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter);
	TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange);
	
	<LW extends ListWidget<?>> LW newList(ListSignal<?> source, ListModelSetter<?> setter);


}
package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextField;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListRegister;

public interface RFactory {

	ImageWidget newImage(Signal<Image> source);
	
	TextWidget<JLabel> newLabel(Signal<String> source);
	TextWidget<JLabel> newLabel(Signal<String> source, Omnivore<String> setter);
	
	TextWidget<JTextField> newEditableLabel(Signal<String> source, Omnivore<String> setter);
	TextWidget<JTextField> newEditableLabel(Signal<String> source, Omnivore<String> setter,	boolean notifyEveryChange);
	
	TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter);
	TextWidget<JTextField> newTextField(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange);
	
	<LW extends ListWidget<?>> LW newList(ListRegister<?> register);
}
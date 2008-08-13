package sneer.widgets.reactive;

import java.awt.Image;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface RFactory {

	TextWidget newLabel(Signal<String> source);
	
	ImageWidget newImage(Signal<Image> source);
	
	TextWidget newEditableLabel(Signal<String> source, Omnivore<String> setter);
	TextWidget newEditableLabel(Signal<String> source, Omnivore<String> setter,	boolean notifyEveryChange);
	
	TextWidget newTextField(Signal<String> source, Omnivore<String> setter);
	TextWidget newTextField(Signal<String> source, Omnivore<String> setter, boolean notifyEveryChange);

}
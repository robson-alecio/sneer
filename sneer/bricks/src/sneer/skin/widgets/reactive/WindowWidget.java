package sneer.skin.widgets.reactive;

import java.awt.Window;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface WindowWidget extends Widget<Window>{

	Signal<String> output();
	Omnivore<String> setter();	
}
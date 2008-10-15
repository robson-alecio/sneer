package sneer.skin.widgets.reactive;

import java.awt.Window;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface WindowWidget<WINDOW extends Window> extends Widget<WINDOW>{

	Signal<String> output();
	Omnivore<String> setter();	
}
package sneer.skin.widgets.reactive;

import java.awt.Window;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface WindowWidget<WINDOW extends Window> extends Widget<WINDOW>{

	Signal<String> output();
	Consumer<String> setter();	
}
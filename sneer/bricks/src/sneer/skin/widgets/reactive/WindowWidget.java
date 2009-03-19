package sneer.skin.widgets.reactive;

import java.awt.Window;

import sneer.pulp.reactive.Signal;

import wheel.lang.Consumer;

public interface WindowWidget<WINDOW extends Window> extends Widget<WINDOW>{

	Signal<String> output();
	Consumer<String> setter();	
}
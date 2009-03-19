package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JPanel;

import sneer.pulp.reactive.Signal;

import wheel.lang.Consumer;

public interface ImageWidget extends ComponentWidget<JPanel>{

	Signal<Image> output();
	Consumer<Image> setter();	
}
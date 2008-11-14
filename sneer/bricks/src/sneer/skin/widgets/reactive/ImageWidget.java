package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JPanel;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface ImageWidget extends ComponentWidget<JPanel>{

	Signal<Image> output();
	Consumer<Image> setter();	
}
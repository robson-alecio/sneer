package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JPanel;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ImageWidget extends Widget<JPanel>{

	Signal<Image> output();
	Omnivore<Image> setter();	
}
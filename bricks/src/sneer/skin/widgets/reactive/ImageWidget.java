package sneer.skin.widgets.reactive;

import java.awt.Component;
import java.awt.Image;

import javax.swing.JComponent;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface ImageWidget {

	Signal<Image> output();
	Omnivore<Image> setter();	

	Component getComponent();
	JComponent getMainWidget();
}
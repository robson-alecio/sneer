package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JPanel;

import sneer.pulp.reactive.Signal;
import sneer.software.lang.PickyConsumer;

public interface ImageWidget extends ComponentWidget<JPanel>{

	Signal<Image> output();
	PickyConsumer<Image> setter();	
}
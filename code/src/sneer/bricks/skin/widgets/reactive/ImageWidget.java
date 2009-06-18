package sneer.bricks.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JPanel;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.lang.PickyConsumer;

public interface ImageWidget extends ComponentWidget<JPanel>{

	Signal<Image> output();
	PickyConsumer<Image> setter();	
}
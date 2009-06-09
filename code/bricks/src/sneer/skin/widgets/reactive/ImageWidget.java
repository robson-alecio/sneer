package sneer.skin.widgets.reactive;

import java.awt.Image;

import javax.swing.JPanel;

import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.reactive.Signal;

public interface ImageWidget extends ComponentWidget<JPanel>{

	Signal<Image> output();
	PickyConsumer<Image> setter();	
}
package sneer.bricks.skin.popuptrigger;

import java.awt.Component;
import java.awt.event.MouseEvent;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface PopupTrigger {
	
	void listen(Component sorce, Consumer<MouseEvent> receiver);
	
}

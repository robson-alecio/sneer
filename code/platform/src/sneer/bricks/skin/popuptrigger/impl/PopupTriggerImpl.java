package sneer.bricks.skin.popuptrigger.impl;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sneer.bricks.skin.popuptrigger.PopupTrigger;
import sneer.foundation.lang.Consumer;

public class PopupTriggerImpl implements PopupTrigger {

	@Override
	public void listen(Component sorce, final Consumer<MouseEvent> receiver) {
		sorce.addMouseListener(new MouseAdapter(){ 
			@Override public void mousePressed(MouseEvent e) { receiver.consume(e); }
			@Override public void mouseReleased(MouseEvent e) { receiver.consume(e); }
		});
	}
}

package sneer.skin.widgets.reactive.autoscroll;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.ListSignal;

@Brick
public interface AutoScrolls {
	
	<T> JScrollPane create(JTextPane component, ListSignal<T> inputSignal, Consumer<CollectionChange<T>> receiver);

}
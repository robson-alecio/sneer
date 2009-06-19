package sneer.bricks.skin.widgets.reactive;

import javax.swing.JList;

import sneer.bricks.pulp.reactive.Signal;

public interface ListWidget<ELEMENT> extends ComponentWidget<JList>{

	Signal<ELEMENT> selectedElement();
	void clearSelection();

}

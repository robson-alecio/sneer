package sneer.skin.widgets.reactive;

import javax.swing.JList;

import sneer.pulp.reactive.Signal;

public interface ListWidget<ELEMENT> extends ComponentWidget<JList>{

	Signal<ELEMENT> selectedElement();
	void clearSelection();

}

package sneer.skin.widgets.reactive;

import javax.swing.JList;

import wheel.reactive.lists.ListSignal;

public interface ListWidget<ELEMENT> extends Widget<JList>{

	ListSignal<ELEMENT> output();
	void setLabelProvider(LabelProvider<ELEMENT> labelProvider);
	
}

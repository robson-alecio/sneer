package sneer.skin.widgets.reactive;

import javax.swing.JList;

import wheel.reactive.lists.ListRegister;

public interface ListWidget<ELEMENT> extends Widget<JList>{

	ListRegister<ELEMENT> register();
	void setLabelProvider(LabelProvider<ELEMENT> labelProvider);
	
}

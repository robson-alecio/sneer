package sneer.snapp.owner.impl;

import java.awt.Container;
import java.awt.FlowLayout;

import sneer.bricks.ownName.OwnNameKeeper;
import sneer.lego.Inject;
import sneer.snapp.owner.OwnerSnapp;
import sneer.widgets.reactive.RFactory;
import sneer.widgets.reactive.TextWidget;

public class OwnerSnappImpl implements OwnerSnapp {

	@Inject
	static private OwnNameKeeper ownNameKeeper;

	@Inject
	static private RFactory rfactory;

	private TextWidget editableLabel;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Container container) {	
        container.setLayout(new FlowLayout());
        editableLabel = rfactory.newEditableLabel(
	        	ownNameKeeper.name(), 
				ownNameKeeper.nameSetter());

        container.add(editableLabel.getContainer());
	}

	@Override
	public TextWidget getEditableLabel() {
		return editableLabel;
	}

	@Override
	public OwnNameKeeper getOwnNameKeeper() {
		return ownNameKeeper;
	}

	@Override
	public String getName() {
		return "Owner";
	}
}
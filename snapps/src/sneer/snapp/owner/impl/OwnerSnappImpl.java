package sneer.snapp.owner.impl;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import sneer.bricks.ownName.OwnNameKeeper;
import sneer.bricks.ownTagline.OwnTaglineKeeper;
import sneer.lego.Inject;
import sneer.snapp.owner.OwnerSnapp;
import sneer.widgets.reactive.RFactory;
import sneer.widgets.reactive.TextWidget;

public class OwnerSnappImpl implements OwnerSnapp {

	@Inject
	static private OwnNameKeeper ownNameKeeper;

	@Inject
	static private OwnTaglineKeeper ownTaglineKeeper;

	@Inject
	static private RFactory rfactory;

	private TextWidget editableLabel;

	@Override
	public void init(Container container) {	
		container.setLayout(new GridBagLayout());
		
		GridBagConstraints c;
		
		c = new GridBagConstraints(0,0,1,1,1.0,1.0,
					GridBagConstraints.EAST, 
					GridBagConstraints.HORIZONTAL,
					new Insets(0,0,0,0),0,0);
		editableLabel = rfactory.newEditableLabel(
	        	ownNameKeeper.name(), 
				ownNameKeeper.nameSetter());
        container.add(editableLabel.getContainer(), c);
        
		c = new GridBagConstraints(0,1,1,1,1.0,1.0,
				GridBagConstraints.EAST, 
				GridBagConstraints.HORIZONTAL,
				new Insets(0,0,0,0),0,0);
       editableLabel = rfactory.newEditableLabel(
        		ownTaglineKeeper.tagline(), 
        		ownTaglineKeeper.taglineSetter());
        container.add(editableLabel.getContainer(), c);
	}

	@Override
	public String getName() {
		return "Owner";
	}
}
package sneer.snapp.owner.impl;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JSeparator;

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
		
		c = getConstraints(0, 10,10,0,10);
		editableLabel = rfactory.newEditableLabel(
	        	ownNameKeeper.name(), 
				ownNameKeeper.nameSetter());
		
        container.add(editableLabel.getContainer(), c);
 
		c = getConstraints(1, 0,10,0,0);
        JSeparator separator = new JSeparator();
		container.add(separator, c);
        
		c = getConstraints(2, 0,10,10,10);
        editableLabel = rfactory.newEditableLabel(
        		ownTaglineKeeper.tagline(), 
        		ownTaglineKeeper.taglineSetter());
        container.add(editableLabel.getContainer(), c);
        
		c = new GridBagConstraints(1,0, 1,3,0.0,0.0,
				GridBagConstraints.EAST, 
				GridBagConstraints.BOTH,
				new Insets(5,0,5,5),0,0);
		
        container.add(new JButton(), c);
        
	}

	private GridBagConstraints getConstraints(int y, int top, int left, int botton, int right) {
		GridBagConstraints c;
		c = new GridBagConstraints(0,y,1,1,1.0,1.0,
					GridBagConstraints.EAST, 
					GridBagConstraints.HORIZONTAL,
					new Insets(top,left,botton,right),0,0);
		return c;
	}

	@Override
	public String getName() {
		return "Owner";
	}
}
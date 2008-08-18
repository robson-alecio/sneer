package sneerapps.owner.impl;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSeparator;

import sneer.bricks.ownAvatar.OwnAvatarKeeper;
import sneer.bricks.ownName.OwnNameKeeper;
import sneer.bricks.ownTagline.OwnTaglineKeeper;
import sneer.lego.Inject;
import sneer.skin.imageSelector.ImageSelector;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import sneerapps.owner.OwnerSnapp;

public class OwnerSnappImpl implements OwnerSnapp {

	@Inject
	static private OwnNameKeeper _ownNameKeeper;

	@Inject
	static private OwnTaglineKeeper _ownTaglineKeeper;

	@Inject
	static private OwnAvatarKeeper _ownAvatarKeeper;

	@Inject
	static private ImageSelector _imageSelector;

	@Inject
	static private RFactory rfactory;

	private TextWidget editableLabel;

	private Container _container;

	@Override
	public void init(Container container) {	
		_container = container;
		container.setLayout(new GridBagLayout());
		
		GridBagConstraints c;
		
		c = getConstraints(0, 5,10,0,10);
		editableLabel = rfactory.newEditableLabel(
	        	_ownNameKeeper.name(), 
				_ownNameKeeper.nameSetter());
		c.anchor = GridBagConstraints.SOUTHEAST;
		
        container.add(editableLabel.getContainer(), c);
 
		c = getConstraints(1, 0,10,0,0);
        JSeparator separator = new JSeparator();
		container.add(separator, c);
        
		c = getConstraints(2, 0,10,5,10);
        editableLabel = rfactory.newEditableLabel(
        		_ownTaglineKeeper.tagline(), 
        		_ownTaglineKeeper.taglineSetter());
		c.anchor = GridBagConstraints.NORTHEAST;

        container.add(editableLabel.getContainer(), c);
        
        
		c = new GridBagConstraints(1,0, 1,3,0.0,0.0,
				GridBagConstraints.EAST, 
				GridBagConstraints.BOTH,
				new Insets(5,0,5,5),0,0);
		
		ImageWidget avatar = rfactory.newImage(_ownAvatarKeeper.avatar(48));
		container.add(avatar.getComponent(), c);
		addMouseLitener(avatar);
	}

	private void addMouseLitener(ImageWidget avatar) {
		avatar.getMainWidget().addMouseListener(new MouseAdapter(){
			@Override
			public void mouseReleased(MouseEvent e) {
				_imageSelector.open(_ownAvatarKeeper.avatarSetter());
			}
		
			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
				_container.setCursor(cursor);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
				_container.setCursor(cursor);
			}
		});
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
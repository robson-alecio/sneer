package snapps.owner.impl;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSeparator;
import javax.swing.JTextField;

import snapps.owner.OwnerSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.own.avatar.OwnAvatarKeeper;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.own.tagline.OwnTaglineKeeper;
import sneer.skin.imgselector.ImageSelector;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;

class OwnerSnappImpl implements OwnerSnapp {

	@Inject
	static private OwnNameKeeper _ownNameKeeper;

	@Inject
	static private OwnTaglineKeeper _ownTaglineKeeper;

	@Inject
	static private OwnAvatarKeeper _ownAvatarKeeper;

	@Inject
	static private SnappManager _snappManager;

	@Inject
	static private ImageSelector _imageSelector;

	@Inject
	static private RFactory _rfactory;

	private TextWidget<JTextField> _editableLabel;

	private Container _container;

	public OwnerSnappImpl(){
		_snappManager.registerSnapp(this);
	}
	
	@Override
	public void init(Container container) {	
		_container = container;
		container.setLayout(new GridBagLayout());
		
		initOwnNameKeeper(container);
		initOwnTaglineKeeper(container);
		initOwnAvatarKeeper(container);

	}

	private void initOwnNameKeeper(Container container) {
		GridBagConstraints c;
		c = getConstraints(0, 5,10,0,10);
		_editableLabel = _rfactory.newEditableLabel(
				_ownNameKeeper.name(), 
				_ownNameKeeper.nameSetter());
		c.anchor = GridBagConstraints.SOUTHEAST;
		
		container.add(_editableLabel.getComponent(), c);
	}
	
	private void initOwnTaglineKeeper(Container container) {
		GridBagConstraints c;
		c = getConstraints(1, 0,10,0,0);
        JSeparator separator = new JSeparator();
		container.add(separator, c);
        
		c = getConstraints(2, 0,10,5,10);
        _editableLabel = _rfactory.newEditableLabel(
        		_ownTaglineKeeper.tagline(), 
        		_ownTaglineKeeper.taglineSetter());
		c.anchor = GridBagConstraints.NORTHEAST;

        container.add(_editableLabel.getComponent(), c);
	}

	private void initOwnAvatarKeeper(Container container) {
		GridBagConstraints c;
		c = new GridBagConstraints(1,0, 1,3,0.0,0.0,
				GridBagConstraints.EAST, 
				GridBagConstraints.BOTH,
				new Insets(5,0,5,5),0,0);
		
		ImageWidget avatar = _rfactory.newImage(_ownAvatarKeeper.avatar(48));
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

}
package spikes.sneer.bricks.snapps.owner.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSeparator;
import javax.swing.JTextField;

import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.skin.imgselector.ImageSelector;
import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.bricks.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.bricks.skin.widgets.reactive.ImageWidget;
import sneer.bricks.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.bricks.skin.widgets.reactive.TextWidget;
import spikes.sneer.bricks.pulp.own.avatar.OwnAvatarKeeper;
import spikes.sneer.bricks.pulp.own.tagline.OwnTaglineKeeper;
import spikes.sneer.bricks.snapps.owner.gui.OwnerGui;

class OwnerGuiImpl implements OwnerGui {

	private final OwnNameKeeper _ownNameKeeper = my(OwnNameKeeper.class);

	private final OwnTaglineKeeper _ownTaglineKeeper = my(OwnTaglineKeeper.class);

	private final OwnAvatarKeeper _ownAvatarKeeper = my(OwnAvatarKeeper.class);

	private final InstrumentRegistry _instrumentManager = my(InstrumentRegistry.class);

	private final ImageSelector _imageSelector = my(ImageSelector.class);

	private final ReactiveWidgetFactory _rfactory = my(ReactiveWidgetFactory.class);

	private TextWidget<JTextField> _editableLabel;

	private Container _container;

	OwnerGuiImpl(){
		_instrumentManager.registerInstrument(this);
	}
	
	@Override
	public void init(InstrumentPanel window) {
		_container = window.contentPane();
		_container.setLayout(new GridBagLayout());
		
		initOwnNameKeeper(_container);
		initOwnTaglineKeeper(_container);
		initOwnAvatarKeeper(_container);
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

	@Override
	public int defaultHeight() {
		return DEFAULT_HEIGHT;
	}

	@Override
	public String title() {
		return "Owner";
	}
}
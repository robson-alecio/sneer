package sneer.bricks.software.sharing.gui.impl;

import javax.swing.AbstractListModel;

import sneer.bricks.software.sharing.BrickVersion;
import sneer.bricks.software.sharing.FileVersion;

public class FileVersionListModel extends AbstractListModel{

	private final BrickVersion _brickVersion;

	FileVersionListModel(BrickVersion brickVersion){
		_brickVersion = brickVersion;
	}
	
	@Override public Object getElementAt(int index) {
		return wrap(_brickVersion.files().get(index));
	}

	@Override public int getSize() {
		return _brickVersion.files().size();
	}

	private Object wrap(FileVersion fileVersion){
		return new FileVersionWrapper(fileVersion);
	}
}

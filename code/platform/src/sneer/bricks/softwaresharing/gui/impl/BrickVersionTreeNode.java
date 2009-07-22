package sneer.bricks.softwaresharing.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.BrickVersion.Status;

class BrickVersionTreeNode extends AbstractTreeNodeWrapper<String> {

	private final BrickVersion _brickVersion;

	private static final ImageIcon _currentVersion = loadIcon("currentVersion.png");
	private static final ImageIcon _differentVersion = loadIcon("differentVersion.png");
	private static final ImageIcon _rejectedVersion = loadIcon("rejectedVersion.png");

	private static ImageIcon loadIcon(String fileName){
		return my(ImageFactory.class).getIcon(BrickInfoTreeNode.class, fileName);
	}
	
	BrickVersionTreeNode(TreeNode parent, BrickVersion brickVersion) {
		super(parent, brickVersion);
		_brickVersion = brickVersion;
		
		if(brickVersion.status()==Status.DIFFERENT){
			_icon = _differentVersion;
			return;
		}
		if(brickVersion.status()==Status.REJECTED){
			_icon = _rejectedVersion;
			return;
		}
		_icon = _currentVersion;
	}

	@Override protected List<String> listChildren() {return _brickVersion.knownUsers();}
	
	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new StringTreeNode(this, listChildren().get(childIndex));
	}	
	
	@Override public String toString() {return  new Date(_brickVersion.publicationDate()).toString();	}
}

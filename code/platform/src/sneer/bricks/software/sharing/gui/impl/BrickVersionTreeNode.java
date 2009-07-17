package sneer.bricks.software.sharing.gui.impl;

import java.util.Date;
import java.util.List;

import javax.swing.tree.TreeNode;

import sneer.bricks.software.sharing.BrickVersion;

class BrickVersionTreeNode extends AbstractTreeNodeWrapper<String> {

	private final BrickVersion _brickVersion;
	
	BrickVersionTreeNode(TreeNode parent, BrickVersion brickVersion) {
		super(parent, brickVersion);
		_brickVersion = brickVersion;
	}

	@Override protected List<String> listChildren() {return _brickVersion.knownUsers();}
	
	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new StringTreeNode(this, listChildren().get(childIndex));
	}	
	
	@Override public String toString() {return  new Date(_brickVersion.publicationDate()).toString();	}
}

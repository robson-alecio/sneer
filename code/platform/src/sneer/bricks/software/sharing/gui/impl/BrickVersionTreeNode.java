package sneer.bricks.software.sharing.gui.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import sneer.bricks.software.sharing.BrickVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickVersionTreeNode extends AbstractTreeNodeWrapper<Object> {

	private final static List<Object> _empty = new ArrayList<Object>();
	
	BrickVersionTreeNode(TreeNode parent, BrickVersion brickVersion) {
		super(parent, brickVersion);
	}

	@Override protected List<Object> listChildren() {return _empty;}
	@Override protected AbstractTreeNodeWrapper<Object> wrapChild(int childIndex) {throw new NotImplementedYet();}
	
}

package sneer.bricks.software.sharing.gui.impl;

import java.util.List;

import javax.swing.tree.TreeNode;

import sneer.bricks.software.sharing.BrickInfo;
import sneer.bricks.software.sharing.BrickVersion;

class BrickInfoTreeNode extends AbstractTreeNodeWrapper<BrickVersion> {

	private final BrickInfo _brickInfo;

	BrickInfoTreeNode(TreeNode parent, BrickInfo brickInfo){
		super(parent, brickInfo);
		_brickInfo = brickInfo;
	}
	
	@Override public String toString() {  return _brickInfo.name(); }
	@Override protected List<BrickVersion> listChildren() { return _brickInfo.versions(); }

	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new BrickVersionTreeNode(this, listChildren().get(childIndex));
	}
}

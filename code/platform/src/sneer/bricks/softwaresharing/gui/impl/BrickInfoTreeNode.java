package sneer.bricks.softwaresharing.gui.impl;

import static sneer.foundation.environments.Environments.my;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickVersion;

class BrickInfoTreeNode extends AbstractTreeNodeWrapper<BrickVersion> {

	private final BrickInfo _brickInfo;
//	private static final ImageIcon _newBrick = loadIcon("newBrick.png");
//	private static final ImageIcon _currentBrick = loadIcon("currentBrick.png");
	private static final ImageIcon _rejectedBrick = loadIcon("rejectedBrick.png");
	private static final ImageIcon _conflictBrick = loadIcon("conflictBrick.png");
	private static boolean change;

	private static ImageIcon loadIcon(String fileName){
		return my(ImageFactory.class).getIcon(BrickInfoTreeNode.class, fileName);
	}
	
	BrickInfoTreeNode(TreeNode parent, BrickInfo brickInfo){
		super(parent, brickInfo);
		_brickInfo = brickInfo;
		change = !change;
		if(change){
			_icon = _conflictBrick;
		}else{
			_icon = _rejectedBrick;
		}
	}
	
	@Override public String toString() {  return _brickInfo.name(); }
	@Override protected List<BrickVersion> listChildren() { return _brickInfo.versions(); }

	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new BrickVersionTreeNode(this, listChildren().get(childIndex));
	}
}

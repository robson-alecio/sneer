package sneer.bricks.software.sharing.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.software.sharing.BrickInfo;
import sneer.bricks.software.sharing.BrickVersion;
import sneer.bricks.software.sharing.BrickInfo.Status;

class BrickInfoTreeNode extends AbstractTreeNodeWrapper<BrickVersion> {

	private final BrickInfo _brickInfo;
	private static final ImageIcon _newBrick = loadIcon("newBrick.png");
	private static final ImageIcon _currentBrick = loadIcon("currentBrick.png");
	private static final ImageIcon _rejectedBrick = loadIcon("rejectedBrick.png");
	private static final ImageIcon _differentBrick = loadIcon("differentBrick.png");

	private static ImageIcon loadIcon(String fileName){
		return my(ImageFactory.class).getIcon(BrickInfoTreeNode.class, fileName);
	}
	
	BrickInfoTreeNode(TreeNode parent, BrickInfo brickInfo){
		super(parent, brickInfo);
		_brickInfo = brickInfo;
		
		if(_brickInfo.status() == Status.DIFFERENT ) {
			_icon = _differentBrick;
			return;
		}
		
		if(_brickInfo.status() == Status.NEW ) {
			_icon = _newBrick;
			return;
		}
		
		if(_brickInfo.status() == Status.REJECTED ) {
			_icon = _rejectedBrick;
			return;
		}
		
		_icon = _currentBrick;
	}
	
	@Override public String toString() {  return _brickInfo.name(); }
	@Override protected List<BrickVersion> listChildren() { return _brickInfo.versions(); }

	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new BrickVersionTreeNode(this, listChildren().get(childIndex));
	}
}

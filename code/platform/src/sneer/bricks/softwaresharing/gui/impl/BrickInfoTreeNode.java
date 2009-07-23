package sneer.bricks.softwaresharing.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.BrickInfo.Status;

class BrickInfoTreeNode extends AbstractTreeNodeWrapper<BrickVersion> {

	private final BrickInfo _brickInfo;
	private static final ImageIcon _newBrick = loadIcon("newBrick.png");
	private static final ImageIcon _currentBrick = loadIcon("currentBrick.png");
	private static final ImageIcon _rejectedBrick = loadIcon("rejectedBrick.png");
	private static final ImageIcon _differentBrick = loadIcon("differentBrick.png");
	private static final ImageIcon _divergingBrick = loadIcon("divergingBrick.png");

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
		
		if(_brickInfo.status() == Status.DIVERGING ) {
			_icon = _divergingBrick;
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

	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new BrickVersionTreeNode(this, listChildren().get(childIndex));
	}

	@Override protected List<BrickVersion> listChildren() { 
		Collections.sort(_brickInfo.versions(), new Comparator<BrickVersion>(){ @Override public int compare(BrickVersion v1, BrickVersion v2) {
			if(v1.publicationDate()==v2.publicationDate())
				return usersCount(v1) - usersCount(v2);
			
			return (int)(v1.publicationDate()-v2.publicationDate());
		}

		private int usersCount(BrickVersion v1) {
			return v1.unknownUsers() + v1.knownUsers().size();
		}});
		return _brickInfo.versions(); 
	}

}

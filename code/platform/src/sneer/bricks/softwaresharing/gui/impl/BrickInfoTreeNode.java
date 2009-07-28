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
	private static final ImageIcon _currentBrick = loadIcon("currentBrick.png");
	private static final ImageIcon _rejectedBrick = loadIcon("rejectedBrick.png");
	
	private static final ImageIcon _newBrick = loadIcon("newBrick.png");
	private static final ImageIcon _differentBrick = loadIcon("differentBrick.png");
	private static final ImageIcon _divergingBrick = loadIcon("divergingBrick.png");

	private static final ImageIcon _addNewBrick = loadIcon("addNewBrick.png");
	private static final ImageIcon _addDifferentBrick = loadIcon("addDifferentBrick.png");
	private static final ImageIcon _addDivergingBrick = loadIcon("addDivergingBrick.png");

	private static ImageIcon loadIcon(String fileName){
		return my(ImageFactory.class).getIcon(BrickInfoTreeNode.class, fileName);
	}
	
	BrickInfoTreeNode(TreeNode parent, BrickInfo brickInfo){
		super(parent, brickInfo);
		_brickInfo = brickInfo;
		
		getIcon();
	}

	@Override public ImageIcon getIcon() {
		if(_brickInfo.status() == Status.DIFFERENT ) {
			if(Util.isBrickStagedForExecution(_brickInfo))
				return _addDifferentBrick;
			
			return _differentBrick;
		}
		
		if(_brickInfo.status() == Status.DIVERGING ){
			if(Util.isBrickStagedForExecution(_brickInfo))
				return _addDivergingBrick;

			return _divergingBrick;
		}
		
		if(_brickInfo.status() == Status.NEW ) {
			if(Util.isBrickStagedForExecution(_brickInfo))
				return _addNewBrick;

			return  _newBrick;
		}
		
		if(_brickInfo.status() == Status.REJECTED ) 
			return _rejectedBrick;
		
		return _currentBrick;
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

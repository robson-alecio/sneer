package sneer.bricks.softwaresharing.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import sneer.bricks.skin.image.ImageFactory;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class StringTreeNode extends AbstractTreeNodeWrapper<Object> {

	private final static List<Object> _empty = new ArrayList<Object>();
	private final String _name;
	
	private static final ImageIcon _users = loadIcon("users.png");

	private static ImageIcon loadIcon(String fileName){
		return my(ImageFactory.class).getIcon(BrickInfoTreeNode.class, fileName);
	}
	StringTreeNode(TreeNode parent, String name) {
		super(parent, null);
		_name = name;
	}

	@Override protected List<Object> listChildren() {return _empty;}
	@Override protected AbstractTreeNodeWrapper<Object> wrapChild(int childIndex) {throw new NotImplementedYet();}
	@Override public String toString() {return _name;	}
	@Override public ImageIcon getIcon() { return _users; }
}

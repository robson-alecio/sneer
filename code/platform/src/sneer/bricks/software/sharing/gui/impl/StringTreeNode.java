package sneer.bricks.software.sharing.gui.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import sneer.foundation.lang.exceptions.NotImplementedYet;

class StringTreeNode extends AbstractTreeNodeWrapper<Object> {

	private final static List<Object> _empty = new ArrayList<Object>();
	private final String _name;
	
	StringTreeNode(TreeNode parent, String name) {
		super(parent, null);
		_name = name;
	}

	@Override protected List<Object> listChildren() {return _empty;}
	@Override protected AbstractTreeNodeWrapper<Object> wrapChild(int childIndex) {throw new NotImplementedYet();}
	@Override public String toString() {return _name;	}
	
}

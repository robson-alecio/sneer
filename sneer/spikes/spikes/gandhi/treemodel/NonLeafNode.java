package spikes.gandhi.treemodel;

import javax.swing.tree.DefaultMutableTreeNode;

public class NonLeafNode extends DefaultMutableTreeNode{

	public NonLeafNode(Object object){
		super(object);
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}

	private static final long serialVersionUID = 1L;

}

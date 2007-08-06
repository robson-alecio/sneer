package spikes.gandhi.treemodel;

import javax.swing.tree.DefaultMutableTreeNode;

public class MyTreeNode extends DefaultMutableTreeNode{

	public MyTreeNode(Object object){
		super(object);
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}

	private static final long serialVersionUID = 1L;

}

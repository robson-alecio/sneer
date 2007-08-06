package spikes.gandhi.treemodel;

import javax.swing.tree.DefaultTreeModel;

public class MyTreeModel extends DefaultTreeModel {
	
	private static final long serialVersionUID = 1L;

	public MyTreeModel(){
		super(new MyTreeNode("root"));
	}

	

}

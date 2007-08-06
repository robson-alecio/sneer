package spikes.gandhi.treemodel;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Collections;

import javax.swing.tree.TreeNode;

public class BasicTreeNode implements TreeNode{
	private TreeNode _parent = null;
	private List<TreeNode> _children = new ArrayList<TreeNode>();
	private Object _userObject;
	
	protected void add(TreeNode node){ //should only be called by model!
		_children.add(node);
	}
	
	protected void remove(TreeNode node){ //should only be called by model!
		_children.remove(node);
	}
	
	protected int size(){
		return _children.size();
	}
	
	protected TreeNode parent(){
		return _parent;
	}

	public Enumeration children() {
		return Collections.enumeration(_children);
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public TreeNode getChildAt(int childIndex) {
		return _children.get(childIndex);
	}

	public int getChildCount() {
		return _children.size();
	}

	public int getIndex(TreeNode node) {
		return _children.indexOf(node);
	}

	public TreeNode getParent() {
		return _parent;
	}

	public boolean isLeaf() {
		return false; // at normal operation should be (getChildCount == 0)...
	}

	protected void setUserObject(Object userObject) { 
		_userObject = userObject;
	}

	protected Object getUserObject() {
		return _userObject;
	}

}

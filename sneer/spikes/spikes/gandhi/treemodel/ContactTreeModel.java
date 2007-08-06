package spikes.gandhi.treemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;

public class ContactTreeModel implements TreeModel{
	
	private ContactRootTreeNode _rootNode;
	private List<TreeModelListener> _listeners = new ArrayList<TreeModelListener>();

	public ContactTreeModel(Party me){
		buildTree(me);
	}

	private void buildTree(Party me) {
		_rootNode = new ContactRootTreeNode(me);
		for(Contact contact:me.contacts()){
			ContactFirstLevelTreeNode node = new ContactFirstLevelTreeNode(contact);
			_rootNode.add(node);
			//add receivers
			//notify treelisteners
		}
	}
	
	private void insertFirstLevel(BasicTreeNode parent, BasicTreeNode node){ 
		parent.add(node);
		//		add receivers
		//		notify treelisteners
	}
	
	public void insertThirdParty(BasicTreeNode parent, BasicTreeNode node){ 
		parent.add(node);
		//		add receivers
		//		notify treelisteners
	}
	
	public void addTreeModelListener(TreeModelListener l) {
		_listeners.add(l);
	}

	public Object getChild(Object parent, int index) {
		return ((TreeNode)parent).getChildAt(index);
	}

	public int getChildCount(Object parent) {
		return ((BasicTreeNode)parent).size();
	}

	public int getIndexOfChild(Object parent, Object child) {
		return ((BasicTreeNode)parent).getIndex((TreeNode)child);
	}

	public Object getRoot() {
		return _rootNode;
	}

	public boolean isLeaf(Object node) {
		return false; //at normal operation should return node information...
	}

	public void removeTreeModelListener(TreeModelListener l) {
		_listeners.remove(l);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		BasicTreeNode node = (BasicTreeNode) path.getLastPathComponent();
		node.setUserObject(newValue);
		//notify listeners
	}

}

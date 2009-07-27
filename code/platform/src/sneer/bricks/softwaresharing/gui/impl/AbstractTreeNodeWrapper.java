package sneer.bricks.softwaresharing.gui.impl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import sneer.foundation.lang.exceptions.NotImplementedYet;

abstract class AbstractTreeNodeWrapper<CHILD> implements TreeNode{

	private final Object _source;
	protected final TreeNode _parent;

	AbstractTreeNodeWrapper(TreeNode parent, Object source){
		_parent = parent;
		_source = source;
	}

	Object sourceObject() {return _source;}
	
	@Override public boolean getAllowsChildren() {return true;}
	@Override public TreeNode getParent() { return _parent;}
	@Override public TreeNode getChildAt(int childIndex) { return wrapChild(childIndex); }
	@Override public int getIndex(TreeNode node) {  return listChildren().indexOf(cast(node).sourceObject()); }
	@Override public int getChildCount() { return listChildren().size(); }
	@Override public boolean isLeaf() {return listChildren().size()==0 ;}
	
	@Override public Enumeration<CHILD> children() {
		return new Enumeration<CHILD>(){
			Iterator<CHILD> delegate = listChildren().iterator();
			@Override public boolean hasMoreElements() { return delegate.hasNext(); }
			@Override public CHILD nextElement() { return delegate.next(); }
		};
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractTreeNodeWrapper cast(TreeNode node) {
		try {
			return (AbstractTreeNodeWrapper)node;
		} catch (Exception e) {
			throw new NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	public abstract ImageIcon getIcon();

	protected abstract List<CHILD> listChildren();
	protected abstract AbstractTreeNodeWrapper<CHILD> wrapChild(int childIndex);
}

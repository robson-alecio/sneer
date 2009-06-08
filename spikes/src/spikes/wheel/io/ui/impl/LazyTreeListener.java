package spikes.wheel.io.ui.impl;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;

public abstract class LazyTreeListener implements TreeExpansionListener, TreeWillExpandListener {

	public LazyTreeListener(JTree tree) {
		tree.addTreeExpansionListener(this);
		tree.addTreeWillExpandListener(this);
	}

	@Override
	public void treeExpanded(TreeExpansionEvent ignored) {}

	@Override
	public void treeWillCollapse(TreeExpansionEvent ignored) {}

}

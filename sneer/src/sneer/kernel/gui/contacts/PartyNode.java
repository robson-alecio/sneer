package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultMutableTreeNode;

import sneer.kernel.pointofview.Party;

abstract class PartyNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	PartyNode(Object userObj) {
		super(userObj);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	abstract Party party();

}
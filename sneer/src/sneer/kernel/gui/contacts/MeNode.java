package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultMutableTreeNode;

import sneer.kernel.pointofview.Party;

public class MeNode extends DefaultMutableTreeNode{

	public MeNode(Party party){
		super(party);
	}

	public Party party() {
		return (Party)getUserObject();
	}
	
	@Override
	public boolean isLeaf(){
		return false;
	}
	
	private static final long serialVersionUID = 1L;
}

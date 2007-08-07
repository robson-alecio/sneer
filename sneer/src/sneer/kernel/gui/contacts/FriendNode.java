package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultMutableTreeNode;

import sneer.kernel.pointofview.Contact;

public class FriendNode extends DefaultMutableTreeNode{

	public FriendNode(Contact contact){
		super(contact);
	}
	
	public Contact contact() {
		return (Contact)getUserObject();
	}
	
	@Override
	public boolean isLeaf(){
		return false;
	}
	
	private static final long serialVersionUID = 1L;
}

package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultMutableTreeNode;

import sneer.kernel.pointofview.Contact;

public class ContactNode extends DefaultMutableTreeNode{

	public ContactNode(Contact contact){
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

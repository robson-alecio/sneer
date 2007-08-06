package spikes.gandhi.treemodel;

import sneer.kernel.pointofview.Contact;


public class ContactThirdPartyTreeNode extends BasicTreeNode{

	public ContactThirdPartyTreeNode(Contact contact){
		setUserObject(contact);
	}
	
	public Contact contact(){
		return (Contact) getUserObject();
	}

}

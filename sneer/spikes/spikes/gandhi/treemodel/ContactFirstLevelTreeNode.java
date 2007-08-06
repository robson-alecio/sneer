package spikes.gandhi.treemodel;

import sneer.kernel.pointofview.Contact;

public class ContactFirstLevelTreeNode extends BasicTreeNode{

	public ContactFirstLevelTreeNode(Contact contact){
		setUserObject(contact);
	}
	
	public Contact contact(){
		return (Contact)getUserObject();
	}

}

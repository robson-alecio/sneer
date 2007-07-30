package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultMutableTreeNode;

import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;

public class PartyTreeNode extends DefaultMutableTreeNode{
	private boolean expanded = false;
	
    public PartyTreeNode(Party party){
    	super(party);
    }
    
    @Override
	public boolean isLeaf() {
    	Party party = (Party) getUserObject();
        if (party.contacts().currentSize()==0)
        	return true;
        return false;
    }
    
    public void expand(){
    	if (expanded)
    		return;
    	Party party = (Party) getUserObject();
    	for(Contact contact:party.contacts()){
			PartyTreeNode contactNode = new PartyTreeNode(contact.party());
			add(contactNode);
		}
    }
    
    public void collapse(){
    	if (!expanded)
    		return;
    	removeAllChildren();
    }
    
    @Override
	public String toString(){
    	return ((Party) getUserObject()).toString();
    }

	private static final long serialVersionUID = 1L;

}

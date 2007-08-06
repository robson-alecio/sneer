package spikes.gandhi.treemodel;

import sneer.kernel.pointofview.Party;


public class ContactRootTreeNode extends BasicTreeNode{
	
	public ContactRootTreeNode(Party me){
		setUserObject(me);
	}
	
	public Party me(){
		return (Party) getUserObject();
	}
}

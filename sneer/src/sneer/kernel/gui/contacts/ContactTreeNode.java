package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import sneer.kernel.pointofview.Contact;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ContactTreeNode extends DefaultMutableTreeNode{
	static final DefaultMutableTreeNode NO_CONTACTS = new DefaultMutableTreeNode("No contacts.");
	private final DefaultTreeModel _model;
	
    public ContactTreeNode(Contact contact, DefaultTreeModel model){
    	super(contact);
		_model = model;

    	for (Signal<?> signal : signalsToReceiveFrom(contact))
    		addReceiverToSignal(displayedSignalsReceiver(), signal);
    	
       	add(NO_CONTACTS);
       	_model.reload(this);
    }
    
    private void registerReceiver(ListSignal<Contact> contacts) {
		new SimpleListReceiver<Contact>(contacts) {

			@Override
			protected void elementAdded(Contact newContact) {
				add(new ContactTreeNode(newContact,_model));
				
				if (getChildAt(0) == NO_CONTACTS)
					remove(NO_CONTACTS);
				
				_model.reload(ContactTreeNode.this);
			}

			@Override
			protected void elementPresent(Contact contact) {
				add(new ContactTreeNode(contact,_model));
			}

			@Override
			protected void elementToBeRemoved(Contact contactRemoved) {
				int count = getChildCount();
				for (int i = 0; i < count; i++) {
					if (((ContactTreeNode)getChildAt(i)).contact() == contactRemoved) {
						remove(i);

						if (getChildCount() == 0)
				        	add(NO_CONTACTS);

						_model.reload(ContactTreeNode.this);
						return;
					}
				}
			}

		};
	}

	private <U> Omnivore<U> displayedSignalsReceiver() {
		return new Omnivore<U>() { public void consume(U ignored) {
			_model.nodeChanged(ContactTreeNode.this);
		}};
    }

	Contact contact() {
    	return (Contact) getUserObject();
    }
    
   @Override
	public boolean isLeaf() {
    	return false;
        //return contact().party().contacts().currentSize() == 0;
    	//return getChildCount() == 0;
    }
    
    void prepareToExpand(){
    	registerReceiver(contact().party().contacts());
        if (getChildCount() == 0)
        	add(NO_CONTACTS);
    }
    
    void prepareToCollapse(){
    	removeAllChildren();
    	add(NO_CONTACTS);
    	//Fix: implement weak references for receivers to avoid leak 
    }
    
    @Override
	public String toString(){
    	return ((Contact) getUserObject()).party().toString();
    }

	private Signal<?>[] signalsToReceiveFrom(Contact contact) {
		return new Signal<?>[] {
			contact.party().isOnline(),
			contact.party().publicKeyConfirmed(),
			contact.nick(),
			contact.party().host(),
			contact.party().port()
		};
	}
    
	private <U> void addReceiverToSignal(Omnivore<?> receiver, Signal<U> signal) {
		Omnivore<U> castedReceiver = Casts.uncheckedGenericCast(receiver);
		signal.addReceiver(castedReceiver);
	}

	private static final long serialVersionUID = 1L;

}

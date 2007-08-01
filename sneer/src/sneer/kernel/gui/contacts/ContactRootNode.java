package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListValueChange;

public class ContactRootNode extends DefaultMutableTreeNode {

	private final DefaultTreeModel _model = new DefaultTreeModel(this);
	private final Party _I;
	
	public ContactRootNode(Party I) {
		super(I);
		_I = I;
		for(Contact contact:_I.contacts())
			add(new ContactTreeNode(contact,_model));
		
		for (Signal<?> signal : signalsToReceiveFromI())
    		addReceiverToSignal(displayedSignalsReceiver(), signal);
    	
    	_I.contacts().addListReceiver(contactsListReceiver());
       	
	}
	
	private Omnivore<ListValueChange> contactsListReceiver() {
		return new Omnivore<ListValueChange>() { public void consume(ListValueChange ignored) {
			_model.reload(ContactRootNode.this);
		}};
	}

	private <U> Omnivore<U> displayedSignalsReceiver() {
		return new Omnivore<U>() { public void consume(U ignored) {
			_model.nodeChanged(ContactRootNode.this);
		}};
    }

	public TreeModel model() {
		return _model;
	}
	
	private Signal<?>[] signalsToReceiveFromI() {
		return new Signal<?>[] {
			_I.isOnline(),
			_I.publicKeyConfirmed(),
			_I.host(),
			_I.port()
		};
	}
    
	private <U> void addReceiverToSignal(Omnivore<?> receiver, Signal<U> signal) {
		Omnivore<U> castedReceiver = Casts.uncheckedGenericCast(receiver);
		signal.addReceiver(castedReceiver);
	}

	private static final long serialVersionUID = 1L;
}

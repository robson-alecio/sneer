package sneer.kernel.gui.contacts;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import sneer.kernel.pointofview.Contact;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ContactTreeController {
	
	private final DefaultTreeModel _model;

	private final Map<ContactNode, Omnivore<Object>> _displayReceiversByNode = new HashMap<ContactNode,Omnivore<Object>>();
	private final Map<PartyNode, SimpleListReceiver<Contact>> _contactListReceiversByNode = new HashMap<PartyNode,SimpleListReceiver<Contact>>();
	

	public ContactTreeController(JTree tree, DefaultTreeModel model) {
		_model = model;
		prepareToExpand((PartyNode)_model.getRoot());
		registerExpansionListeners(tree);
	}

	private void createContactNode(MutableTreeNode parent, Contact contact) {
		ContactNode node =  new ContactNode(contact);
		_model.insertNodeInto(node, parent, 0);
		startReceiving(node);
	}

	private void registerExpansionListeners(JTree tree) {
		tree.addTreeExpansionListener(collapseListener());
		tree.addTreeWillExpandListener(willExpandListener());
	}

	private TreeWillExpandListener willExpandListener() { return new TreeWillExpandListener(){
		public void treeWillCollapse(TreeExpansionEvent ignored) {}
		
		public void treeWillExpand(TreeExpansionEvent event) {
			prepareToExpand((PartyNode)event.getPath().getLastPathComponent());
		}};
	}

	private void prepareToExpand(PartyNode node) {
		SimpleListReceiver<Contact> contactListReceiver = registerContactListReceiver(node);
    	_contactListReceiversByNode.put(node, contactListReceiver);
	}

	private TreeExpansionListener collapseListener() { return new TreeExpansionListener(){
		public void treeExpanded(TreeExpansionEvent ignored)  {}
		
		public void treeCollapsed(TreeExpansionEvent event) {
			nodeCollapsed((PartyNode)event.getPath().getLastPathComponent());
		}};
	}
	
	private void nodeCollapsed(PartyNode node) {
		crashContactListReceiver(node);

		while (_model.getChildCount(node) != 0) {
			ContactNode child = (ContactNode) _model.getChild(node, 0);
			remove(child);
		}
		
	}

	private void remove(ContactNode child) {
		nodeCollapsed(child);
		stopReceiving(child);
		_model.removeNodeFromParent(child);
	}

	private void startReceiving(ContactNode friend) {
		Omnivore<Object> displayReceiver = displaySignalReceiver(friend);
    	for (Signal<?> signal : signalsToReceiveFrom(friend.contact())){
    		addReceiverToSignal(displayReceiver, signal);
    	}
    	_displayReceiversByNode.put(friend, displayReceiver);
    }
	
    
    private Omnivore<Object> displaySignalReceiver(final PartyNode friend) {
		return new Omnivore<Object>() { public void consume(Object ignored) {
			if (SwingUtilities.isEventDispatchThread()) return; //FixUrgent Model does not have to be notified when the receiver is first added and the receiver is first added in the awt thread. VERY OBSCURE!
				
			runBlockThatChangesModel(new Runnable(){ public void run(){
				_model.nodeChanged(friend);
			}});
		}};
	}

	private void stopReceiving(ContactNode node){
		Omnivore<Object> receiver = _displayReceiversByNode.remove(node);
		if (receiver == null) return;
    	for (Signal<?> signal : signalsToReceiveFrom(node.contact()))
    		removeReceiverFromSignal(receiver, signal);
    }

	private void crashContactListReceiver(PartyNode node) {
		SimpleListReceiver<Contact> contactListReceiver = _contactListReceiversByNode.remove(node);
		if (contactListReceiver == null) return;
		contactListReceiver.crash();
	}
    
	private <U> void addReceiverToSignal(Omnivore<?> receiver, Signal<U> signal) {
		Omnivore<U> castedReceiver = Casts.uncheckedGenericCast(receiver);
		signal.addReceiver(castedReceiver);
	}
	
	private <U> void removeReceiverFromSignal(Omnivore<?> receiver, Signal<U> signal) {
		Omnivore<U> castedReceiver = Casts.uncheckedGenericCast(receiver);
		signal.removeReceiver(castedReceiver);
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

 
	private SimpleListReceiver<Contact> registerContactListReceiver(final PartyNode node) {
		
		return new SimpleListReceiver<Contact>(node.party().contacts()) {

			@Override
			protected void elementAdded(final Contact newContact) {
				runBlockThatChangesModel(new Runnable(){ public void run(){
					createContactNode(node, newContact);
				}});
			}

			@Override
			protected void elementPresent(Contact contact) {
				createContactNode(node, contact);
			}

			@Override
			protected void elementToBeRemoved(final Contact contactRemoved) {
				runBlockThatChangesModel(new Runnable(){ public void run(){
					int count = node.getChildCount();
					for (int i = 0; i < count; i++) {
						ContactNode candidate = (ContactNode)node.getChildAt(i);
						if (candidate.contact() == contactRemoved) {
							remove(candidate);
							return;
						}
					}
				}});
			}

		};
	}
	
	public void runBlockThatChangesModel(final Runnable runnable){
		SwingUtilities.invokeLater(runnable);
	}
	
}

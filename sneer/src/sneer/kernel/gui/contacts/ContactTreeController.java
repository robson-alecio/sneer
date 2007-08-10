package sneer.kernel.gui.contacts;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import sneer.kernel.pointofview.Contact;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ContactTreeController {
	
	private final DefaultTreeModel _model;

	public ContactTreeController(JTree tree, DefaultTreeModel model){
		_model = model;
		buildFirstLevel();
		registerExpansionListeners(tree);
	}

	private void buildFirstLevel() {
		MeNode me = (MeNode)_model.getRoot();
		for(Contact contact:me.party().contacts())
			createContactNode(me, contact);
	}

	private void createContactNode(DefaultMutableTreeNode parent, Contact contact) {
		ContactNode node =  new ContactNode(contact);
		_model.insertNodeInto(node, parent, 0);
		startReceiving(node);
	}

	private void registerExpansionListeners(JTree tree) {
		tree.addTreeExpansionListener(collapseListener());
		tree.addTreeWillExpandListener(willExpandListener());
	}

	private TreeWillExpandListener willExpandListener() { return new TreeWillExpandListener(){
		public void treeWillCollapse(TreeExpansionEvent ignored) { }
		public void treeWillExpand(TreeExpansionEvent event) {
			if (ignoredNode(event.getPath())) return;
			
			expandFriendNode((ContactNode)event.getPath().getLastPathComponent());
			
		}};
	}

	private void expandFriendNode(final ContactNode friend) {
		for(Contact contact:friend.contact().party().contacts())
			createContactNode(friend,contact);
	}

	private TreeExpansionListener collapseListener() { return new TreeExpansionListener(){
		public void treeExpanded(TreeExpansionEvent ignored)  { }
		public void treeCollapsed(TreeExpansionEvent event) {
			if (ignoredNode(event.getPath())) return;
			
			collapseFriendNode((ContactNode)event.getPath().getLastPathComponent());
		}};
	}
	
	private void collapseFriendNode(final ContactNode friend) {
		while (_model.getChildCount(friend) != 0) {
			ContactNode child = (ContactNode) _model.getChild(friend, 0);
			removeFriendNode(child);
		}
	}

	private void removeFriendNode(ContactNode child) {
		stopReceiversRecursively(child);
		_model.removeNodeFromParent(child); //dont worry about subtree, it will be garbage collected
	}
	
	private void stopReceiversRecursively(ContactNode friend){
		for(int t=friend.getChildCount()-1;t>=0;t--){
			ContactNode child = (ContactNode)friend.getChildAt(t);
			stopReceiversRecursively(child);
		}
		stopReceiving(friend);
	}

	private boolean ignoredNode(TreePath path) {
		return path.getLastPathComponent() instanceof MeNode;
	}
	
	private Hashtable<ContactNode,Omnivore<Object>> _displayReceiversByFriend = new Hashtable<ContactNode,Omnivore<Object>>();

	private void startReceiving(ContactNode friend){
		Omnivore<Object> displayReceiver = displaySignalReceiver(friend);
    	for (Signal<?> signal : signalsToReceiveFrom(friend.contact())){
    		addReceiverToSignal(displayReceiver, signal);
    	}
    	_displayReceiversByFriend.put(friend, displayReceiver);
    	SimpleListReceiver<Contact> contactListReceiver = registerContactListReceiver(friend);
    	_contactListReceiversByFriend.put(friend, contactListReceiver);
    }
	
    
    private Omnivore<Object> displaySignalReceiver(final ContactNode friend) {
		return new Omnivore<Object>(){
			public void consume(Object ignored) {
				runBlockThatChangesModel(new Runnable(){ public void run(){
					_model.nodeChanged(friend);
				}});
			}
		};
	}

	private void stopReceiving(ContactNode friend){
		SimpleListReceiver<Contact> contactListReceiver = _contactListReceiversByFriend.remove(friend);
		if (contactListReceiver != null)
			contactListReceiver.stopReceiving();
		Omnivore<Object> receiver = _displayReceiversByFriend.remove(friend);
		if (receiver == null) return;
    	for (Signal<?> signal : signalsToReceiveFrom(friend.contact()))
    		removeReceiverFromSignal(receiver, signal);
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
	
	private Hashtable<ContactNode,SimpleListReceiver<Contact>> _contactListReceiversByFriend = new Hashtable<ContactNode,SimpleListReceiver<Contact>>();

 
	private SimpleListReceiver<Contact> registerContactListReceiver(final ContactNode friend) {
		return new SimpleListReceiver<Contact>(friend.contact().party().contacts()) {

			@Override
			protected void elementAdded(final Contact newContact) {
				runBlockThatChangesModel(new Runnable(){ public void run(){
					createContactNode(friend, newContact);
				}});
			}

			@Override
			protected void elementPresent(Contact contact) {
				// not needed, when the tree is expanded, the current childs are added.
				// it's receiver's responsability to continue updating it.
			}

			@Override
			protected void elementToBeRemoved(final Contact contactRemoved) {
				runBlockThatChangesModel(new Runnable(){ public void run(){
					int count = friend.getChildCount();
					for (int i = 0; i < count; i++) {
						ContactNode child = (ContactNode)friend.getChildAt(i);
						if (child.contact() == contactRemoved) {
							removeFriendNode(child);
							return;
						}
					}
				}});
			}

		};
	}
	
	private MyConsumer _consumer = new MyConsumer();
	
	public void runBlockThatChangesModel(final Runnable runnable){
		_consumer.add(runnable);
	}
	
	private List<Runnable> _buffer = new LinkedList<Runnable>();
	
	public class MyConsumer extends Thread{
		
		public MyConsumer(){
			setDaemon(true);
			start();
		}
		
		@Override
		public void run(){
			while(true){
				synchronized(_buffer){
					if (_buffer.isEmpty())
						Threads.waitWithoutInterruptions(_buffer);
					final Runnable runnable = _buffer.remove(0); //can't be inside invokelater (executed in eventqueue thread)
					SwingUtilities.invokeLater(runnable);
				}
			}
		}
		
		public void add(Runnable runnable){
			synchronized (_buffer) {
				_buffer.add(runnable);
				_buffer.notify();
			}
		}
	}
	
}

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
	
	private final JTree _tree;
	private final DefaultTreeModel _model;

	public ContactTreeController(JTree tree, DefaultTreeModel model){
		_tree = tree;
		_model = model;
		buildFirstLevel();
		registerListeners();
	}

	private void buildFirstLevel() {
		MeNode me = (MeNode)_model.getRoot();
		for(Contact contact:me.party().contacts()){
			createFriendNode(me, contact);
		}
	}

	private void createFriendNode(final DefaultMutableTreeNode parent, final Contact contact) {
		runBlockThatChangesModel(new Runnable(){ public void run(){
			unsynchronizedCreateFriendNode(parent,contact);
		}});
	}
	
	private void unsynchronizedCreateFriendNode(DefaultMutableTreeNode parent, Contact contact) {
			FriendNode friend =  new FriendNode(contact);
			_model.insertNodeInto(friend, parent, 0);
			startReceiving(friend);
	}

	private void registerListeners() {
		_tree.addTreeExpansionListener(expansionListener());
		_tree.addTreeWillExpandListener(willExpandListener());
	}

	private TreeWillExpandListener willExpandListener() { return new TreeWillExpandListener(){
		public void treeWillCollapse(TreeExpansionEvent ignored) { }
		public void treeWillExpand(TreeExpansionEvent event) {
			if (ignoredNode(event.getPath())) return;
			
			expandFriendNode((FriendNode)event.getPath().getLastPathComponent());
			
		}};
	}

	private void expandFriendNode(final FriendNode friend) {
		runBlockThatChangesModel(new Runnable(){ public void run(){
			for(Contact contact:friend.contact().party().contacts()){
				unsynchronizedCreateFriendNode(friend,contact);
			}
		}});
	}

	private TreeExpansionListener expansionListener() { return new TreeExpansionListener(){
		public void treeExpanded(TreeExpansionEvent ignored)  { }
		public void treeCollapsed(TreeExpansionEvent event) {
			if (ignoredNode(event.getPath())) return;
			
			collapseFriendNode((FriendNode)event.getPath().getLastPathComponent());
		}};
	}
	
	private void collapseFriendNode(final FriendNode friend) {
		runBlockThatChangesModel(new Runnable(){ public void run(){
			for(int t=_model.getChildCount(friend)-1;t>=0;t--){
				FriendNode child = (FriendNode) _model.getChild(friend,t);
				unsynchronizedRemoveFriendNode(child);
			}
		}});
	}

	//private void removeFriendNode(FriendNode child) {
	//	synchronized(_synchronizeAnyModelAccess){
	//		unsynchronizedRemoveFriendNode(child);
	//	}
	//}
	
	private void unsynchronizedRemoveFriendNode(FriendNode child) {
			stopReceiversRecursively(child);
			_model.removeNodeFromParent(child); //dont worry about subtree, it will be garbage collected
	}
	
	private void stopReceiversRecursively(FriendNode friend){
		for(int t=friend.getChildCount()-1;t>=0;t--){
			FriendNode child = (FriendNode)friend.getChildAt(t);
			stopReceiversRecursively(child);
		}
		stopReceiving(friend);
	}

	private boolean ignoredNode(TreePath path) {
		return path.getLastPathComponent() instanceof MeNode;
	}
	
	private Hashtable<FriendNode,Omnivore<Object>> _displayReceiversByFriend = new Hashtable<FriendNode,Omnivore<Object>>();

	private void startReceiving(FriendNode friend){
		Omnivore<Object> displayReceiver = displaySignalReceiver(friend);
    	for (Signal<?> signal : signalsToReceiveFrom(friend.contact())){
    		addReceiverToSignal(displayReceiver, signal);
    	}
    	_displayReceiversByFriend.put(friend, displayReceiver);
    	SimpleListReceiver<Contact> contactListReceiver = registerContactListReceiver(friend);
    	_contactListReceiversByFriend.put(friend, contactListReceiver);
    }
	
    
    private Omnivore<Object> displaySignalReceiver(final FriendNode friend) {
		return new Omnivore<Object>(){
			public void consume(Object ignored) {
				runBlockThatChangesModel(new Runnable(){ public void run(){
					_model.nodeChanged(friend);
				}});
			}
		};
	}

	private void stopReceiving(FriendNode friend){
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
	
	private Hashtable<FriendNode,SimpleListReceiver<Contact>> _contactListReceiversByFriend = new Hashtable<FriendNode,SimpleListReceiver<Contact>>();

 
	private SimpleListReceiver<Contact> registerContactListReceiver(final FriendNode friend) {
		return new SimpleListReceiver<Contact>(friend.contact().party().contacts()) {

			@Override
			protected void elementAdded(Contact newContact) {
				createFriendNode(friend, newContact);
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
						FriendNode child = (FriendNode)friend.getChildAt(i);
						if (child.contact() == contactRemoved) {
							unsynchronizedRemoveFriendNode(child);
							return;
						}
					}
				}});
			}

		};
	}
	
	private Object _syncObject = new Object();
	
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
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							synchronized(_syncObject){
								runnable.run();
							}
						}
					});
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

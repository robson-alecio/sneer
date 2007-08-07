package sneer.kernel.gui.contacts;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import sneer.kernel.pointofview.Contact;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

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

	private void createFriendNode(DefaultMutableTreeNode parent, Contact contact) {
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

	private void expandFriendNode(FriendNode friend) {
		for(Contact contact:friend.contact().party().contacts()){
			createFriendNode(friend,contact);
		}
		_model.reload(friend);
	}

	private TreeExpansionListener expansionListener() { return new TreeExpansionListener(){
		public void treeExpanded(TreeExpansionEvent ignored)  { }
		public void treeCollapsed(TreeExpansionEvent event) {
			if (ignoredNode(event.getPath())) return;
			
			collapseFriendNode((FriendNode)event.getPath().getLastPathComponent());
		}};
	}
	
	private void collapseFriendNode(FriendNode friend) {
		for(int t=_model.getChildCount(friend)-1;t>=0;t--){
			FriendNode child = (FriendNode) _model.getChild(friend,t);
			stopReceiversRecursively(child);
			_model.removeNodeFromParent(child); //dont worry about subtree, it will be garbage collected  
		}
		_model.reload(friend);
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
	

	private void startReceiving(FriendNode friend){
    	for (Signal<?> signal : signalsToReceiveFrom(friend.contact())){
    		addReceiverToSignal(new DisplaySignalReceiver(_model,friend), signal);
    	}
    }
    
    private void stopReceiving(FriendNode friend){
    	for (Signal<?> signal : signalsToReceiveFrom(friend.contact()))
    		removeReceiverFromSignal(new DisplaySignalReceiver(_model,friend), signal);
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
}

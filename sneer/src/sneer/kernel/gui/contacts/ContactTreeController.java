package sneer.kernel.gui.contacts;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import sneer.kernel.pointofview.Contact;
import wheel.io.ui.impl.LazyTreeListener;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ContactTreeController { //Refactor Extract a generic ReactiveTreeController from this which receives a Functor of Node->ListSignal (children) and a Functor of Node->Signals (rendered for each node). 
	
	private final DefaultTreeModel _model;

	private final Map<PartyNode, Omnivore<Object>> _renderReceiversByNode = new HashMap<PartyNode,Omnivore<Object>>();
	private final Map<PartyNode, SimpleListReceiver<Contact>> _contactListReceiversByNode = new HashMap<PartyNode,SimpleListReceiver<Contact>>();

	private final ContactTreeCellRenderer _renderer;

	public ContactTreeController(JTree tree, MeNode root, Signal<Font> font) {
		_model = new DefaultTreeModel(root);
		_renderer = new ContactTreeCellRenderer(font);
		tree.setModel(_model);
		tree.setCellRenderer(_renderer);
		tree.setShowsRootHandles(true);

		prepareToExpand(root);
		registerExpansionListener(tree);
		
		startReceiving(root);
		font.addReceiver(fontReceiver());
	}

	private Omnivore<Font> fontReceiver() {
		return new Omnivore<Font>(){ public void consume(Font valueObject) {
			_model.reload();
		}};
	}

	private void createContactNode(MutableTreeNode parent, Contact contact) {
		ContactNode node =  new ContactNode(contact);
		_model.insertNodeInto(node, parent, 0);
		startReceiving(node);
	}

	private void registerExpansionListener(JTree tree) {
		new LazyTreeListener(tree) {

			public void treeCollapsed(TreeExpansionEvent event) {
				nodeCollapsed((PartyNode)event.getPath().getLastPathComponent());
			}

			public void treeWillExpand(TreeExpansionEvent event) {
				prepareToExpand((PartyNode)event.getPath().getLastPathComponent());
			}
		};
	}

	private void prepareToExpand(PartyNode node) {
		SimpleListReceiver<Contact> contactListReceiver = registerContactListReceiver(node);
    	_contactListReceiversByNode.put(node, contactListReceiver);
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

	private void startReceiving(PartyNode node) {
		Omnivore<Object> renderReceiver = renderReceiver(node);
    	for (Signal<?> signal : _renderer.signalsToReceiveFrom(node))
    		addReceiverToSignal(renderReceiver, signal);

    	_renderReceiversByNode.put(node, renderReceiver);
    }
	
    
    private Omnivore<Object> renderReceiver(final PartyNode node) {
		return new Omnivore<Object>() { public void consume(Object ignored) {
			runBlockThatChangesModel(new Runnable() { public void run(){
				_model.nodeChanged(node);
			}});
		}};
	}

	private void stopReceiving(PartyNode node){
		Omnivore<Object> receiver = _renderReceiversByNode.remove(node);
		if (receiver == null) return;
    	for (Signal<?> signal : _renderer.signalsToReceiveFrom(node))
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

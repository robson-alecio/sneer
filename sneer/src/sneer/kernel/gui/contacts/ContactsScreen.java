package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo2;
import sneer.kernel.gui.NewContactAddition;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;

class ContactsScreen extends JFrame {

	private final User _user;
	private final Party _I;
	private final List<ContactAction> _contactActions;
	private final Consumer<ContactInfo2> _contactAdder;
	private final Omnivore<ContactId> _contactRemover;
	private final Consumer<Pair<ContactId, String>> _nickChanger;


	ContactsScreen(User user, Party I, List<ContactAction> contactActions, Consumer<ContactInfo2> contactAdder, Omnivore<ContactId> contactRemover, Consumer<Pair<ContactId, String>> nickChanger) {
		_user = user;
		_I = I;
		_contactActions = contactActions;
		_contactAdder = contactAdder;
		_contactRemover = contactRemover;
		_nickChanger = nickChanger;

		initComponents();
		setVisible(true);
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());

		final JTextField nameText = new JTextField();
		makeNameTextTheFirstFocusComponent(nameText);
		
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new BorderLayout());
		//editPanel.add(nameText, BorderLayout.CENTER);
		editPanel.add(createAddButton(), BorderLayout.EAST);
		JScrollPane scrollpane = new JScrollPane(createFriendsTree());
		scrollpane.setBackground(java.awt.Color.black);
		this.add(scrollpane, BorderLayout.CENTER);
		this.add(editPanel, BorderLayout.SOUTH);

		setTitle(translate("Contacts"));
		setSize(200, 400);
	}

	private void makeNameTextTheFirstFocusComponent(
			final JTextField nameText) {
		this.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy(){
			@Override
			public Component getFirstComponent(Container arg0) {
				return nameText;
			}
		
			private static final long serialVersionUID = 1L;});
	}

	/*private JList createFriendsList() {
		final ListSignalModel<Contact> friendsListModel = new ListSignalModel<Contact>(_I.contacts(), signalChooser());
		final JList friendsList = new JList(friendsListModel);
		friendsList.setBackground(java.awt.Color.black);
		friendsList.setCellRenderer(new ContactCellRenderer());
		
		friendsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				final boolean rightClick = mouseEvent.getButton() == MouseEvent.BUTTON3;
				if (rightClick){
					int indexUnderMouse = friendsList.locationToIndex(mouseEvent.getPoint());
					if (indexUnderMouse == -1)
						return;
					
					friendsList.setSelectedIndex(indexUnderMouse);
					
					getFriendPopUpMenu(friendsList)
						.show(friendsList, mouseEvent.getX(), mouseEvent.getY());
				}
			}
		});
		
		return friendsList;
	}*/
	
	private JTree createFriendsTree() {
		ContactRootNode root = new ContactRootNode(_I);
		TreeModel model = root.model();
		
		final JTree tree = new JTree(model);
		tree.setCellRenderer(new ContactTreeCellRenderer());
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.addTreeWillExpandListener(new TreeWillExpandListener(){
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
				TreePath path = event.getPath();
				if (path.getLastPathComponent() instanceof ContactRootNode)
					throw new ExpandVetoException(event, "Root node collapse not allowed");
				ContactTreeNode partyNode = (ContactTreeNode)path.getLastPathComponent();
				if (partyNode!=null){
					partyNode.prepareToCollapse();
				}
			}
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				TreePath path = event.getPath();
				if (path.getLastPathComponent() instanceof ContactRootNode)
					throw new ExpandVetoException(event, "Root node expansion not allowed");
				ContactTreeNode partyNode = (ContactTreeNode)path.getLastPathComponent();
				if (partyNode!=null){
					partyNode.prepareToExpand();
				}
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				final boolean rightClick = mouseEvent.getButton() == MouseEvent.BUTTON3;
				if (!rightClick) return;

				TreePath path = tree.getPathForLocation(mouseEvent.getX(),mouseEvent.getY());
					
				ContactTreeNode partyNode = (ContactTreeNode)path.getLastPathComponent();
				if (partyNode==null) return;
					
				getFriendPopUpMenu(partyNode).show(tree, mouseEvent.getX(), mouseEvent.getY());
			}
		});
		return tree;
	}

	private JPopupMenu getFriendPopUpMenu(final ContactTreeNode partyTreeNode) {
		final JPopupMenu result = new JPopupMenu();
		addToContactMenu(result, nickChangeAction(), partyTreeNode);
		for (ContactAction action : _contactActions) addToContactMenu(result, action, partyTreeNode);
		addToContactMenu(result, new ContactRemovalAction(_contactRemover), partyTreeNode);
		return result;
	}

	private ContactNickChangeAction nickChangeAction() {
		return new ContactNickChangeAction(_user, _nickChanger);
	}

	private void addToContactMenu(JPopupMenu menu, final ContactAction action, final ContactTreeNode partyTreeNode) {
		final JMenuItem item = new JMenuItem(action.caption());
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ignored) {
				
				new Thread(){
					@Override
					public void run() {
						partyTreeNode.toString(); //avoids unused warning
						//Party party = (Party)partyTreeNode.getUserObject();
						//Contact contact = party.; //Fix: how to get contact from party?
						//action.actUpon(contact);
						JOptionPane.showMessageDialog(null, "NOT IMPLEMENTED YET!!! How to get the contact that contains this party?");
					}
				}.start();
			}
		});
		
		menu.add(item);
	}

	private JButton createAddButton() {
		JButton addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					new NewContactAddition(_user, _contactAdder);
				} catch (CancelledByUser e1) {
					//Fair enough.
				}
			}

		});
		return addButton;
	}
	
	private static final long serialVersionUID = 1L;
	
}
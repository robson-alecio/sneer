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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo2;
import sneer.kernel.gui.NewContactAddition;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.Threads;

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
	
	private JTree createFriendsTree() {
		MeNode me = new MeNode(_I);
		DefaultTreeModel model = new DefaultTreeModel(me);
		final JTree tree = new JTree(model);
		tree.setCellRenderer(new ContactTreeCellRenderer());
		tree.setShowsRootHandles(true);
		
		new ContactTreeController(tree,model);
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				final boolean rightClick = mouseEvent.getButton() == MouseEvent.BUTTON3;
				if (!rightClick) return;

				TreePath path = tree.getPathForLocation(mouseEvent.getX(),mouseEvent.getY());
					
				FriendNode node = (FriendNode)path.getLastPathComponent();
				if (node==null) return;
					
				getFriendPopUpMenu(node).show(tree, mouseEvent.getX(), mouseEvent.getY());
			}
		});
		
		return tree;
	}

	private JPopupMenu getFriendPopUpMenu(final FriendNode node) {
		final JPopupMenu result = new JPopupMenu();
		addToContactMenu(result, infoAction(), node);
		for (ContactAction action : _contactActions) addToContactMenu(result, action, node);
		addToContactMenu(result, new ContactRemovalAction(_contactRemover), node);
		return result;
	}

	private ContactAction infoAction() {
		return new ContactAction(){
			public void actUpon(Contact contact) {
				new InfoPanel(contact, _nickChanger);
			}
			public String caption() {
				return translate("Info");
			}
			
		};
	}

	private void addToContactMenu(JPopupMenu menu, final ContactAction action, final FriendNode node) {
		final JMenuItem item = new JMenuItem(action.caption());
		item.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ignored) {
			Threads.startDaemon(new Runnable() { @Override public void run() {
				action.actUpon(node.contact());
			}});
		}});
		
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
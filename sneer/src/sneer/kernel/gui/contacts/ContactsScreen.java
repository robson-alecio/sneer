package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
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
import javax.swing.JTree;
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
	private final Party _me;
	private final List<ContactAction> _contactActions;
	private final Consumer<ContactInfo2> _contactAdder;
	private final Omnivore<ContactId> _contactRemover;
	private final Consumer<Pair<ContactId, String>> _nickChanger;


	ContactsScreen(User user, Party me, List<ContactAction> contactActions, Consumer<ContactInfo2> contactAdder, Omnivore<ContactId> contactRemover, Consumer<Pair<ContactId, String>> nickChanger) {
		_user = user;
		_me = me;
		_contactActions = contactActions;
		_contactAdder = contactAdder;
		_contactRemover = contactRemover;
		_nickChanger = nickChanger;

		initComponents();
		setVisible(true);
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());

		JPanel editPanel = new JPanel();
		editPanel.setLayout(new BorderLayout());
		editPanel.add(createAddButton(), BorderLayout.EAST);
		JScrollPane scrollpane = new JScrollPane(createFriendsTree());
		scrollpane.setBackground(java.awt.Color.black);
		this.add(scrollpane, BorderLayout.CENTER);
		this.add(editPanel, BorderLayout.SOUTH);

		setTitle(translate("Contacts"));
		setSize(200, 400);
	}

	
	private JTree createFriendsTree() {
		final JTree tree = new JTree();
		new ContactTreeController(tree, new MeNode(_me));
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				final boolean rightClick = mouseEvent.getButton() == MouseEvent.BUTTON3;
				if (!rightClick) return;

				TreePath path = tree.getPathForLocation(mouseEvent.getX(),mouseEvent.getY());
					
				Object uncasted = path.getLastPathComponent();
				if (!(uncasted instanceof ContactNode)) return;
				
				ContactNode node = (ContactNode)uncasted;
				if (node==null) return;
					
				getFriendPopUpMenu(node).show(tree, mouseEvent.getX(), mouseEvent.getY());
			}
		});
		
		return tree;
	}

	private JPopupMenu getFriendPopUpMenu(final ContactNode node) {
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

	private void addToContactMenu(JPopupMenu menu, final ContactAction action, final ContactNode node) {
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

			public void actionPerformed(ActionEvent ignored) {
				try {
					new NewContactAddition(_user, _contactAdder);
				} catch (CancelledByUser cbu) {
					//Fair enough.
				}
			}

		});
		return addButton;
	}
	
	private static final long serialVersionUID = 1L;
	
}
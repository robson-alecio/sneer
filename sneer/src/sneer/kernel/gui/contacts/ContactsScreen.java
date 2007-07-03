package sneer.kernel.gui.contacts;

import static sneer.Language.translate;

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
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.gui.NewContactAddition;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.io.ui.impl.ListSignalModel;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;

public class ContactsScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private final ListSignal<Contact> _contacts;
	private final Consumer<ContactInfo> _contactAdder;
	private final User _user;
	private final List<ContactAction> _contactActions;
	private final Omnivore<ContactId> _contactRemover;


	public ContactsScreen(User user, ListSignal<Contact> contacts, Consumer<ContactInfo> contactAdder, Omnivore<ContactId> contactRemover, List<ContactAction> contactActions) {

		_user = user;
		_contacts = contacts;
		_contactAdder = contactAdder;
		_contactRemover = contactRemover;
		_contactActions = contactActions;

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

		this.add(new JScrollPane(createFriendsList()), BorderLayout.CENTER);
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

	private JList createFriendsList() {
		final ListSignalModel friendsListModel = new ListSignalModel(new ContactListPrinter(_contacts).output());
		final JList friendsList = new JList(friendsListModel);
		
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
	}

	private JPopupMenu getFriendPopUpMenu(final JList friendsList) {
		final JPopupMenu result = new JPopupMenu();
		addToContactMenu(result, new ContactRemovalAction(_contactRemover), friendsList);
		for (ContactAction action : _contactActions) addToContactMenu(result, action, friendsList);
		return result;
	}

	private void addToContactMenu(JPopupMenu menu, final ContactAction action, final JList friendsList) {
		final JMenuItem item = new JMenuItem(action.caption());
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ignored) {
				Contact contact = _contacts.currentGet(friendsList.getSelectedIndex());
				action.actUpon(contact);
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
	
}
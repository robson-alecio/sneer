package sneer.kernel.gui.contacts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sneer.apps.messages.ChatEvent;
import sneer.apps.messages.gui.ChatScreen;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.gui.NewContactAddition;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.io.ui.impl.ListSignalModel;
import wheel.lang.Consumer;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.lists.ListSignal;

public class ContactsScreen extends JFrame {

	private static final String TITLE = "Amigos"; //Refactor: Inline all constants. Use debian i18n scheme.
	private static final String ADD_FRIEND_BUTTON_TEXT = "+";
	private static final String FRIEND_MENU_REMOVE_TEXT = "Delete";
	private static final String FRIEND_MENU_CHAT_TEXT = "Chat";

	private static final long serialVersionUID = 1L;
	private final ListSignal<Contact> _contacts;
	private final Consumer<ContactInfo> _contactAdder;
	private final User _user;
	private final Consumer<ChatEvent> _chatSender;


	public ContactsScreen(User user, ListSignal<Contact> contacts, Consumer<ContactInfo> contactAdder, Consumer<ChatEvent> sender) {

		if (contacts == null) throw new IllegalArgumentException();
		if (contactAdder == null) throw new IllegalArgumentException();
		if (user == null) throw new IllegalArgumentException();
		
		_contacts = contacts;
		_contactAdder = contactAdder;
		_user = user;
		_chatSender = sender;

		initComponents();
		setVisible(true);
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		
		final JTextField nameText = new JTextField();
		makeNameTextTheFirstFocusComponent(nameText);
		
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new BorderLayout());
		editPanel.add(nameText, BorderLayout.CENTER);
		editPanel.add(createAddButton(), BorderLayout.EAST);

		this.add(new JScrollPane(createFriendsList()), BorderLayout.CENTER);
		this.add(editPanel, BorderLayout.SOUTH);

		setTitle(TITLE);
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
		final JPopupMenu friendMenu = new JPopupMenu();
		friendMenu.add(getRemoveFriendMenuItem());
		friendMenu.add(getChatMenuItem(friendsList));
		return friendMenu;
	}

	private JMenuItem getRemoveFriendMenuItem() {
		final JMenuItem removeFriendMenuItem = new JMenuItem(FRIEND_MENU_REMOVE_TEXT);
		removeFriendMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ignored) {
				throw new NotImplementedYet(); //Implement
			}
		});
		return removeFriendMenuItem;
	}
	
	private JMenuItem getChatMenuItem(final JList friendsList) {
		final JMenuItem chatFriendMenuItem = new JMenuItem(FRIEND_MENU_CHAT_TEXT);
		chatFriendMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ignored) {
				Contact contact = _contacts.currentGet(friendsList.getSelectedIndex());
				new ChatScreen(contact.nick(), contact.chatEventsPending(), _chatSender);
			}
		});
		return chatFriendMenuItem;
	}

	private JButton createAddButton() {
		JButton addButton = new JButton(ADD_FRIEND_BUTTON_TEXT);
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
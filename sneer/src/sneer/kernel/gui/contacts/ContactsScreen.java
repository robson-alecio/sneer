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
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo2;
import sneer.kernel.gui.NewContactAddition;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.io.ui.impl.ListSignalModel;
import wheel.io.ui.impl.ListSignalModel.SignalChooser;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.Signal;

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
		JScrollPane scrollpane = new JScrollPane(createFriendsList());
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

	private JList createFriendsList() {
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
	}

	private SignalChooser<Contact> signalChooser() {
		return new SignalChooser<Contact>(){
			public Signal<?>[] signalsToReceiveFrom(Contact contact) {
				return new Signal<?>[] {
						contact.party().isOnline(),
						contact.party().publicKeyConfirmed(),
						contact.nick(),
						contact.party().host(),
						contact.party().port()
				};
			}};

	}

	private JPopupMenu getFriendPopUpMenu(final JList friendsList) {
		final JPopupMenu result = new JPopupMenu();
		addToContactMenu(result, nickChangeAction(), friendsList);
		for (ContactAction action : _contactActions) addToContactMenu(result, action, friendsList);
		addToContactMenu(result, new ContactRemovalAction(_contactRemover), friendsList);
		return result;
	}

	private ContactNickChangeAction nickChangeAction() {
		return new ContactNickChangeAction(_user, _nickChanger);
	}

	private void addToContactMenu(JPopupMenu menu, final ContactAction action, final JList friendsList) {
		final JMenuItem item = new JMenuItem(action.caption());
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ignored) {
				
				new Thread(){
					@Override
					public void run() {
						Contact contact = _I.contacts().currentGet(friendsList.getSelectedIndex());
						action.actUpon(contact);
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
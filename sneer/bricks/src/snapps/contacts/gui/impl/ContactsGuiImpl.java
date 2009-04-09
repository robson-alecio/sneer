package snapps.contacts.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.comparator.ContactComparator;
import snapps.contacts.internetaddress.gui.InternetAddressWindow;
import sneer.commons.lang.Functor;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.io.ui.graphics.Images;
import wheel.reactive.lists.ListSignal;

//Refactor Consider use only reactive widgets  
class ContactsGuiImpl implements ContactsGui {
	
	private static final Image ONLINE = getImage("online.png");
	private static final Image OFFLINE = getImage("offline.png");
	
	private final InstrumentManager _instrumentManager = my(InstrumentManager.class);
	private final ContactActionManager _actionsManager = my(ContactActionManager.class);
	private final ReactiveWidgetFactory _rfactory = my(ReactiveWidgetFactory.class);
	private final ContactManager _contacts = my(ContactManager.class);
	private final ConnectionManager _connections = my(ConnectionManager.class);
	private final ContactComparator _comparator = my(ContactComparator.class);
	private final ListSorter _sorter = my(ListSorter.class);

	private final SignalChooser<Contact> _chooser;
	private ListSignal<Contact> _sortedList;
	private ListWidget<Contact> _contactList;
	private Container _container;
	
	private static Image getImage(String fileName) {
		return Images.getImage(ContactsGuiImpl.class.getResource(fileName));
	}
	
	ContactsGuiImpl(){
		_instrumentManager.registerInstrument(this);
		_chooser = new SignalChooser<Contact>(){ @Override public Signal<?>[] signalsToReceiveFrom(Contact element) {
			return new Signal<?>[]{_connections.connectionFor(element).isOnline(), element.nickname()};
		}};
	} 

	@Override
	public void init(InstrumentWindow window) {
		ContactLabelProvider labelProvider = new ContactLabelProvider();
		ContactsGuiCellRenderer cellRenderer = new ContactsGuiCellRenderer(labelProvider);

		_container = window.contentPane();
		_sortedList = _sorter.sort(_contacts.contacts() , _comparator, _chooser);
		_contactList = _rfactory.newList(_sortedList, labelProvider, cellRenderer);
		_contactList.getComponent().setBorder(new EmptyBorder(0,0,0,0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(_contactList.getComponent());
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_contactList.getComponent().getBackground());
		
		_container.setLayout(new BorderLayout());
		_container.add(scrollPane, BorderLayout.CENTER);
		
		addNewContatAction(window.actions());
		addEditContactAction();

		new PopUpSupport();
	}
	
	@Override
	public int defaultHeight() {
		return 144;
	}
	
	@Override
	public String title() {
		return "My Contacts";
	}
	
	private void addNewContatAction(JPopupMenu popupMenu) {
		JMenuItem addContact = new JMenuItem("add or edit a contact");
		popupMenu.add(addContact);
		addContact.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			new NewContactWindow();
		}});
	}
	
	private void addEditContactAction(){
		_actionsManager.addContactAction(new ContactAction(){
			private Contact _contact;
			@Override public boolean isEnabled() {return true;}
			@Override public boolean isVisible() { return true; }
			@Override public void setActive(Contact contact) { _contact = contact; }
			@Override public String caption() { return "Edit Contact Info";}
			@Override public void run() {
				InternetAddressWindow frm = my(InternetAddressWindow.class);
				frm.setActiveContact(_contact);
				frm.open();
			}});
	}	
	
	final class ContactLabelProvider implements LabelProvider<Contact> {
		@Override public Signal<String> labelFor(Contact contact) {
			return contact.nickname();
		}

		@Override public Signal<Image> imageFor(Contact contact) {
			Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){ @Override public Image evaluate(Boolean value) {
				return value?ONLINE:OFFLINE;
			}};
			
			Signal<Boolean> isOnline = _connections.connectionFor(contact).isOnline();
			return my(Signals.class).adapt(isOnline, functor);
		}
	}
	
	private final class PopUpSupport {
		private PopUpSupport() {
			final JList list = _contactList.getMainWidget();
			list.addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					tryShowContactMenu(e);
				Contact contact = (Contact) list.getSelectedValue();
				if(contact==null) return;
				my(InternetAddressWindow.class).setActiveContact(contact);
			}});
		}

		private void tryShowContactMenu(MouseEvent e) {
			JList list = _contactList.getMainWidget();
			int index = list.locationToIndex(e.getPoint());
			list.getSelectionModel().setSelectionInterval(index, index);
			Contact contact = (Contact) list.getSelectedValue();

			JPopupMenu popupMain = new JPopupMenu();	
			for (ContactAction action : _actionsManager.actions()) {
				if(!action.isVisible()) continue;
				createMenuItem(popupMain, action, contact);
			}

			if(popupMain.getSubElements().length>0){
				popupMain.show(e.getComponent(),e.getX(),e.getY());
			}
		}

		private JMenuItem createMenuItem(JPopupMenu menu, ContactAction action, Contact contact) {
			action.setActive(contact);
			JMenuItem item = new JMenuItem(new SwingActionAdapter(action));
			item.setText(action.caption());
			menu.add(item);
			return item;
		}
	}
}
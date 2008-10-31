package snapps.contacts.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import sneer.kernel.container.Inject;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.io.ui.graphics.Images;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adapter;
import wheel.reactive.lists.ListSignal;

class ContactsGuiImpl implements ContactsGui {
	
	private static final Image ONLINE = getImage("online.png");
	private static final Image OFFLINE = getImage("offline.png");
	
	@Inject
	static private InstrumentManager _instrumentManager;

	@Inject
	static private ContactManager _contacts;
	
	@Inject
	static private ContactActionManager _actionsManager;

	@Inject
	static private ConnectionManager _connectionManager;

	@Inject
	static private ReactiveWidgetFactory _rfactory;
	
//	@Inject
//	static private ListSorter<Contact> _sorter;
	
	private ListWidget<Contact> _contactList;
	
	private ListSignal<Contact> _sortedList;
	
	ContactsGuiImpl(){
		_instrumentManager.registerInstrument(this);
	} 

	private static Image getImage(String fileName) {
		return Images.getImage(ContactsGuiImpl.class.getResource(fileName));
	}
	
	@Override
	public void init(Container container) {	
		new SorterSupport();
		_contactList = _rfactory.newList(_sortedList, new ContactLabelProvider());
		JScrollPane scrollPane = new JScrollPane();
		container.setLayout(new BorderLayout());
		container.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(_contactList.getComponent());
		scrollPane.setBorder(new TitledBorder(new EmptyBorder(5,5,5,5), getName()));
		_contactList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_contactList.getComponent().getBackground());
		new PopUpSupport();
	}
	
	@Override
	public int defaultHeight() {
		return 144;
	}
	
	private String getName() {
		return "My Contacts";
	}

//	private Boolean isOnline(Contact contact) {
//		return _connectionManager.connectionFor(contact).isOnline().currentValue();
//	}
	
	private final class ContactLabelProvider implements LabelProvider<Contact> {
		@Override
		public Signal<String> labelFor(Contact contact) {
			return contact.nickname();
		}

		@Override
		public Signal<Image> imageFor(Contact contact) {
			Signal<Boolean> isOnline = _connectionManager.connectionFor(contact).isOnline();
			Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){
				@Override
				public Image evaluate(Boolean value) {
					return value?ONLINE:OFFLINE;
				}};
			
			Adapter<Boolean, Image> imgSource = new Adapter<Boolean, Image>(isOnline, functor);
			return imgSource.output();
		}
	}
	
	private final class SorterSupport {{
		_sortedList = _contacts.contacts();
//		_sortedList = _sorter.sort(_contacts.contacts(), new Comparator<Contact>(){ public int compare(Contact contact1, Contact contact2) {
//				boolean isOnline1 = isOnline(contact1);
//				boolean isOnline2 = isOnline(contact1);
//	
//				if(isOnline1!=isOnline2){
//					if(isOnline1) return 1;
//					return -1;
//				}
//				return nick(contact1).compareTo(nick(contact2));
//			}
//			private String nick(Contact contact1) {
//				return contact1.nickname().currentValue();
//			}
//		});
	}}
	
	private final class PopUpSupport {
		
		private PopUpSupport() {
			final JList list = _contactList.getMainWidget();
			list.addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					tryShowContactMenu(e);
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
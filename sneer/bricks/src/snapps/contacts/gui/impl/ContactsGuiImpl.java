package snapps.contacts.gui.impl;

import static wheel.lang.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.comparator.ContactComparator;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.dashboard.InstrumentWindow;
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
	private static final Image ADD = getImage("add.png");
	
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
	
	ContactsGuiImpl(){
		_instrumentManager.registerInstrument(this);
		_chooser = new SignalChooser<Contact>(){ @Override public Signal<?>[] signalsToReceiveFrom(Contact element) {
			return new Signal<?>[]{_connections.connectionFor(element).isOnline(), element.nickname()};
		}};
	} 

	private static Image getImage(String fileName) {
		return Images.getImage(ContactsGuiImpl.class.getResource(fileName));
	}

	@Override
	public void init(InstrumentWindow window) {
		Container container = window.contentPane();
		ContactLabelProvider labelProvider = new ContactLabelProvider();
		ContactsGuiCellRenderer cellRenderer = new ContactsGuiCellRenderer(labelProvider);
		_sortedList = _sorter.sort(_contacts.contacts() , _comparator, _chooser);
		_contactList = _rfactory.newList(_sortedList, labelProvider, cellRenderer);
		
		JScrollPane scrollPane = new JScrollPane();
		container.setLayout(new BorderLayout());
		container.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(_contactList.getComponent());
		scrollPane.setBorder(new EmptyBorder(0,5,8,5));
		_contactList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_contactList.getComponent().getBackground());
		new PopUpSupport();
		new ToolbarSupport(window.actions());
	}
	
	@Override
	public int defaultHeight() {
		return 144;
	}

	final class ContactLabelProvider implements LabelProvider<Contact> {
		
		@Override
		public Signal<String> labelFor(Contact contact) {
			return contact.nickname();
		}

		@Override
		public Signal<Image> imageFor(Contact contact) {
			Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){ @Override public Image evaluate(Boolean value) {
				return value?ONLINE:OFFLINE;
			}};
			
			Signal<Boolean> isOnline = _connections.connectionFor(contact).isOnline();
			Adapter<Boolean, Image> imgSource = new Adapter<Boolean, Image>(isOnline, functor);
			return imgSource.output();
		}
	}
	
	private final class ToolbarSupport {
		public ToolbarSupport(Container toolbar) {
			JButton add = new JButton(new ImageIcon(ADD));
			add.setPreferredSize(new Dimension(16, 16));
			add.setBorder(new EmptyBorder(0,0,0,0));
			add.setOpaque(true);
			add.setBackground(Color.WHITE);
			toolbar.add(add);
		}
	}
	
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
	
	@Override
	public String title() {
		return "My Contacts";
	}
}
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
import javax.swing.border.TitledBorder;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.comparator.ContactInfoComparator;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.list.ContactInfo;
import sneer.pulp.contacts.list.ContactList;
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
	
	private final ContactList _contacts = my(ContactList.class);
	
	private final ContactInfoComparator _comparator = my(ContactInfoComparator.class);
	
	private final ListSorter _sorter = my(ListSorter.class);
	
	private final SignalChooser<ContactInfo> _chooser;

	private ListSignal<ContactInfo> _sortedList;
	
	private ListWidget<ContactInfo> _contactList;
	
	ContactsGuiImpl(){
		_instrumentManager.registerInstrument(this);
		_chooser = new SignalChooser<ContactInfo>(){ @Override public Signal<?>[] signalsToReceiveFrom(ContactInfo element) {
			return new Signal<?>[]{element.isOnline(), element.contact().nickname()};
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
		_sortedList = _sorter.sort(_contacts.output() , _comparator, _chooser);
		_contactList = _rfactory.newList(_sortedList, labelProvider, cellRenderer);
		
		JScrollPane scrollPane = new JScrollPane();
		container.setLayout(new BorderLayout());
		container.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(_contactList.getComponent());
		scrollPane.setBorder(new TitledBorder(new EmptyBorder(5,5,5,5), getName()));
		_contactList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_contactList.getComponent().getBackground());
		new PopUpSupport();
		new ToolbarSupport(window.toolbar());
	}
	
	@Override
	public int defaultHeight() {
		return 144;
	}
	
	private String getName() {
		return "My Contacts";
	}

	final class ContactLabelProvider implements LabelProvider<ContactInfo> {
		@Override
		public Signal<String> labelFor(ContactInfo info) {
			return info.contact().nickname();
		}

		@Override
		public Signal<Image> imageFor(ContactInfo info) {
			Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){ @Override public Image evaluate(Boolean value) {
				return value?ONLINE:OFFLINE;
			}};
			
			Signal<Boolean> isOnline = info.isOnline();
			
			Adapter<Boolean, Image> imgSource = new Adapter<Boolean, Image>(isOnline, functor);
			return imgSource.output();
		}
	}
	
	private final class ToolbarSupport {
		public ToolbarSupport(Container toolbar) {
			JButton add = new JButton(new ImageIcon(ADD));
			add.setPreferredSize(new Dimension(22, 22));
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
			ContactInfo contactInfo = (ContactInfo) list.getSelectedValue();
			Contact contact = contactInfo.contact();
			
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
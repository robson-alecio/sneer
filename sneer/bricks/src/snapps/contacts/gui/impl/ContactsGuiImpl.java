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

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.comparator.ContactComparator;
import snapps.contacts.gui.info.ContactInfoWindow;
import sneer.commons.lang.Functor;
import sneer.hardware.gui.images.Images;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.main.dashboard.InstrumentPanel;
import sneer.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.skin.main.synth.Synth;
import sneer.skin.main.synth.scroll.SynthScrolls;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;

class ContactsGuiImpl implements ContactsGui {
	
	private final Synth _synth = my(Synth.class);
	
	{_synth.load(this.getClass());}
	private final Image ONLINE = getImage("ContactsGuiImpl.onlineIconName");
	private final Image OFFLINE = getImage("ContactsGuiImpl.offlineIconName");
	
	private final SignalChooser<Contact> _chooser;

	private ListSignal<Contact> _sortedList;
	private ListWidget<Contact> _contactList;
	private Container _container;
	
	private Image getImage(String key) {
		return my(Images.class).getImage(ContactsGuiImpl.class.getResource((String) _synth.getDefaultProperty(key)));
	}
	
	ContactsGuiImpl(){
		my(InstrumentRegistry.class).registerInstrument(this);
		_chooser = new SignalChooser<Contact>(){ @Override public Signal<?>[] signalsToReceiveFrom(Contact element) {
			return new Signal<?>[]{my(ConnectionManager.class).connectionFor(element).isOnline(), element.nickname()};
		}};
	} 

	@Override
	public void init(InstrumentPanel window) {
		ContactLabelProvider labelProvider = new ContactLabelProvider();
		ContactsGuiCellRenderer cellRenderer = new ContactsGuiCellRenderer(labelProvider);

		_container = window.contentPane();
		_sortedList = my(ListSorter.class).sort( my(ContactManager.class).contacts() , my(ContactComparator.class), _chooser);
		
		_contactList = my(ReactiveWidgetFactory.class).newList(_sortedList, labelProvider, cellRenderer);
		_contactList.getComponent().setName("ContactList");
		_synth.attach(_contactList.getComponent());
		
		JScrollPane scrollPane = my(SynthScrolls.class).create();
		scrollPane.getViewport().add(_contactList.getComponent());
		
		_container.setLayout(new BorderLayout());
		_container.add(scrollPane, BorderLayout.CENTER);
		
		addContatActions(window.actions());
		addDefaultContactAction();
		
		new ListContactsPopUpSupport();
	}

	private void addDefaultContactAction() {
		contactList().addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(MouseEvent e) {
			if (e.getClickCount() > 1)
				my(ContactActionManager.class).defaultAction().run();
		}});
	}

	@Override
	public int defaultHeight() {
		return 144;
	}
	
	@Override
	public String title() {
		return "My Contacts";
	}
	
	@Override
	public Signal<Contact> selectedContact(){
		return _contactList.selectedElement();
	}
	
	private void addContatActions(JPopupMenu popupMenu) {
		JMenuItem add = new JMenuItem("New Contact...");
		popupMenu.add(add);
		
		add.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			contactList().setSelectedValue(newContact(), true);
			my(ContactInfoWindow.class).open();
		}});
	}
	
	private Contact newContact() {
		return my(ContactManager.class).produceContact("<nickname>");
	}

	private JList contactList() {
		return (JList)_contactList.getComponent();
	}	

	final class ContactLabelProvider implements LabelProvider<Contact> {
		@Override public Signal<String> labelFor(Contact contact) {
			return contact.nickname();
		}

		@Override public Signal<Image> imageFor(Contact contact) {
			Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){ @Override public Image evaluate(Boolean value) {
				return value?ONLINE:OFFLINE;
			}};
			
			Signal<Boolean> isOnline = my(ConnectionManager.class).connectionFor(contact).isOnline();
			return my(Signals.class).adapt(isOnline, functor);
		}
	}
	
	private final class ListContactsPopUpSupport {
		private ListContactsPopUpSupport() {
			final JList list = _contactList.getMainWidget();
			list.addMouseListener(new MouseAdapter(){ @Override public void mouseReleased(MouseEvent e) {
				int index = list.locationToIndex(e.getPoint());
				list.getSelectionModel().setSelectionInterval(index, index);
				if (e.isPopupTrigger())
					tryShowContactMenu(e);
			}});
		}

		private void tryShowContactMenu(MouseEvent e) {
			JPopupMenu popupMain = new JPopupMenu();	
			for (ContactAction action : my(ContactActionManager.class).actions())
				if (action.isVisible())
					createMenuItem(popupMain, action);

			if (popupMain.getSubElements().length>0)
				popupMain.show(e.getComponent(),e.getX(),e.getY());
		}

		private void createMenuItem(JPopupMenu menu, ContactAction action) {
			menu.add(new JMenuItem(new SwingActionAdapter(action)));
		}
	}
}
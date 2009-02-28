package snapps.contacts.gui.impl;

import static sneer.brickness.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.comparator.ContactComparator;
import sneer.brickness.PublicKey;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.dashboard.util.GuiUtil;
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
	
	private final KeyManager _keyManager = my(KeyManager.class);
	private final ContactManager _contactManager = my(ContactManager.class);
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
		_container = window.contentPane();
		ContactLabelProvider labelProvider = new ContactLabelProvider();
		ContactsGuiCellRenderer cellRenderer = new ContactsGuiCellRenderer(labelProvider);
		_sortedList = _sorter.sort(_contacts.contacts() , _comparator, _chooser);
		_contactList = _rfactory.newList(_sortedList, labelProvider, cellRenderer);
		
		JScrollPane scrollPane = new JScrollPane();
		_container.setLayout(new BorderLayout());
		_container.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(_contactList.getComponent());
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		_contactList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_contactList.getComponent().getBackground());
		new PopUpSupport();
		new ToolbarSupport(window.actions());
	}
	
	@Override
	public int defaultHeight() {
		return 144;
	}
	
	private void showNewContactFrame() {
		final JFrame frm = new JFrame("Inform Contact Nickname:");
		frm.getContentPane(). setLayout(new GridBagLayout());
		
		final JTextField txtNick = new JTextField();
		frm.getContentPane().add(txtNick,  new GridBagConstraints(0,0, 2,1, 2.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0) );

		JButton bntCancel = new JButton("Cancel");
		frm.getContentPane().add(bntCancel,  new GridBagConstraints(0,1, 1,1, 1.0,0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0) );

		JButton bntOk = new JButton("Ok");
		frm.getContentPane().add(bntOk,  new GridBagConstraints(1,1, 1,1, 1.0,0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0) );
		
		frm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		bntCancel.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			frm.setVisible(false);
			frm.dispose();
		}});
		
		bntOk.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			frm.setVisible(false);
			String nick = txtNick.getText();
			if(nick == null || nick.trim().length() == 0)	return;
			
			Contact contact = _contactManager.contactGiven(nick);
			if(contact == null){
				contact = _contactManager.addContact(nick);
				_keyManager.addKey(contact, mickeyMouseKey(nick));
			}
			frm.dispose();
			
			InternetAddressFrame frame = new InternetAddressFrame(contact);
			my(GuiUtil.class).setWindowBounds(_container, frame, 20, 400);
			frame.setVisible(true);
		}});
		
		my(GuiUtil.class).setWindowBounds(_container, frm, 0, 300);
		frm.setVisible(true);
		
	}

	@SuppressWarnings("deprecation")
	private PublicKey mickeyMouseKey(String nick) {
		return _keyManager.generateMickeyMouseKey(nick);
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
			
			add.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) {
				showNewContactFrame();
			}});
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
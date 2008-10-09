package snapps.contacts.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

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

class ContactsGuiImpl implements ContactsGui {

	private static final Image ONLINE = getImage("online.png");
	private static final Image OFFLINE = getImage("offline.png");
	
	@Inject
	static private InstrumentManager _instrumentManager;

	@Inject
	static private ContactManager _contacts;
	
	@Inject
	static private ConnectionManager _connectionManager;

	@Inject
	static private ReactiveWidgetFactory _rfactory;
	
	private ListWidget<Contact> _contactList;
	
	ContactsGuiImpl(){
		_instrumentManager.registerInstrument(this);
	} 

	private static Image getImage(String fileName) {
		return Images.getImage(ContactsGuiImpl.class.getResource(fileName));
	}
	
	@Override
	public void init(Container container) {	
		_contactList = _rfactory.newList(_contacts.contacts(), new ContactLabelProvider());
		JScrollPane scrollPane = new JScrollPane();
		container.setLayout(new BorderLayout());
		container.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(_contactList.getComponent());
		scrollPane.setMinimumSize(size(container));
		scrollPane.setPreferredSize(size(container));
		scrollPane.setBorder(new TitledBorder(new EmptyBorder(5,5,5,5), getName()));
		_contactList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(_contactList.getComponent().getBackground());
	}

	private Dimension size(Container container) {
		return new Dimension(container.getSize().width, 180 );
	}
	
	private String getName() {
		return "My Contacts";
	}
	
	public final class ContactLabelProvider implements LabelProvider<Contact> {
		@Override
		public Signal<String> labelFor(Contact contact) {
			return contact.nickname();
		}

		@Override
		public Signal<Image> imageFor(Contact contact) {
			Signal<Boolean> isOnline = _connectionManager.connectionFor(contact).isOnline();
//			Signal<Boolean> isOnline = new RandomBoolean().output();

			Functor<Boolean, Image> functor = new Functor<Boolean, Image>(){
				@Override
				public Image evaluate(Boolean value) {
					return value?ONLINE:OFFLINE;
				}};
			
			Adapter<Boolean, Image> imgSource = new Adapter<Boolean, Image>(isOnline, functor);
			return imgSource.output();
		}
	}
}
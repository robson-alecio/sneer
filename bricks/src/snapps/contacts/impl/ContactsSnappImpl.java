package snapps.contacts.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import snapps.contacts.ContactsSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import wheel.graphics.Images;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.impl.Adapter;

public class ContactsSnappImpl implements ContactsSnapp {

	private static final Image ONLINE = getImage("online.png");
	private static final Image OFFLINE = getImage("offline.png");
	
	@Inject
	static private SnappManager _snapps;

	@Inject
	static private ContactManager _contacts;
	
	@Inject
	static private ConnectionManager _connectionManager;

	@Inject
	static private RFactory _rfactory;
	
	private ListWidget<Contact> _contactList;
	
	public ContactsSnappImpl(){
		_snapps.registerSnapp(this);
	} 

	private static Image getImage(String fileName) {
		return Images.getImage(ContactsSnappImpl.class.getResource(fileName));
	}
	
	@Override
	public void init(Container container) {	
		_contactList = _rfactory.newList(_contacts.contacts(), new ContactLabelProvider());
		
		container.setLayout(new BorderLayout());
		container.add(_contactList.getComponent(), BorderLayout.CENTER);
		_contactList.getComponent().setBorder(new TitledBorder(new EmptyBorder(5,5,5,5), getName()));
	}

	@Override
	public String getName() {
		return "My Contacts";
	}
	
	public final class ContactLabelProvider implements LabelProvider<Contact> {
		
		private final Map<Contact, Signal<Image>> _imagesByContact = new HashMap<Contact, Signal<Image>>();
		
		@Override
		public Signal<Image> imageFor(Contact contact) {
			if (!_imagesByContact.containsKey(contact))
				_imagesByContact.put(contact, createImageSignal(contact));
			
			return _imagesByContact.get(contact);
		}

		@Override
		public Signal<String> labelFor(Contact contact) {
			return contact.nickname();
		}

		private Signal<Image> createImageSignal(Contact contact) {
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
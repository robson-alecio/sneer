package snapps.contacts.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JScrollPane;
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

class ContactsSnappImpl implements ContactsSnapp {

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
		return new Dimension(container.getSize().width, 150 );
	}
	
	@Override
	public String getName() {
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
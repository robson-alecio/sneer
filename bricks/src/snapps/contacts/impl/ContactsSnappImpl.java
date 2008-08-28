package snapps.contacts.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;

import snapps.contacts.ContactsSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import wheel.graphics.Images;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;

public class ContactsSnappImpl implements ContactsSnapp {

	@Inject
	static private ContactManager _contacts;

	@Inject
	static private RFactory _rfactory;

	
	private ListWidget<Contact> _contactList;
	
	private Container _container;


	@Inject
	static private SnappManager _snapps;
	
	public ContactsSnappImpl(){
		_snapps.registerSnapp(this);
	} 
	
	@Override
	public void init(Container container) {	
		_contactList = _rfactory.newList(_contacts.getContacts());
		_container = container;
		_container.setLayout(new BorderLayout());
		_container.add(_contactList.getComponent(), BorderLayout.CENTER);
		
		_contactList.setLabelProvider(new LabelProvider<Contact>(){

			Constant<Image> _image = new Constant<Image>(getImage());
			
			private Image getImage() {
				return Images.getImage(ContactsSnappImpl.class.getResource("online.png"));
			}

			@Override
			public Signal<Image> imageFor(Contact contact) {
				return _image;
			}

			@Override
			public Signal<String> labelFor(Contact contact) {
				return contact.nickname();
			}});
	}

	@Override
	public String getName() {
		return "My Contacts";
	}
}
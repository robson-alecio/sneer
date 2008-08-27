package snapps.contacts.impl;

import java.awt.BorderLayout;
import java.awt.Container;

import snapps.contacts.ContactsSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;

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
	}

	@Override
	public String getName() {
		return "My Contacts";
	}
}
package snapps.contacts.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Iterator;

import snapps.contacts.ContactsSnapp;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.widgets.reactive.ListModelSetter;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import wheel.reactive.lists.ListSignal;

public class ContactsSnappImpl implements ContactsSnapp {

	@sneer.kernel.container.Inject
	static private ContactManager _manager;

	@sneer.kernel.container.Inject
	static private RFactory _rfactory;

	private ListSignal<Contact> _source;
	
	private ListWidget<Contact> _contactList;
	private Container _container;

	private ListModelSetter<String> _setter;

	@Override
	public void init(Container container) {	
		_source = _manager.contacts();
		_setter = createListModelSetterFromContactsManager();
		
		_contactList = _rfactory.newList(_source, _setter);

		_container = container;
		_container.setLayout(new BorderLayout());
		_container.add(_contactList.getComponent(), BorderLayout.CENTER);
	}

	private ListModelSetter<String> createListModelSetterFromContactsManager() {
		ListModelSetter<String> tmp  = new ListModelSetter<String>() {

			@Override
			public void addElement(String nickname) {
				_manager.addContact(nickname);
			}

			@Override
			public void addElementAt(String nickname, int index) {
				_manager.addContact(nickname); //Fix: add support in manager
			}

			@Override
			public void removeElement(String nickname) {
				_manager.removeContact(nickname);
			}

			@Override
			public void removeElementAt(int index) {
				Iterator<Contact> iterator = _manager.contacts().iterator();
				for (int i = 0; iterator.hasNext(); i++) {
					if(i==index){
						Contact contact = iterator.next();
						_manager.removeContact(contact.nickname().currentValue());
					}
				}
			}
		};
		return tmp;
	}

	@Override
	public String getName() {
		return "My Contacts";
	}
}
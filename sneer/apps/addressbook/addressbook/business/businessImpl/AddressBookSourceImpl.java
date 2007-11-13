package addressbook.business.businessImpl;

import java.awt.Rectangle;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;
import addressbook.business.AddressBook;
import addressbook.business.AddressBookSource;
import addressbook.business.Person;
import addressbook.business.PersonInfo;
import addressbook.business.PersonSource;

public class AddressBookSourceImpl implements AddressBookSource{

	private final class MyOutput implements AddressBook {

		public ListSignal<Person> personList() {
			return _personList.output(); 
		}

		public Signal<Rectangle> bounds() {
			return _bounds.output();
		}
		
	}
	
	private MyOutput _output = new MyOutput();

	private final Source<Rectangle> _bounds = new SourceImpl<Rectangle>(new Rectangle(0,0,400,300));
	
	private final ListSource<Person> _personList = new ListSourceImpl<Person>(); 
	private final ListSource<PersonSource> _personSourceList = new ListSourceImpl<PersonSource>(); 
	
	public AddressBook output() {
		return _output;
	}

	public Consumer<PersonInfo> personAdder() {
		return new Consumer<PersonInfo>(){ public void consume(PersonInfo person) throws IllegalParameter {
			if (personSourceByName(person._name)!=null)
				throw new IllegalParameter("Person already Exists!");
			PersonSource tempSource = new PersonSourceImpl(person._name,person._address,person._phone,person._email);
			_personSourceList.add(tempSource);
			_personList.add(tempSource.output());
		}};
	}
	
	public Consumer<Pair<PersonInfo,PersonInfo>> personUpdater() {
		return new Consumer<Pair<PersonInfo,PersonInfo>>(){ public void consume(Pair<PersonInfo,PersonInfo> oldNew) throws IllegalParameter {
			if (personSourceByName(oldNew._b._name)!=null)
				throw new IllegalParameter("Person already Exists!");
			PersonSource tempSource = personSourceByName(oldNew._a._name);
			if (tempSource==null) return;
			if (!tempSource.output().name().currentValue().equals(oldNew._b._name))
				tempSource.nameSetter().consume(oldNew._b._name);
			if (!tempSource.output().address().currentValue().equals(oldNew._b._address))
				tempSource.addressSetter().consume(oldNew._b._address);
			if (!tempSource.output().phone().currentValue().equals(oldNew._b._phone))
				tempSource.phoneSetter().consume(oldNew._b._phone);
			if (!tempSource.output().email().currentValue().equals(oldNew._b._email))
				tempSource.emailSetter().consume(oldNew._b._email);		
		}};
	}

	public Omnivore<PersonInfo> personRemover() {
		return new Omnivore<PersonInfo>(){ public void consume(PersonInfo person) {
			PersonSource tempSource = personSourceByName(person._name);
			if (tempSource==null) return;
			_personSourceList.remove(tempSource);
			_personList.remove(tempSource.output());		
		}};
	}
	
	private PersonSource personSourceByName(String name){
		for(PersonSource person:_personSourceList.output())
			if (person.output().name().currentValue().equals(name))
				return person;
		return null;
	}

	public Omnivore<Rectangle> boundsSetter() {
		return _bounds.setter();
	}

}

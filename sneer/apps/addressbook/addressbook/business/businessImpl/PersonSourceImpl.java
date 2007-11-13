package addressbook.business.businessImpl;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import addressbook.business.Person;
import addressbook.business.PersonSource;

public class PersonSourceImpl implements PersonSource{

	public PersonSourceImpl(String name, String address, String phone, String email){
		_name = new SourceImpl<String>(name); 
		_address = new SourceImpl<String>(address); 
		_phone = new SourceImpl<String>(phone); 
		_email = new SourceImpl<String>(email); 
	}
	
	private final class MyOutput implements Person {

		public Signal<String> address() {
			return _address.output();
		}

		public Signal<String> email() {
			return _email.output();
		}

		public Signal<String> name() {
			return _name.output();
		}

		public Signal<String> phone() {
			return _phone.output();
		}
		
		@Override
		public String toString(){
			return _name.output().currentValue()+" - "+_phone.output().currentValue();
		}

		public int compareTo(Person o) {
			return toString().compareTo(o.toString());
		}
		
	}
	
	private MyOutput _output = new MyOutput();
	
	private Source<String> _name = new SourceImpl<String>("");
	private Source<String> _address = new SourceImpl<String>("");
	private Source<String> _email = new SourceImpl<String>("");
	private Source<String> _phone = new SourceImpl<String>("");

	public Person output() {
		return _output;
	}

	public Omnivore<String> addressSetter() {
		return _address.setter();
	}

	public Omnivore<String> emailSetter() {
		return _email.setter();
	}

	public Omnivore<String> nameSetter() {
		return _name.setter();
	}

	public Omnivore<String> phoneSetter() {
		return _phone.setter();
	}

}

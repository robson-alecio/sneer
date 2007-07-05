package sneer.kernel.gui.contacts;

import sneer.kernel.business.contacts.Contact;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;

public class ContactPrinter {

	private final Source<String> _source;
	private final Contact _contact;

	public ContactPrinter(Contact contact) {
		 _contact = contact;
		 _source = new SourceImpl<String>(print(contact));

		 Receiver<String> myReceiver = myReceiver();
		_contact.nick().addReceiver(myReceiver);
		 _contact.host().addReceiver(myReceiver);
		 _contact.state().addReceiver(myReceiver);

		 Receiver<Integer> myIntReceiver = myReceiver();
		 _contact.port().addReceiver(myIntReceiver);
}

	private <VO> Receiver<VO> myReceiver() {
		return new Receiver<VO>(){

			@Override
			public void receive(VO newValue) {
				String print = print(_contact);
				if (_source.output().currentValue().equals(print)) return; //Fix: Use a Source that can be initted without value.
				_source.setter().consume(print);
			}};
	}

	public Signal<String> output() {
		return _source.output();
	}

	private String print(Contact contact) { //Fix: this must be reactive.
		return onlineTag(contact) + " - " + contact.nick().currentValue() + " - " + contact.host().currentValue() + ":" + contact.port().currentValue() + " - " + contact.state().currentValue();
	}

	private String onlineTag(Contact contact) {
		return contact.isOnline().currentValue().booleanValue()
			? "On  :)"
			: "Off :(";
	}

}

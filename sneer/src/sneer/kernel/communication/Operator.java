package sneer.kernel.communication;

import sneer.kernel.business.contacts.Contact;

public interface Operator {

	<T> Connection<T> establishConnectionTo(Contact contact);

}

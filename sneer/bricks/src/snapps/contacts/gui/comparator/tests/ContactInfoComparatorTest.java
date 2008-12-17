package snapps.contacts.gui.comparator.tests;

import org.junit.Test;
import org.junit.runner.RunWith;

import snapps.contacts.gui.comparator.ContactInfoComparator;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.list.ContactInfo;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import tests.ContainerEnvironment;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;
import wheel.testutil.TestUtils;
import static wheel.lang.Environments.my;

@RunWith(ContainerEnvironment.class)
public class ContactInfoComparatorTest {
	
	private ListSorter _sorter;
	private final ListRegister<ContactInfo> _contacts = new ListRegisterImpl<ContactInfo>();
	private final SignalChooser<ContactInfo> _chooser = new SignalChooser<ContactInfo>(){ @Override public Signal<?>[] signalsToReceiveFrom(ContactInfo element) {
		return new Signal<?>[]{element.isOnline(), element.contact().nickname()};
	}};
	
	@Test
	public void testComparator() {
		
		final ContactInfoMock truea = new ContactInfoMock("a", true);
		final ContactInfoMock trueA = new ContactInfoMock("A", true);
		final ContactInfoMock trueB = new ContactInfoMock("B", true);
		final ContactInfoMock falseA = new ContactInfoMock("A", false);
		final ContactInfoMock falseB = new ContactInfoMock("B", false);

		ContactInfoComparator comparator = my(ContactInfoComparator.class);
		_sorter = my(ListSorter.class);

		_contacts.add(falseA);
		_contacts.add(trueB);
		
		ListSignal<ContactInfo> sortedList = _sorter.sort(_contacts.output(), comparator, _chooser);
		
		_contacts.add(trueA);
		_contacts.add(falseB);
		_contacts.add(truea);
		
		TestUtils.assertSameContents(sortedList, truea, trueA, trueB, falseA, falseB);
	}
}

class ContactInfoMock extends ContactInfo{

	ContactInfoMock(final String nick, boolean isOnline) {
		super( new Contact(){ @Override public Signal<String> nickname() { return new Constant<String>(nick); }}, 
				new Constant<Boolean>(isOnline)
		);
		_isOnline = new Constant<Boolean>(isOnline);
	}

	@Override public Signal<Boolean> isOnline() {	return _isOnline;	}
	@Override public String toString() { return isOnline().currentValue() + " - " + contact().nickname();}
}
package sneer.pulp.probe.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.probe.ProbeManager;
import sneer.pulp.tuples.TupleSpace;
import tests.TestThatIsInjected;
import wheel.reactive.impl.Constant;

@Ignore
@RunWith(JMock.class)
public class ProbeManagerTest extends TestThatIsInjected {

	@Inject private static ProbeManager _subject;
	@Inject private static ContactManager _contactManager;
	
	private final Mockery _mockery = new Mockery();
	
	private final TupleSpace _tuples = _mockery.mock(TupleSpace.class);
	
	private final ConnectionManager _connectionManager = _mockery.mock(ConnectionManager.class);
	@SuppressWarnings("unused")
	private Contact _neide;
	private final ByteConnection _connection = _mockery.mock(ByteConnection.class);

	
	@Override
	protected Object[] getBindings() {
		return new Object[]{_tuples, _connectionManager};
	}


	@Test
	public void testTupleBlocking() {
		_mockery.checking(new Expectations(){{
			one(_connectionManager).connectionFor(with(aNonNull(Contact.class))); will(returnValue(_connection));
			one(_connection).isOnline(); will(returnValue(new Constant<Boolean>(true)));
//			one(_contacts).addListReceiver(with(aNonNull(Consumer.class)));			
//				will(new CustomAction("adding contact list receiver") { @Override public Object invoke(Invocation invocation) throws Throwable {
//					_contactListReceiver = (Consumer<ListValueChange<Contact>>) invocation.getParameter(0);
//					return null;
//				}});// inSequence(main);
		}});

		_neide = _contactManager.addContact("Neide");
		_subject.blockTupleType(TupleTypeA.class);
	}
	
}

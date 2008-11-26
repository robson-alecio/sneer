package snapps.listentome.speextuples.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import snapps.listentome.speextuples.SpeexTuples;
import sneer.kernel.container.Inject;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.skin.sound.PcmSoundPacket;
import tests.TestThatIsInjected;

@RunWith(JMock.class)
public class PcmBlockingTest extends TestThatIsInjected {
	
	@SuppressWarnings("unused")
	@Inject
	private static SpeexTuples _subject;
	
	
	private final Mockery _mockery = new JUnit4Mockery();
	
	private final TupleFilterManager _filter = _mockery.mock(TupleFilterManager.class);
	
	{ 
		_mockery.checking(new Expectations() {{
			one(_filter).block(PcmSoundPacket.class);
		}});
	}
	
	
	@Override
	protected Object[] getBindings() {
		return new Object[] { _filter };
	}

	@Test
	public void testPcmBlocking() throws Exception {
		//  ;)
	}
	
}

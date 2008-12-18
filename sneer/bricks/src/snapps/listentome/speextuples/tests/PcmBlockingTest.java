package snapps.listentome.speextuples.tests;

import static wheel.lang.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import snapps.listentome.speextuples.SpeexTuples;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.skin.sound.PcmSoundPacket;
import tests.Contribute;
import tests.TestInContainerEnvironment;

public class PcmBlockingTest extends TestInContainerEnvironment {
	
	
	@Contribute
	private final TupleFilterManager _filter = mock(TupleFilterManager.class);
	
	{ 
		checking(new Expectations() {{
			one(_filter).block(PcmSoundPacket.class);
		}});
	}

	@SuppressWarnings("unused")
	private SpeexTuples _subject = my(SpeexTuples.class);
	
	@Test
	public void testPcmBlocking() throws Exception {
	}
	
}

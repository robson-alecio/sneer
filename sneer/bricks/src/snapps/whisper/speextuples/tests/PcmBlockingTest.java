package snapps.whisper.speextuples.tests;

import static wheel.lang.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import snapps.whisper.speextuples.SpeexTuples;
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
	private final SpeexTuples _subject = my(SpeexTuples.class);
	
	@Test
	public void testPcmBlocking() throws Exception {
	}
	
}

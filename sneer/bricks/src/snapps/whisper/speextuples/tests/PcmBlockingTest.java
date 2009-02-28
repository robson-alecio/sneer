package snapps.whisper.speextuples.tests;

import static sneer.brickness.environments.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import snapps.whisper.speextuples.SpeexTuples;
import sneer.brickness.testsupport.Contribute;
import sneer.brickness.testsupport.TestInBrickness;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.skin.sound.PcmSoundPacket;

public class PcmBlockingTest extends TestInBrickness {
	
	
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

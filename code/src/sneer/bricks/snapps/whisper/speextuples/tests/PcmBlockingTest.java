package sneer.bricks.snapps.whisper.speextuples.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.pulp.distribution.filtering.TupleFilterManager;
import sneer.bricks.skin.sound.PcmSoundPacket;
import sneer.bricks.snapps.whisper.speextuples.SpeexTuples;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Contribute;

public class PcmBlockingTest extends BrickTest {
	
	
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

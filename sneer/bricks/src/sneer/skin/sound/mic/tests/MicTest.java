package sneer.skin.sound.mic.tests;

import static sneer.commons.environments.Environments.my;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.Contribute;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.mic.Mic;
import wheel.testutil.SignalUtils;

public class MicTest extends BrickTest {

	private final Mic _subject = my(Mic.class);

	@Contribute	private final Audio _audio = mock(Audio.class);
	private final TargetDataLine _line = mock(TargetDataLine.class);
	private final AudioFormat _format = new AudioFormat(8000, 16, 1, true, false);
	
	@Test
	public void testIsRunningSignal() throws LineUnavailableException {
		checking(soundExpectations());
		
		SignalUtils.waitForValue(false, _subject.isRunning());
		
		_subject.open();
		SignalUtils.waitForValue(true, _subject.isRunning());
		
		_subject.close();
		SignalUtils.waitForValue(false, _subject.isRunning());
	}
	
	private Expectations soundExpectations() throws LineUnavailableException {
		return new Expectations() {{
			one(_audio).tryToOpenCaptureLine(); will(returnValue(_line));
			allowing(_audio).defaultAudioFormat(); will(returnValue(_format));
			allowing(_line).read(with(aNonNull(byte[].class)), with(0), with(320)); will(returnValue(320));
			allowing(_line).close();
		}};
	}
}
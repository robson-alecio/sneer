package sneer.bricks.skin.audio.mic.tests;

import static sneer.foundation.environments.Environments.my;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.bricks.skin.audio.kernel.Audio;
import sneer.bricks.skin.audio.mic.Mic;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Bind;

public class MicTest extends BrickTest {

	private final Mic _subject = my(Mic.class);
	private final TargetDataLine _line = mock(TargetDataLine.class);
	private final AudioFormat _format = new AudioFormat(8000, 16, 1, true, false);
	@Bind	private final Audio _audio = mock(Audio.class);

	@Test (timeout = 3000)
	public void testIsRunningSignal() throws LineUnavailableException {

		checking(soundExpectations());

		final SignalUtils signalUtils = my(SignalUtils.class);

		signalUtils.waitForValue(_subject.isOpen(), false);

		_subject.open();
		signalUtils.waitForValue(_subject.isOpen(), true);

		_subject.close();
		signalUtils.waitForValue(_subject.isOpen(), false);
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
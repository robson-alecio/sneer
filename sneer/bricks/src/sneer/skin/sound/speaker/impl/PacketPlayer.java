package sneer.skin.sound.speaker.impl;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import wheel.lang.Consumer;
import wheel.lang.exceptions.FriendlyException;

class PacketPlayer implements Consumer<PcmSoundPacket> {

	@Inject private static Audio _audio;
	@Inject private static BlinkingLights _lights;
	
	private final Light _light = _lights.prepare(LightType.ERROR);
	private boolean _isRunning = true;
	private SourceDataLine _line;
	
	synchronized void crash() {
		_isRunning = false;
		if (_line == null)	return;
		_line.stop();
		_line.drain();
		_line.close();
		_line = null;
	}
	
	@Override
	synchronized public void consume(PcmSoundPacket packet) {
		if (!_isRunning) return;
		play(packet);
	}
	
	private void play(PcmSoundPacket packet) {
		try {
			ensureLineIsOpen();
		} catch (LineUnavailableException e) {
			_lights.turnOnIfNecessary(_light, 
					new FriendlyException(e, "Error: audio line is unavailable, can't play a sound!", 
			  									  "Get an expert sovereign friend to help you."));
			crash();
			return;
		}
		final byte[] buffer = packet.payload.copy();
		_line.write(buffer, 0, buffer.length);
	}
	
	private void ensureLineIsOpen() throws LineUnavailableException {
		if(_line ==null)
			_line = _audio.openSourceDataLine();
		if (_line.isActive()) return;
	}
}
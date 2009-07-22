package spikes.sneer.bricks.snapps.whisper.speextuples.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.LineUnavailableException;

import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray2D;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMaps;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.streams.sequencer.Sequencer;
import sneer.bricks.pulp.streams.sequencer.Sequencers;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.skin.rooms.ActiveRoomKeeper;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.lang.ByRef;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Producer;
import spikes.sneer.bricks.skin.audio.mic.Mic;
import spikes.sneer.bricks.skin.audio.speaker.Speaker;
import spikes.sneer.bricks.skin.audio.speaker.Speaker.Line;
import spikes.sneer.bricks.snapps.whisper.speex.Decoder;
import spikes.sneer.bricks.snapps.whisper.speex.Encoder;
import spikes.sneer.bricks.snapps.whisper.speex.Speex;
import spikes.sneer.bricks.snapps.whisper.speextuples.SpeexPacket;
import spikes.sneer.bricks.snapps.whisper.speextuples.SpeexTuples;

class SpeexTuplesImpl implements SpeexTuples { //Refactor Break this into the encoding and decoding sides.

	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final Seals _keyManager = my(Seals.class);
	private final Speex _speex = my(Speex.class);

	private final Signal<String> _room = my(ActiveRoomKeeper.class).room();
	private byte[][] _frames = newFramesArray();
	private int _frameIndex;
	
	private final Encoder _encoder = _speex.createEncoder();
	private final Decoder _decoder = _speex.createDecoder();
	
	private final AtomicInteger _ids = new AtomicInteger();
	@SuppressWarnings("unused")	private Object _refToAvoidGc;
	private Consumer<SpeexPacket> _refToAvoidGc2;
	
	private final CacheMap<Seal, Sequencer<SpeexPacket>> _sequencersByPublisher = my(CacheMaps.class).newInstance();
	private Producer<Sequencer<SpeexPacket>> _sequencerProducer = sequencerProducer();
	
	public SpeexTuplesImpl() {

		_refToAvoidGc = my(Signals.class).receive(my(Mic.class).sound(), new Consumer<ImmutableByteArray>() { @Override public void consume(ImmutableByteArray packet) {
			if (encode(packet.copy()))
				flush();
		}});

		_refToAvoidGc2 = new Consumer<SpeexPacket>() {

		@Override public void consume(SpeexPacket packet) {
			if (isMine(packet))	return;
			if (!_room.currentValue().equals(packet.room)) return;

			playInSequence(packet);
		}};
		_tupleSpace.addSubscription(SpeexPacket.class, _refToAvoidGc2);
	}
	
	
	private void playInSequence(SpeexPacket packet) {
		Sequencer<SpeexPacket> sequencer = _sequencersByPublisher.get(packet.publisher(), _sequencerProducer);
		sequencer.produceInSequence(packet, packet.sequence);
	}
	
	
	private Producer<Sequencer<SpeexPacket>> sequencerProducer() {
		return new Producer<Sequencer<SpeexPacket>>() { @Override public Sequencer<SpeexPacket> produce() {
			final ByRef<Line> speakerLine = ByRef.newInstance();
			return my(Sequencers.class).createSequencerFor((short)15, (short)150, new Consumer<SpeexPacket>(){ @Override public void consume(SpeexPacket sequencedPacket) {
				play(sequencedPacket, speakerLine);
			}});
		}};
	}

	private short nextShort() {
		if(_ids.compareAndSet(Short.MAX_VALUE, Short.MIN_VALUE))
			return Short.MIN_VALUE;
		return (short)_ids.incrementAndGet();
	}
	
	private boolean isMine(Tuple packet) {
		return _keyManager.ownSeal().equals(packet.publisher());
	}
	
	private static byte[][] newFramesArray() {
		return new byte[FRAMES_PER_AUDIO_PACKET][];
	}

	private void flush() {
		_tupleSpace.publish(new SpeexPacket(immutable(_frames), _room.currentValue(), nextShort()));
		_frames = newFramesArray();
		_frameIndex = 0;
	}

	private ImmutableByteArray2D immutable(byte[][] array2D) {
		return my(ImmutableArrays.class).newImmutableByteArray2D(array2D);
	}

	private boolean encode(final byte[] pcmBuffer) {
		if (!_encoder.processData(pcmBuffer)) return false;
		
		_frames[_frameIndex++] = _encoder.getProcessedData();
		return _frameIndex == FRAMES_PER_AUDIO_PACKET;
	}
	
	
	private void play(SpeexPacket packet, ByRef<Line> speakerLine) {
		if (!initSpeakerLine(speakerLine)) return;
		for (byte[] frame : _decoder.decode(packet.frames.copy()))
			speakerLine.value.consume(frame);
	}

	
	private boolean initSpeakerLine(ByRef<Line> speakerLine) {
		if (speakerLine.value != null) return true;
		try {
			speakerLine.value = my(Speaker.class).acquireLine();
		} catch (LineUnavailableException e) {
			return false;
		}
		return true;
	}

}
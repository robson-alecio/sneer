package sneer.bricks.snapps.whisper.speextuples.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray2D;
import sneer.bricks.pulp.distribution.filtering.TupleFilterManager;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.streams.sequencer.Sequencer;
import sneer.bricks.pulp.streams.sequencer.Sequencers;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.skin.audio.PcmSoundPacket;
import sneer.bricks.skin.rooms.ActiveRoomKeeper;
import sneer.bricks.snapps.whisper.speex.Decoder;
import sneer.bricks.snapps.whisper.speex.Encoder;
import sneer.bricks.snapps.whisper.speex.Speex;
import sneer.bricks.snapps.whisper.speextuples.SpeexPacket;
import sneer.bricks.snapps.whisper.speextuples.SpeexTuples;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.lang.Consumer;

class SpeexTuplesImpl implements SpeexTuples {

	private final Map<Seal, Sequencer<SpeexPacket>> _sequencers = new HashMap<Seal, Sequencer<SpeexPacket>>();
	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final Seals _keyManager = my(Seals.class);
	private final TupleFilterManager _filter = my(TupleFilterManager.class); {
		_filter.block(PcmSoundPacket.class);
	}
	private final Speex _speex = my(Speex.class);

	static private final int FRAMES_PER_AUDIO_PACKET = 10;

	private final Signal<String> _room = my(ActiveRoomKeeper.class).room();
	private byte[][] _frames = newFramesArray();
	private int _frameIndex;
	
	private final Encoder _encoder = _speex.createEncoder();
	private final Decoder _decoder = _speex.createDecoder();
	
	private final AtomicInteger _ids = new AtomicInteger();
	private Consumer<PcmSoundPacket> _refToAvoidGc;
	private Consumer<SpeexPacket> _refToAvoidGc2;

	public SpeexTuplesImpl() {

		final Consumer<SpeexPacket> consumer = new Consumer<SpeexPacket>(){ @Override public void consume(SpeexPacket packet) {
			decode(packet);
		}};
		
		_refToAvoidGc = new Consumer<PcmSoundPacket>() { @Override public void consume(PcmSoundPacket packet) {
			if (!isMine(packet)) return;
			if (encode(packet.payload.copy()))
				flush();
		}};
		_refToAvoidGc2 = new Consumer<SpeexPacket>() { @Override public void consume(SpeexPacket packet) {
			if (isMine(packet))	return;
			if (!_room.currentValue().equals(packet.room)) return;

			Seal publisher = packet.publisher();
			if(!_sequencers.containsKey(publisher)) 
				_sequencers.put(publisher, my(Sequencers.class).createSequencerFor(consumer, (short)15, (short)150));
			
			_sequencers.get(publisher).produceInSequence(packet, packet.sequence);
		}};
		
		_tupleSpace.addSubscription(PcmSoundPacket.class, _refToAvoidGc);
		_tupleSpace.addSubscription(SpeexPacket.class, _refToAvoidGc2);
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
	
	protected void decode(SpeexPacket packet) {
		for (byte[] frame : _decoder.decode(packet.frames.copy()))
			_tupleSpace.acquire(new PcmSoundPacket(packet.publisher(), packet.publicationTime(), my(ImmutableArrays.class).newImmutableByteArray(frame)));
	}

	@Override
	public int framesPerAudioPacket() {
		return FRAMES_PER_AUDIO_PACKET;
	}
}
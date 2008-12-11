package snapps.listentome.speextuples.impl;

import static wheel.lang.Environments.my;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import snapps.listentome.speex.Decoder;
import snapps.listentome.speex.Encoder;
import snapps.listentome.speex.Speex;
import snapps.listentome.speextuples.SpeexPacket;
import snapps.listentome.speextuples.SpeexTuples;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.streams.sequencer.Sequencer;
import sneer.pulp.streams.sequencer.Sequencers;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.rooms.ActiveRoomKeeper;
import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Consumer;
import wheel.lang.ImmutableByteArray;
import wheel.reactive.Signal;

class SpeexTuplesImpl implements SpeexTuples {

	private final Map<PublicKey, Sequencer<SpeexPacket>> _sequencers = new HashMap<PublicKey, Sequencer<SpeexPacket>>();
	private final Speex _speex = my(Speex.class);
	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final KeyManager _keyManager = my(KeyManager.class);
	private final TupleFilterManager _filter = my(TupleFilterManager.class); {
		_filter.block(PcmSoundPacket.class);
	}

	static private final int FRAMES_PER_AUDIO_PACKET = 10;

	private final Signal<String> _room = my(ActiveRoomKeeper.class).room();
	private byte[][] _frames = newFramesArray();
	private int _frameIndex;
	
	private final Encoder _encoder = _speex.createEncoder();
	private final Decoder _decoder = _speex.createDecoder();
	
	private final AtomicInteger _ids = new AtomicInteger();

	public SpeexTuplesImpl() {

		final Consumer<SpeexPacket> consumer = new Consumer<SpeexPacket>(){ @Override public void consume(SpeexPacket packet) {
			decode(packet);
		}};
		_tupleSpace.addSubscription(PcmSoundPacket.class, new Consumer<PcmSoundPacket>() { @Override public void consume(PcmSoundPacket packet) {
			if (!isMine(packet)) return;
			if (encode(packet.payload.copy()))
				flush();
		}});
		_tupleSpace.addSubscription(SpeexPacket.class, new Consumer<SpeexPacket>() { @Override public void consume(SpeexPacket packet) {
			if (isMine(packet))	return;
			if (!_room.currentValue().equals(packet.room)) return;

			PublicKey publisher = packet.publisher();
			if(!_sequencers.containsKey(publisher)) 
				_sequencers.put(publisher, my(Sequencers.class).createSequencerFor(consumer, (short)30, (short)500));
			
			_sequencers.get(publisher).produceInSequence(packet, packet.sequence);
		}});
	}

	private short nextShort() {
		if(_ids.compareAndSet(Short.MAX_VALUE, Short.MIN_VALUE))
			return Short.MIN_VALUE;
		return (short)_ids.incrementAndGet();
	}
	
	private boolean isMine(Tuple packet) {
		return _keyManager.ownPublicKey().equals(packet.publisher());
	}
	
	private static byte[][] newFramesArray() {
		return new byte[FRAMES_PER_AUDIO_PACKET][];
	}

	private void flush() {
		_tupleSpace.publish(new SpeexPacket(_frames, _room.currentValue(), nextShort()));
		_frames = newFramesArray();
		_frameIndex = 0;
	}

	private boolean encode(final byte[] pcmBuffer) {
		if (!_encoder.processData(pcmBuffer)) return false;
		
		_frames[_frameIndex++] = _encoder.getProcessedData();
		return _frameIndex == FRAMES_PER_AUDIO_PACKET;
	}
	
	protected void decode(SpeexPacket packet) {
		for (byte[] frame : _decoder.decode(packet.frames))
			_tupleSpace.acquire(new PcmSoundPacket(packet.publisher(), packet.publicationTime(), new ImmutableByteArray(frame, frame.length)));
	}

	@Override
	public int framesPerAudioPacket() {
		return FRAMES_PER_AUDIO_PACKET;
	}
}
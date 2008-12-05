package snapps.listentome.speextuples.impl;

import snapps.listentome.speex.Decoder;
import snapps.listentome.speex.Encoder;
import snapps.listentome.speex.Speex;
import snapps.listentome.speextuples.SpeexPacket;
import snapps.listentome.speextuples.SpeexTuples;
import sneer.kernel.container.Inject;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Consumer;
import wheel.lang.ImmutableByteArray;

class SpeexTuplesImpl implements SpeexTuples {

	@Inject private static Speex _speex;
	@Inject static private TupleSpace _tupleSpace; 
	@Inject static private KeyManager _keyManager;
	@Inject static private TupleFilterManager _filter; {
		_filter.block(PcmSoundPacket.class);
	}

	static private final int FRAMES_PER_AUDIO_PACKET = 10;

	
	private byte[][] _frames = newFramesArray();
	private int _frameIndex;
	
	private final Encoder _encoder = _speex.createEncoder();
	private final Decoder _decoder = _speex.createDecoder();

	
	public SpeexTuplesImpl() {
		_tupleSpace.addSubscription(PcmSoundPacket.class, new Consumer<PcmSoundPacket>() { @Override public void consume(PcmSoundPacket packet) {
			if (!isMine(packet))
				return;
			if (encode(packet.payload.copy()))
				flush();
		}});
		_tupleSpace.addSubscription(SpeexPacket.class, new Consumer<SpeexPacket>() { @Override public void consume(SpeexPacket packet) {
			if (isMine(packet))
				return;
			decode(packet);
		}});
	}
	
	private boolean isMine(Tuple packet) {
		return _keyManager.ownPublicKey().equals(packet.publisher());
	}
	
	private static byte[][] newFramesArray() {
		return new byte[FRAMES_PER_AUDIO_PACKET][];
	}

	private void flush() {
		_tupleSpace.publish(new SpeexPacket(_frames));
		_frames = newFramesArray();
		_frameIndex = 0;
	}

	private boolean encode(final byte[] pcmBuffer) {
		if (!_encoder.processData(pcmBuffer)) return false;
		
		_frames[_frameIndex++] = _encoder.getProcessedData();
		return _frameIndex == FRAMES_PER_AUDIO_PACKET;
	}
	
	protected void decode(SpeexPacket packet) {
		for (byte[] frame : _decoder.decode(packet._frames))
			_tupleSpace.acquire(new PcmSoundPacket(packet.publisher(), packet.publicationTime(), new ImmutableByteArray(frame, frame.length)));
	}

	@Override
	public int framesPerAudioPacket() {
		return FRAMES_PER_AUDIO_PACKET;
	}
}

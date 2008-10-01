package sneer.pulp.audio;

/** A packet of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
public interface PcmSoundPacket {

	/** A newly created array with a copy of the contents of this packet. You can write to this array. See description of this interface.*/
	byte[] payload();
	
}

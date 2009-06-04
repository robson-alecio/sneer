package spikes.sneer.apps.talk.audio;

import javax.sound.sampled.AudioFormat;

class AudioUtil {
	static final private int FRAME_DURATION_MILLIS = 20;

	static final int SAMPLE_RATE = 8000;

	public static final int SAMPLE_SIZE_IN_BITS = 16;

	private static final int CHANNELS = 2; //for linux/alsa compatibility do not use mono

	static final boolean SIGNED = true;

	static final boolean BIG_ENDIAN = false;

	public static final int FRAMES_PER_AUDIO_PACKET = 10;

	static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);

	public static final int SOUND_QUALITY = 8; //From 0 (bad) to 10 (good);

	public static final int NARROWBAND_ENCODING = 0;

	public static final Integer RECEIVING_PACKET_LAG_THRESHOLD = 1000 / FRAME_DURATION_MILLIS / FRAMES_PER_AUDIO_PACKET;

	static void shortToByte(byte[] buffer, int offset, int value) {
		buffer[offset + 1] = (byte) (value & 0x00FF);
		buffer[offset] = (byte) ((value >> 8) & 0x000000FF);
	}

	static int byteToShort(byte[] buffer, int offset) {
		int result = buffer[offset + 1];
		result += (buffer[offset] & 0x00FF) << 8;
		return result;
	}

	private AudioUtil() {
	}
}

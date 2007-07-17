package sneer.apps.talk.audio;

import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;

public class AudioUtil {
    static final int SAMPLE_RATE = 11025;
    static final int SAMPLE_SIZE_IN_BITS = 16;
    static final int CHANNELS = 2; //for linux/alsa compatibility, should not use mono
    static final boolean SIGNED = true;
    static final boolean BIG_ENDIAN = false;
    static final int FRAMES=10;
    
    public static final int BUFFER_SIZE = SAMPLE_RATE*SAMPLE_SIZE_IN_BITS*CHANNELS/8;
    
    private AudioUtil() {
    }
    
    public static AudioFormat getFormat(){
        return new AudioFormat(SAMPLE_RATE,SAMPLE_SIZE_IN_BITS,CHANNELS,SIGNED,BIG_ENDIAN);
    }
    
    public static TargetDataLine getTargetDataLine() throws LineUnavailableException{
        return AudioSystem.getTargetDataLine(getFormat());
    }
    
    public static SourceDataLine getSourceDataLine() throws LineUnavailableException{
        return AudioSystem.getSourceDataLine(getFormat());
    }
    
	public static void shortToByte(byte[] buffer, int offset, int value) {
		buffer[offset+1] = (byte) (value & 0x00FF);
		buffer[offset] = (byte) ((value >> 8) & 0x000000FF);
	}

	public static int byteToShort(byte[] buffer, int offset) {
		int value = 0;
		value+=buffer[offset+1];
		value+=(buffer[offset]& 0x00FF) << 8;
		return value;
	}
    
}

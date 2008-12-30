package snapps.whisper.speex;

public interface Encoder {

	boolean processData(byte[] pcmBuffer);

	byte[] getProcessedData();

}

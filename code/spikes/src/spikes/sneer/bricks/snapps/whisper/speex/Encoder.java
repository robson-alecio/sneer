package spikes.sneer.bricks.snapps.whisper.speex;

public interface Encoder {

	boolean processData(byte[] pcmBuffer);

	byte[] getProcessedData();

}

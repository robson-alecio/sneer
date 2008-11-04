package snapps.listentome.speex;

public interface Encoder {

	boolean processData(byte[] pcmBuffer);

	byte[] getProcessedData();

}

package snapps.listentome.speex.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.junit.Test;

import snapps.listentome.speex.Decoder;
import snapps.listentome.speex.Encoder;
import snapps.listentome.speex.Speex;
import sneer.kernel.container.Inject;
import tests.TestThatIsInjected;

public class SpeexTest extends TestThatIsInjected {
	
	@Inject
	private static Speex _subject;
	
	@Test
	public void testCodec() throws Exception {

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("alo.wav"));
		
		byte[] dataToEncode = frames(audioInputStream);

		Encoder encoder = _subject.createEncoder();
		
		byte[][] encodedPackets = encodePackets(encoder, dataToEncode);

		if(false) 
			decodeAndPlayEncodedAudio(audioInputStream, encodedPackets);
	}

	private byte[][] encodePackets(Encoder encoder, byte[] dataToEncode) {

		List<byte[]> listOfEncodedPackets = new ArrayList<byte[]>();		
		
		final int bufferSize = 640;
		
		ByteArrayInputStream streamData = new ByteArrayInputStream(dataToEncode);
		
		int bytesRead;
		byte dataBuffer[] = new byte[bufferSize];
		while ((bytesRead = streamData.read(dataBuffer, 0, dataBuffer.length)) != -1) {
			if (bytesRead > 0) { 
				byte[] encodedBytes = encodeFrame(encoder, dataBuffer);
				listOfEncodedPackets.add(encodedBytes);
				
				assertTrue(dataBuffer.length > encodedBytes.length);
			}
		}		
		
		final byte[][] encodedPackets = new byte[listOfEncodedPackets.size()][];
		
		for (int index = 0; index < listOfEncodedPackets.size(); index++)
			encodedPackets[index] = listOfEncodedPackets.get(index);
		
		return encodedPackets;
	}
	
	private byte[] frames(AudioInputStream audioInputStream) throws IOException {
		AudioFormat audioFormat = audioInputStream.getFormat();
		
		int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
		byte[] originalbytes = new byte[bufferSize];

		audioInputStream.read(originalbytes, 0, originalbytes.length);
		return originalbytes;
	}
	
	private byte[] encodeFrame(Encoder encoder, byte[] frame) {
		boolean processedData = encoder.processData(frame);
		
		assertTrue(processedData);
		
		return encoder.getProcessedData();		
	}
	
	private void decodeAndPlayEncodedAudio(AudioInputStream audioInputStream,
			byte[][] encodedPackets) throws LineUnavailableException {
		SourceDataLine dataLine = tryInitSourceDataLine(audioInputStream.getFormat());
		try {
			Decoder decoder = _subject.createDecoder();
			byte[][] decodedBytes = decoder.decode(encodedPackets);
			
			for (byte[] decodedData: decodedBytes)
				dataLine.write(decodedData, 0, decodedData.length);			
			
		} finally {
			dataLine.drain();
			dataLine.stop();
			dataLine.close();
		}
	}
	
	private SourceDataLine tryInitSourceDataLine(AudioFormat audioFormat) throws LineUnavailableException {
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine(info);
		dataLine.open(audioFormat);
		dataLine.start();
		return dataLine;
	}	

}

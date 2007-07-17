package sneer.apps.talk.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.xiph.speex.SpeexEncoder;

// AUDIO FORMAT:
// Sequence of a fixed number (AudioUtil.FRAMES) speex decoded frames,
// each frame with the structure:
// - Header - short, 2 bytes
// - Content - decoded frame

public class SpeexMicrophone extends Thread {

	private AudioFormat _format;

	private TargetDataLine _line;

	private boolean _running = true;

	private AudioCallback _callback;

	private SpeexEncoder _encoder = new SpeexEncoder();

	public SpeexMicrophone(AudioCallback callback) {
		setDaemon(true);
		_callback = callback;
	}

	public void init() throws LineUnavailableException {
		_format = AudioUtil.getFormat();
		_line = AudioUtil.getTargetDataLine();
		_line.open(_format);
		_line.start();
		_encoder.init(0, 8, (int) _format.getFrameRate(), _format.getChannels());
		start();
		while (!isAlive()) {
			try {
				sleep(200);
			} catch (InterruptedException ie) {
			}
		}
		;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[2 * _encoder.getFrameSize() * _encoder.getChannels()];
		byte[][] frameBuffer = new byte[3][buffer.length * AudioUtil.FRAMES];

		int frameIndex = 0;
		int[] frameBufferIndex=new int[3];
		int[] average = new int[3];

		int currentBufferIndex=0;

		
		while (_running) {
			
			int read = _line.read(buffer, 0, buffer.length); //pega audio pcm puro, 16 bits 2 bytes=onda

			average[currentBufferIndex] = average[currentBufferIndex] + calculateAverage16BitsPcm(buffer,read);

			if (_encoder.processData(buffer, 0, read)) {
				int processed = _encoder.getProcessedData(frameBuffer[currentBufferIndex], frameBufferIndex[currentBufferIndex] + 2);
				AudioUtil.shortToByte(frameBuffer[currentBufferIndex], frameBufferIndex[currentBufferIndex], processed);
				frameBufferIndex[currentBufferIndex] = frameBufferIndex[currentBufferIndex] + processed + 2;
				frameIndex++;
				//System.out.println("encoding "+frameIndex+" - "+processed);
			}
			
			if (frameIndex == AudioUtil.FRAMES) {
				
				int first=0,second=0,third=0;
				if (currentBufferIndex == 0){
					first=1;second=2;third=0;
				}
				if (currentBufferIndex == 1){
					first=2;second=0;third=1;
				}
				if (currentBufferIndex == 2){
					first=0;second=1;third=2;
				}
					
				int cut = 20000;
				System.out.println(""+average[first]+"/"+average[second]+"/"+average[third]);
				if (((average[first]/AudioUtil.FRAMES) < cut)&&((average[second]/AudioUtil.FRAMES) > cut)&&((average[third]/AudioUtil.FRAMES) < cut)){ 
					//dont send
				}else{
					System.out.println(average[second]);
					if ((average[second]/AudioUtil.FRAMES) > cut)
					_callback.audio(frameBuffer[second], 0, frameBufferIndex[second]);
				}

				
				frameIndex = 0;
				
				currentBufferIndex++; //change current buffer
				if (currentBufferIndex==3)
					currentBufferIndex = 0;
				
				average[currentBufferIndex] = 0;
				frameBufferIndex[currentBufferIndex] = 0;
				
			}

		}
		_line.close();
	}
	
	public int calculateAverage16BitsPcm(byte[] buffer,int length){
		int total=0;
		for(int t=0;t<(length/2);t++){
			total+=AudioUtil.byteToShort(buffer, t*2);
		}
		return total/(length/2);
	}

	public interface AudioCallback {
		public void audio(byte[] buffer, int offset, int length);
	}

	public void close() {
		_running = false;
	}

}

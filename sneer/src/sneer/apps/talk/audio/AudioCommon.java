package sneer.apps.talk.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

import wheel.io.Log;

// This class is a singleton.

// The application should use isPossiblePlaybackAndCapture() before using the audio.
// If it is not possible, ask the user to close offending application, and then
// use refreshDeviceList() before calling isPossiblePlaybackAndCapture() again.

// You should use bestAvailableOutputMixer() to get the best mixer for playback,
// and use *the same mixer* in your java application to open multiple
// SourceDataLines to play simultaneous sounds.

public class AudioCommon {

	public static final AudioCommon instance = new AudioCommon();

	private List<AudioDevice> _inputDevices = new ArrayList<AudioDevice>();
	private List<AudioDevice> _outputDevices = new ArrayList<AudioDevice>();

	private AudioCommon(){ 
		refreshDeviceList();
	}

	public static final AudioCommon getInstance() {
		return instance;
	}


	public void refreshDeviceList() { 

		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		System.out.println("Available Mixers: " + mixerInfos.length);

		for (int i = 0; i < mixerInfos.length; i++) {
			System.out.println("********************\n Mixer " + i + ": " + mixerInfos[i].getName() + " desc: " + mixerInfos[i].getDescription() + " vend: " + mixerInfos[i].getVendor() + " ver: " + mixerInfos[i].getVersion());
			if (mixerInfos[i].getName().startsWith("Port "))
				continue;
			Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
			
			if (!mixer.isOpen()){
				try {
					mixer.open();
				} catch (LineUnavailableException e) {
					System.out.println("NOT AVAILABLE!");
					//lines locked by external applications... dont handle the problem here, check for empty mixer lists.
					continue;
				}
			}
				
			fillInputDevices(mixer);
			fillOutputDevices(mixer);
			mixer.close();
		}
	}

	private void fillOutputDevices(Mixer mixer) {
		Line.Info[] sourceLines = mixer.getSourceLineInfo();

		for (Line.Info info : sourceLines) {
			Line line = null;
			try {
				line = mixer.getLine(info);
				if (!(line instanceof SourceDataLine))
					continue;
				//System.out.println(info + " | " + line);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}

			if (info instanceof DataLine.Info) {
				DataLine.Info dinfo = (DataLine.Info) info;
				AudioFormat widest = null;
				int frameSize = 0;
				for (AudioFormat af : dinfo.getFormats()) {
					if (af.getEncoding() != AudioUtil.AUDIO_FORMAT.getEncoding())
						continue;
					if (af.isBigEndian() != AudioUtil.AUDIO_FORMAT.isBigEndian())
						continue;
					if (af.getSampleSizeInBits() != AudioUtil.AUDIO_FORMAT.getSampleSizeInBits())
						continue;
					if (af.getFrameSize() > frameSize) {
						widest = af;
						frameSize = af.getFrameSize();
					}
				}
				AudioDevice device = new AudioDevice(mixer, new AudioFormat(AudioUtil.SAMPLE_RATE,AudioUtil.SAMPLE_SIZE_IN_BITS,widest.getChannels(),AudioUtil.SIGNED,AudioUtil.BIG_ENDIAN));
				System.out.println(" OUT: " + device);
				getOutputDevices().add(device);
			}
		}

	}

	private void fillInputDevices(Mixer mixer) {
		Line.Info[] targetLines = mixer.getTargetLineInfo();

		for (Line.Info info : targetLines) {

			Line line = null;
			try {
				line = mixer.getLine(info);
				if (!(line instanceof TargetDataLine))
					continue;
				//System.out.println(info + " | " + line);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}

			if (info instanceof DataLine.Info) {
				DataLine.Info dinfo = (DataLine.Info) info;
				AudioFormat widest = null;
				int frameSize = 0;

				for (AudioFormat af : dinfo.getFormats()) {
					if (af.getEncoding() != AudioUtil.AUDIO_FORMAT.getEncoding())
						continue;
					if (af.isBigEndian() != AudioUtil.AUDIO_FORMAT.isBigEndian())
						continue;
					if (af.getSampleSizeInBits() != AudioUtil.AUDIO_FORMAT.getSampleSizeInBits())
						continue;
					if (af.getFrameSize() > frameSize) {
						widest = af;
						frameSize = af.getFrameSize();
					}
				}

				AudioDevice device = new AudioDevice(mixer, new AudioFormat(AudioUtil.SAMPLE_RATE,AudioUtil.SAMPLE_SIZE_IN_BITS,widest.getChannels(),AudioUtil.SIGNED,AudioUtil.BIG_ENDIAN));
				System.out.println(" IN: " + device);
				getInputDevices().add(device);

			}
		}
	}

	public List<Mixer> getPortMixers() {
		List<Mixer> supportingMixers = new ArrayList<Mixer>();
		Mixer.Info[] aMixerInfos = AudioSystem.getMixerInfo();
		for (int i = 0; i < aMixerInfos.length; i++) {
			Mixer mixer = AudioSystem.getMixer(aMixerInfos[i]);
			boolean bSupportsPorts = arePortsSupported(mixer);
			if (bSupportsPorts)
				supportingMixers.add(mixer);
		}
		return supportingMixers;
	}

	public boolean arePortsSupported(Mixer mixer) {
		Line.Info[] infos;
		infos = mixer.getSourceLineInfo();
		for (int i = 0; i < infos.length; i++)
			if (infos[i] instanceof Port.Info)
				return true;
		infos = mixer.getTargetLineInfo();
		for (int i = 0; i < infos.length; i++)
			if (infos[i] instanceof Port.Info)
				return true;
		return false;
	}

	public Port.Info[] getPortInfo(Mixer mixer) {
		Line.Info[] infos;
		List<Port.Info> portInfoList = new ArrayList<Port.Info>();
		infos = mixer.getSourceLineInfo();
		for (int i = 0; i < infos.length; i++)
			if (infos[i] instanceof Port.Info)
				portInfoList.add((Port.Info) infos[i]);
		infos = mixer.getTargetLineInfo();
		for (int i = 0; i < infos.length; i++)
			if (infos[i] instanceof Port.Info)
				portInfoList.add((Port.Info) infos[i]);
		return new Port.Info[0];
	}
	
	public Mixer bestAvailableMixerForInput(){
		if (_inputDevices.isEmpty())
			return null;
		for(AudioDevice device:_inputDevices)
			if (device.completeName().contains("Direct Audio Device") && (device.completeName().contains("Alsa"))) 
				return device.getMixer();
		for(AudioDevice device:_inputDevices)
			if (device.completeName().contains("Direct Audio Device")) 
				return device.getMixer();
		for(AudioDevice device:_inputDevices)
			if (device.completeName().contains("Java Sound Audio Engine")) 
				return device.getMixer();
		return _inputDevices.get(0).getMixer();
	}
	
	public Mixer bestAvailableMixerForOutput(){
		if (_outputDevices.isEmpty())
			return null;
		for(AudioDevice device:_outputDevices)
			if (device.completeName().contains("Direct Audio Device") && (device.completeName().contains("Alsa"))) 
				return device.getMixer();
		for(AudioDevice device:_outputDevices)
			if (device.completeName().contains("Direct Audio Device")) 
				return device.getMixer();
		for(AudioDevice device:_outputDevices)
			if (device.completeName().contains("Java Sound Audio Engine")) 
				return device.getMixer();
		return _outputDevices.get(0).getMixer();
	}

	public List<AudioDevice> getInputDevices() {
		return _inputDevices;
	}

	public List<AudioDevice> getOutputDevices() {
		return _outputDevices;
	}

	public TargetDataLine bestAvailableTargetDataLine(){
		try {
			Mixer mixer = AudioCommon.getInstance().bestAvailableMixerForOutput();
			if (mixer == null)
				return AudioSystem.getTargetDataLine(AudioUtil.AUDIO_FORMAT);
			return AudioSystem.getTargetDataLine(AudioUtil.AUDIO_FORMAT, mixer.getMixerInfo());
		}catch(IllegalArgumentException iae){
			//
		} catch (LineUnavailableException e) {
			//
		}
		try{
			return AudioSystem.getTargetDataLine(AudioUtil.AUDIO_FORMAT);
		} catch (LineUnavailableException e) {
			//
		}
		return null;
	}
	
	public SourceDataLine bestAvailableSourceDataLine(){
		try {
			Mixer mixer = AudioCommon.getInstance().bestAvailableMixerForInput();
			if (mixer == null)
				return AudioSystem.getSourceDataLine(AudioUtil.AUDIO_FORMAT);
			return AudioSystem.getSourceDataLine(AudioUtil.AUDIO_FORMAT, mixer.getMixerInfo());
		}catch(IllegalArgumentException iae){
			//
		} catch (LineUnavailableException e) {
			//
		}
		try{
			return AudioSystem.getSourceDataLine(AudioUtil.AUDIO_FORMAT);
		} catch (LineUnavailableException e) {
			//
		}
		return null;
	}
	
	public boolean isPlaybackPossible(){
		if (bestAvailableSourceDataLine()==null)
			return false;
		return true;
	}
	
	public boolean isCapturePossible(){
		if (bestAvailableTargetDataLine()==null)
			return false;
		return true;
	}
	
	public boolean isPlaybackAndCapturePossible(){
		return isPlaybackPossible()&&isCapturePossible();
	}
	
	public static void main(String[] args){
		AudioCommon.getInstance();
		System.out.println("-----------------------------");
		System.out.println("Playback possible: "+AudioCommon.getInstance().isPlaybackPossible());
		System.out.println("Capture possible: "+AudioCommon.getInstance().isCapturePossible());
		Mixer bestInput = AudioCommon.getInstance().bestAvailableMixerForInput();
		Mixer bestOutput = AudioCommon.getInstance().bestAvailableMixerForOutput();
		System.out.println("-----------------------------");
		System.out.println("best input mixer: " + ((bestInput!=null)?bestInput.getMixerInfo().getName():"NOT AVAILABLE"));
		System.out.println("best output mixer: " + ((bestOutput!=null)?bestOutput.getMixerInfo().getName():"NOT AVAILABLE"));
	}

}

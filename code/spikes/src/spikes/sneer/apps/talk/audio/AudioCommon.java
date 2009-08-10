package spikes.sneer.apps.talk.audio;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;
import sneer.foundation.lang.exceptions.NotImplementedYet;

// This class is a singleton.

// The application should use isPossiblePlaybackAndCapture() before using the audio.
// If it is not possible, ask the user to close offending application, and then
// use refreshDeviceList() before calling isPossiblePlaybackAndCapture() again.

// You should use bestAvailableOutputMixer() to get the best mixer for playback,
// and use *the same mixer* in your java application to open multiple
// SourceDataLines to play simultaneous sounds.

class AudioCommon {

	private static List<AudioDevice> _inputDevices = new ArrayList<AudioDevice>();
	private static List<AudioDevice> _outputDevices = new ArrayList<AudioDevice>();

	static { 
		refreshDeviceList();
	}

	
	private static void refreshDeviceList() { 

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

	private static void fillOutputDevices(Mixer mixer) {
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
				_outputDevices.add(device);
			}
		}

	}

	private static void fillInputDevices(Mixer mixer) {
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
				_inputDevices.add(device);

			}
		}
	}

//	private List<Mixer> getPortMixers() {
//		List<Mixer> supportingMixers = new ArrayList<Mixer>();
//		Mixer.Info[] aMixerInfos = AudioSystem.getMixerInfo();
//		for (int i = 0; i < aMixerInfos.length; i++) {
//			Mixer mixer = AudioSystem.getMixer(aMixerInfos[i]);
//			boolean bSupportsPorts = arePortsSupported(mixer);
//			if (bSupportsPorts)
//				supportingMixers.add(mixer);
//		}
//		return supportingMixers;
//	}

//	private boolean arePortsSupported(Mixer mixer) {
//		Line.Info[] infos;
//		infos = mixer.getSourceLineInfo();
//		for (int i = 0; i < infos.length; i++)
//			if (infos[i] instanceof Port.Info)
//				return true;
//		infos = mixer.getTargetLineInfo();
//		for (int i = 0; i < infos.length; i++)
//			if (infos[i] instanceof Port.Info)
//				return true;
//		return false;
//	}

//	private Port.Info[] getPortInfo(Mixer mixer) {
//		Line.Info[] infos;
//		List<Port.Info> portInfoList = new ArrayList<Port.Info>();
//		infos = mixer.getSourceLineInfo();
//		for (int i = 0; i < infos.length; i++)
//			if (infos[i] instanceof Port.Info)
//				portInfoList.add((Port.Info) infos[i]);
//		infos = mixer.getTargetLineInfo();
//		for (int i = 0; i < infos.length; i++)
//			if (infos[i] instanceof Port.Info)
//				portInfoList.add((Port.Info) infos[i]);
//		return new Port.Info[0];
//	}
//	
	private static Mixer bestAvailableMixerForInput(){
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
	
	private static Mixer bestAvailableMixerForOutput(){
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

	public static TargetDataLine bestAvailableTargetDataLine(){
		try {
			Mixer mixer = AudioCommon.bestAvailableMixerForOutput();
			if (mixer != null)
				return AudioSystem.getTargetDataLine(AudioUtil.AUDIO_FORMAT, mixer.getMixerInfo());
		} catch (IllegalArgumentException iae){
			my(Logger.class).log("Trying to getTargetDataLine. (Mic)", iae.getMessage());
		} catch (LineUnavailableException e) {
			my(ExceptionLogger.class).log(e, "Failed to get target data line (Mic) with best available mixer. Trying default data line...");
		}
		
		try{
			return AudioSystem.getTargetDataLine(AudioUtil.AUDIO_FORMAT);
		} catch (LineUnavailableException e) {
			throw new NotImplementedYet();
		}
	}
	
	public static SourceDataLine bestAvailableSourceDataLine(){
		try {
			Mixer mixer = AudioCommon.bestAvailableMixerForInput();
			if (mixer == null)
				return AudioSystem.getSourceDataLine(AudioUtil.AUDIO_FORMAT);
			return AudioSystem.getSourceDataLine(AudioUtil.AUDIO_FORMAT, mixer.getMixerInfo());
		}catch(IllegalArgumentException iae){
			my(Logger.class).log("Trying to getSourceDataLine. (Speaker)", iae.getMessage());
		} catch (LineUnavailableException e) {
			my(ExceptionLogger.class).log(e, "Failed to get source data line (Speaker) with best available mixer. Trying default data line...");
		}
		try{
			return AudioSystem.getSourceDataLine(AudioUtil.AUDIO_FORMAT);
		} catch (LineUnavailableException e) {
			throw new NotImplementedYet();
		}
	}
	
	private static boolean isPlaybackPossible() {
		return bestAvailableSourceDataLine() != null;
	}
	
	private static boolean isCapturePossible() {
		return bestAvailableTargetDataLine() !=null;
	}
	
	public static void main(String[] args){
		System.out.println("-----------------------------");
		System.out.println("Playback possible: "+AudioCommon.isPlaybackPossible());
		System.out.println("Capture possible: "+AudioCommon.isCapturePossible());
		Mixer bestInput = AudioCommon.bestAvailableMixerForInput();
		Mixer bestOutput = AudioCommon.bestAvailableMixerForOutput();
		System.out.println("-----------------------------");
		System.out.println("best input mixer: " + ((bestInput!=null)?bestInput.getMixerInfo().getName():"NOT AVAILABLE"));
		System.out.println("best output mixer: " + ((bestOutput!=null)?bestOutput.getMixerInfo().getName():"NOT AVAILABLE"));
	}

}

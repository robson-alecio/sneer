package spikes.sneer.apps.talk.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;

class AudioDevice {
	private Mixer _mixer;
	private AudioFormat _format;
	
	public AudioDevice(Mixer mixer, AudioFormat format){
		setMixer(mixer);
		_format = format;
	}

	void setFormat(AudioFormat format) {
		_format = format;
	}

	public AudioFormat getFormat() {
		return _format;
	}

	void setMixer(Mixer mixer) {
		_mixer = mixer;
	}

	public Mixer getMixer() {
		return _mixer;
	}
	
	public String completeName(){ //it aggregates most information possible from Mixer.Info, sometimes they place important information in different fields. ie.: ALSA inside name, not description. 
		Mixer.Info info = _mixer.getMixerInfo();
		return " " + info.getName() + " desc: " + info.getDescription() + " vend: " + info.getVendor() + " ver: " + info.getVersion();
	}
	
	@Override
	public String toString(){
		return completeName();
	}
	
}

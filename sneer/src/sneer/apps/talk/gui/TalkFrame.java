package sneer.apps.talk.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.JToggleButton;

import sneer.apps.talk.AudioPacket;
import sneer.apps.talk.audio.SpeexMicrophone;
import sneer.apps.talk.audio.SpeexSpeaker;
import sneer.apps.talk.audio.SpeexMicrophone.AudioConsumer;
import wheel.io.Log;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class TalkFrame extends JFrame {
	private SpeexMicrophone _microphone;

	private SpeexSpeaker _speaker;

	public TalkFrame(Signal<String> otherGuysNick, final Signal<AudioPacket> audioInput, Omnivore<AudioPacket> audioOutput) {
		_otherGuysNick = otherGuysNick;
		_audioOutput = audioOutput;

		initComponents();
		openAudio();

		_otherGuysNick.addReceiver(new Omnivore<String>() { @Override
			public void consume(String nick) {
				setTitle(nick);
			}
		});

		audioInput.addReceiver(new Omnivore<AudioPacket>() { @Override
			public void consume(AudioPacket audioPacket) {
				if (_speaker == null) return;
				int lagDecay = 1;  //Implement: Drops samples to recover from eventual lag. 0 is perfect sound. Make adaptive
				_speaker.sendAudio(audioPacket._content, lagDecay);
			}
		});

		setVisible(true);
	}

	private final Signal<String> _otherGuysNick;

	private final Omnivore<AudioPacket> _audioOutput;

	private final JToggleButton mute = new JToggleButton("mute");

	private void initComponents() {
		setLayout(new BorderLayout());
		add(mute, BorderLayout.CENTER);
		setSize(100, 50);
		addWindowListeners();
	}

	private void addWindowListeners() {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				openAudio();
			}
		});

	}

	private void sendAudio(byte[][] contents) {
		if (!mute.isSelected()) {
			final AudioPacket audioPacket = new AudioPacket(contents);
			_audioOutput.consume(audioPacket); // queue or thread needed?????
		}
	}

	private synchronized void openAudio() {
		if (_speaker != null) return;
		
		try {
			_speaker = new SpeexSpeaker();
			_microphone = new SpeexMicrophone(
				new AudioConsumer() {
					public void audio(byte[][] contents) {
						sendAudio(contents);
					}
				});
		} catch (LineUnavailableException e1) {
			// Fix: Should handle any problem here... could not open audio device
			Log.log(e1);
			e1.printStackTrace();
		}
	}
	
	synchronized private void closeAudio() {
		System.out.print("closing...");
		_microphone.close();
		_speaker.close();
		_microphone = null;
		_speaker = null;
		System.out.println("done");
	}

	public void close() {
		closeAudio();
		dispose();
	}
	
	private static final long serialVersionUID = 1L;
}

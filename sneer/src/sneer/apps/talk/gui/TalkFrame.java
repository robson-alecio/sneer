package sneer.apps.talk.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JToggleButton;

import org.quilombo.audio.MicrophoneSpeex;
import org.quilombo.audio.SpeakerSpeex;
import org.quilombo.audio.MicrophoneSpeex.MicrophoneCallback;

import sneer.apps.talk.AudioPacket;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class TalkFrame extends JFrame {
	private MicrophoneSpeex _microphoneSpeex;

	private SpeakerSpeex _speakerSpeex;

	public TalkFrame(Signal<String> otherGuysNick, final Signal<AudioPacket> audioInput, Omnivore<AudioPacket> audioOutput) {
		_otherGuysNick = otherGuysNick;
		_audioOutput = audioOutput;

		initComponents();

		_otherGuysNick.addReceiver(new Omnivore<String>() { @Override
			public void consume(String nick) {
				setTitle(nick);
			}
		});

		audioInput.addReceiver(new Omnivore<AudioPacket>() { @Override
			public void consume(AudioPacket audioPacket) {
				_speakerSpeex.sendAudio(audioPacket._content, audioPacket._content.length);
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
		initAudio();
	}

	private void initAudio() {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				_microphoneSpeex.close();
				_speakerSpeex.close();
				_microphoneSpeex = null;
				_speakerSpeex = null;
			}

			@Override
			public void windowOpened(WindowEvent e) {
				_speakerSpeex = new SpeakerSpeex();
				_microphoneSpeex = new MicrophoneSpeex(
						new MicrophoneCallback() {
							public void soundReceived(byte[] buffer, int processed) {
								byte[] contents = new byte[processed];
								System.arraycopy(buffer, 0, contents, 0, processed);
								sendAudio(contents);
							}
						});
				_microphoneSpeex.start();
				_speakerSpeex.start();
				_microphoneSpeex.waitWhileNotRunning();
				_speakerSpeex.waitWhileNotRunning();
			}
		});

	}

	private void sendAudio(byte[] contents) {
		if (!mute.isSelected()) {
			final AudioPacket audioPacket = new AudioPacket(contents);
			_audioOutput.consume(audioPacket); // queue or thread needed?????
		}
	}

	private static final long serialVersionUID = 1L;
}

package spikes.sandro.audio;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class AudioCaptureDemoGui extends JFrame {

	private final Sound _sound;
	
	public AudioCaptureDemoGui(Sound sound) throws HeadlessException {
		super("Capture/Playback Demo");
		_sound = sound;
		 initGui();
	}

	private void initGui() {
		final JButton captureBtn = new JButton("Capture");
		final JButton stopBtn = new JButton("Stop");
		final JButton playBtn = new JButton("Playback");
	
		captureBtn.setEnabled(true);
		stopBtn.setEnabled(false);
		playBtn.setEnabled(false);
		
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(captureBtn);
		getContentPane().add(stopBtn);
		getContentPane().add(playBtn);
	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 70);
	
		createCaptureListener(captureBtn, stopBtn, playBtn);
		createStopListener(captureBtn, stopBtn, playBtn);
		createPlayListener(playBtn);
	}

	private void createCaptureListener(final JButton captureBtn,
			final JButton stopBtn, final JButton playBtn) {
		captureBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			captureBtn.setEnabled(false);
			stopBtn.setEnabled(true);
			playBtn.setEnabled(false);
			_sound.captureAudio();
		}});
	}

	private void createPlayListener(final JButton playBtn) {
		playBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			_sound.playAudio();
		}});
	}

	private void createStopListener(final JButton captureBtn,	final JButton stopBtn, final JButton playBtn) {
		stopBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			captureBtn.setEnabled(true);
			stopBtn.setEnabled(false);
			playBtn.setEnabled(true);
			_sound.stopCapture(true);
		}});
	}

}

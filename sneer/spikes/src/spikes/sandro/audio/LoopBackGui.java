package spikes.sandro.audio;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JToggleButton;

public class LoopBackGui extends JFrame {

	private final JToggleButton _record = new JToggleButton("Rec");
	private final JToggleButton _play = new JToggleButton("Play");
	private final Sound _sound;
	
	public LoopBackGui(Sound sound) throws HeadlessException {
		super("Record/Play Demo");
		_sound = sound;
		 initGui();
	}

	private void initGui() {
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(_record);
		getContentPane().add(_play);
	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 70);
	
		createCaptureListeners();
		createPlayListeners();
	}

	private void createCaptureListeners() {
		_record.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			if(_record.isSelected())
				_sound.startRecord();
			else
				_sound.stopRecord();
		}});
	}

	private void createPlayListeners() {
		_play.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			if(_record.isSelected())
				_sound.startPlay();
			else
				_sound.stopPlay();
		}});
	}
}
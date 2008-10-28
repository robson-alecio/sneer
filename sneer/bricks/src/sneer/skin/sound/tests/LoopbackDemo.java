package sneer.skin.sound.tests;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JToggleButton;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.sound.Loopback;

public class LoopbackDemo extends JFrame {

	private final JToggleButton _record = new JToggleButton("Rec");
	private final JToggleButton _play = new JToggleButton("Play");
	private Loopback _loopback;
	
	public LoopbackDemo() throws HeadlessException {
		super("Record/Play Demo");
		Container container = ContainerUtils.getContainer();
		_loopback = container.produce(Loopback.class);
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
				_loopback.startRecord();
			else
				_loopback.stopRecord();
		}});
	}

	private void createPlayListeners() {
		_play.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			if(_play.isSelected())
				_loopback.startPlayer();
			else
				_loopback.stopPlayer();
		}});
	}
	
	public static void main(String[] args) {
		new LoopbackDemo().setVisible(true);
	}
}
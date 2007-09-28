package sneer.apps.filetransfer.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class FileTransferFrame extends JFrame {

	public FileTransferFrame() {
		initComponents();
		setVisible(true);
	}
	
	
	public void updateProgressBar(long value, long total){
		int current = (int)((value*100)/total);
		progress.setValue(current);
	}

	private final JProgressBar progress = new JProgressBar();

	private void initComponents() {
		setLayout(new BorderLayout());
		add(progress, BorderLayout.CENTER);
		setSize(100, 50);
		progress.setStringPainted(true);
		progress.setValue(0);
	}

	private static final long serialVersionUID = 1L;
}

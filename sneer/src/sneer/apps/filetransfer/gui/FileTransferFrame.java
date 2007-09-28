package sneer.apps.filetransfer.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class FileTransferFrame extends JFrame {

	private final String _prompt;

	public FileTransferFrame(String prompt) {
		_prompt = prompt;
		setTitle(translate("File Transfer"));
		initComponents();
		setVisible(true);
	}
	
	
	public void updateProgressBar(final long value, final long total){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				int current = (int)((value*100)/total);
				progress.setValue(current);
			}
		});
	}

	private final JProgressBar progress = new JProgressBar();

	private void initComponents() {
		setLayout(new BorderLayout());
		add(new JLabel(_prompt),BorderLayout.NORTH);
		add(progress, BorderLayout.CENTER);
		progress.setStringPainted(true);
		progress.setValue(0);
		pack();
	}

	private static final long serialVersionUID = 1L;
}

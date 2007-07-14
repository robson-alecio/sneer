package sneer.apps.filetransfer.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import sneer.apps.filetransfer.FilePart;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class FileTransferFrame extends JFrame {
	
	private Hashtable<String,File> _directoryMap = new Hashtable<String,File>();

	public FileTransferFrame(Signal<String> otherGuysNick, final Signal<FilePart> fileInput) {

		//Fix: should ask if wants do download the file or not!!!!

		initComponents();
		
		_otherGuysNick = otherGuysNick;
		_otherGuysNick.addReceiver(new Omnivore<String>() { @Override
			public void consume(String nick) {
				setTitle(nick);
			}
		});

		fileInput.addReceiver(new Omnivore<FilePart>() { @Override
			public void consume(FilePart filePart) {
			
			if (filePart._offset == 0 ) {//first filePart
				_directoryMap.put(filePart._filename, chooseTargetDirectory("Saving "+filePart._filename));
			}
			
			File directory = _directoryMap.get(filePart._filename);
			
			System.out.println("Received filepart: "+filePart);
			updateProgressBar(filePart._offset,filePart._filesize);
				try {
					String filename = directory.getPath() + File.separator + filePart._filename;
					RandomAccessFile raf = new RandomAccessFile(filename,"rws");
					raf.seek(filePart._offset);
					raf.write(filePart._content,0,filePart._content.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				updateProgressBar(filePart._offset+filePart._content.length,filePart._filesize);
			}

		});

		setVisible(true);
	}
	
	private File chooseTargetDirectory(String dialogTitle) {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle(dialogTitle);
		
		while (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {} //Fix: cancelling the dialog should cancel the transfer
		return fc.getSelectedFile();
	}
	
	public void updateProgressBar(long value, long total){
		int current = (int)((value*100)/total);
		progress.setValue(current);
	}

	private final Signal<String> _otherGuysNick;

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

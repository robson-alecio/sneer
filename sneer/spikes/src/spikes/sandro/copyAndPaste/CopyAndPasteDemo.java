package spikes.sandro.copyAndPaste;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;

public class CopyAndPasteDemo extends JFrame implements ClipboardOwner {

	private TextField sourceText = new TextField(40);
	private Clipboard clipboard;

	public CopyAndPasteDemo() {
		clipboard = getToolkit().getSystemClipboard();
		initGui();
	}

	public void initGui() {
		Button cmdCopy = new Button("Copy All Text");
		Button cmdPaste = new Button("Paste in Console");
		setLayout(new FlowLayout());
		getContentPane().add(sourceText);
		getContentPane().add(cmdCopy);
		getContentPane().add(cmdPaste);
		createCopyListener(cmdCopy);
		createPasteListener(cmdPaste);
	}

	private void createCopyListener(Button cmdCopy) {
		cmdCopy.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) {
			String text = sourceText.getText();
			StringSelection fieldContent = new StringSelection(text);
			clipboard.setContents(fieldContent, CopyAndPasteDemo.this);	
			logClipboardData("Copy: ");
		}});
	}

	private void createPasteListener(Button cmdPaste) {
		cmdPaste.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			logClipboardData("Paste: ");
		}});
	}
	
	private void logClipboardData(String label) {
		try {
			System.out.print(label);
			System.out.println(getClipboardData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getClipboardData() throws UnsupportedFlavorException,	IOException {
		Transferable contents = clipboard.getContents(this);
		if (contents == null) return "Nothing in Clip Board!";
		return (String) contents.getTransferData(DataFlavor.stringFlavor);
	}
	
	public void lostOwnership(Clipboard parClipboard, Transferable parTransferable) {
		//do nothing
	}

	public static void main(String[] args) {
		CopyAndPasteDemo demo = new CopyAndPasteDemo();
		demo.setBounds(10,10,300,200);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.setVisible(true);
	}
}
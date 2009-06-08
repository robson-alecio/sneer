package spikes.sandro.copyAndPaste;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class CopyAndPasteDemo extends JFrame implements ClipboardOwner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField = new JTextField(20);
	private Clipboard clipboard;

	public CopyAndPasteDemo() {
		clipboard = getToolkit().getSystemClipboard();
		initGui();
	}

	public void initGui() {
		JButton cmdCopy = new JButton("Copy All Text");
		JButton cmdPaste = new JButton("Paste in Console");
		setLayout(new FlowLayout());
		getContentPane().add(textField);
		getContentPane().add(cmdCopy);
		getContentPane().add(cmdPaste);
		
		createCopyListener(cmdCopy);
		createPasteListener(cmdPaste);
		
		createKeyMaps();
	}

	private void createKeyMaps() {
		int modifiers = getPortableSoModifiers();
		final KeyStroke ctrlc = KeyStroke.getKeyStroke(KeyEvent.VK_C, modifiers);
		final KeyStroke ctrlv = KeyStroke.getKeyStroke(KeyEvent.VK_V, modifiers);
		
		textField.getInputMap().put(ctrlc,  "ctrlc");
		textField.getActionMap().put("ctrlc",  new AbstractAction(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		@Override public void actionPerformed(ActionEvent e) {
			copy();
		}});

		textField.getInputMap().put(ctrlv,  "ctrlv");
		textField.getActionMap().put("ctrlv",  new AbstractAction(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		@Override public void actionPerformed(ActionEvent e) {
			paste();
		}});
	}

	private int getPortableSoModifiers() {
		return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	}
	
	private void copy() {
		String text = textField.getText();
		StringSelection fieldContent = new StringSelection(text);
		clipboard.setContents(fieldContent, CopyAndPasteDemo.this);	
		logClipboardData("Copy: ");
	}
	
	private void paste() {
		logClipboardData("Paste: ");
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
		return (String) contents.getTransferData(DataFlavor.stringFlavor);
	}
	
	public void lostOwnership(Clipboard parClipboard, Transferable parTransferable) {
		//do nothing
	}

	private void createCopyListener(JButton cmdCopy) {
		cmdCopy.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) {
			copy();
		}});
	}
	
	private void createPasteListener(JButton cmdPaste) {
		cmdPaste.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			paste();
		}});
	}
	
	public static void main(String[] args) {
		CopyAndPasteDemo demo = new CopyAndPasteDemo();
		demo.setBounds(10,10,300,200);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.setVisible(true);
	}
}
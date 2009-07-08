package sneer.bricks.skin.widgets.reactive.filechooser.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.accessibility.AccessibleContext;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import sneer.bricks.skin.widgets.reactive.filechooser.FileChooser;
import sneer.foundation.lang.Consumer;

public class FileChooserImpl implements FileChooser {
	
	@Override
	public void choose(Consumer<File> selectedFile) {
		@SuppressWarnings("unused")
		JFileChooser chooser = new NotModalFileChooser();
	}

	public static class NotModalFileChooser extends JFileChooser {
	
	    public static final int WAITING_OPTION = 100;
	    
	    @Override
		protected JDialog createDialog(Component parent) throws HeadlessException {
			String title = getUI().getDialogTitle(this);
			putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, title);
	
			JDialog dialog;
			if(parent==null) dialog = new JDialog(){
				
			};
			else dialog = new JDialog(SwingUtilities.getWindowAncestor(parent));
			
			dialog.setTitle(title);
			dialog.setModal(false);
			dialog.setComponentOrientation(this.getComponentOrientation());
	
			Container contentPane = dialog.getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(this, BorderLayout.CENTER);
	
			if (JDialog.isDefaultLookAndFeelDecorated()) 
				if (supportsWindowDecorations())
					dialog.getRootPane().setWindowDecorationStyle( JRootPane.FILE_CHOOSER_DIALOG);
	
			dialog.pack();
			dialog.setLocationRelativeTo(parent);
			return dialog;
		}
		
	    @Override
		public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
	    	final int returnValue[] = new int[1];
			if(approveButtonText != null) {
			    setApproveButtonText(approveButtonText);
			    setDialogType(CUSTOM_DIALOG);
			}
			JDialog dialog = createDialog(parent);
			dialog.addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) {
					returnValue[0] = CANCEL_OPTION;
				}
			});
			returnValue[0] = WAITING_OPTION;
			rescanCurrentDirectory();
			dialog.setVisible(true);
			return returnValue[0];
	    }
		
		private boolean supportsWindowDecorations() {
			return UIManager.getLookAndFeel().getSupportsWindowDecorations();
		}
	}
	
	public static void main(String[] args) throws Exception{
		JFileChooser chooser = new NotModalFileChooser();
		chooser.showOpenDialog(null);
	}
}
package sneer.bricks.skin.widgets.notmodal.filechooser.impl;

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

import sneer.bricks.skin.widgets.notmodal.filechooser.FileChooser;
import sneer.foundation.lang.Consumer;

public class FileChooserImpl implements FileChooser {
	
	@Override
	public JFileChooser newFileChooser(Consumer<File> selectionReceiver) {
		return new NotModalFileChooser(selectionReceiver);
	}

	public static class NotModalFileChooser extends JFileChooser {
	
		public static final int WAITING_OPTION = 100;
		private final Consumer<File> _selectionReceiver;

		protected JDialog dialog;
	    
	    public NotModalFileChooser(Consumer<File> selectionReceiver) {
			_selectionReceiver = selectionReceiver;
		}

		@Override
		protected JDialog createDialog(Component parent) throws HeadlessException {
			String title = getUI().getDialogTitle(this);
			putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, title);
			
			if(parent==null) 
				dialog = new JDialog();
			else 
				dialog = new JDialog(SwingUtilities.getWindowAncestor(parent));
			
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
			dialog = createDialog(parent);
			dialog.addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) {
					returnValue[0] = CANCEL_OPTION;
				}
			});
			returnValue[0] = WAITING_OPTION;
			rescanCurrentDirectory();
			dialog.setVisible(true);
			return returnValue[0];
	    }
		
	    @Override
		public void approveSelection() {
	    	fireActionPerformed(APPROVE_SELECTION);
	    	disposeDialog();
	    	_selectionReceiver.consume(getSelectedFile());
        }

	    @Override
       public void cancelSelection() {
	    	fireActionPerformed(CANCEL_SELECTION);
	    	_selectionReceiver.consume(null);
	    	disposeDialog();
        }	    
	    
	    private void disposeDialog() {
	    	if(dialog == null) return;
	    	
	    	dialog.setVisible(false);
	        firePropertyChange("JFileChooserDialogIsClosingProperty", dialog, null);
	    	dialog.removeAll();
	    	dialog.dispose();
	    	dialog = null;
	    }
	    
		private boolean supportsWindowDecorations() {
			return UIManager.getLookAndFeel().getSupportsWindowDecorations();
		}
	}
	
	public static void main(String[] args) throws Exception{
		Consumer<File> selectionReceiver = new Consumer<File>(){ @Override public void consume(File file) {
			System.out.println(file);
		}};
		
		//	JFileChooser chooser = my(FileChooser.class).newNotModalFileChooser(selectionReceiver);
		JFileChooser chooser = new FileChooserImpl().newFileChooser(selectionReceiver);
		chooser.showOpenDialog(null);
	}
}
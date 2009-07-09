package sneer.bricks.skin.filechooser.impl;

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

import sneer.bricks.skin.filechooser.FileChoosers;
import sneer.foundation.lang.Consumer;

class FileChoosersImpl implements FileChoosers {

	@Override
	public JFileChooser newFileChooser(Consumer<File> selectionReceiver) {
		return new TransientFileChooser(selectionReceiver);
	}

	/** Extension of JFileChooser to disable modal behavior */
	private static class TransientFileChooser extends JFileChooser {

		public static final int WAITING_OPTION = 100;

		private final Consumer<File> _selectionReceiver;
		protected JDialog _dialog;

	    public TransientFileChooser(Consumer<File> selectionReceiver) {
			_selectionReceiver = selectionReceiver;
		}

		@Override
		protected JDialog createDialog(Component parent) throws HeadlessException {
			String title = getUI().getDialogTitle(this);
			putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, title);

			_dialog = (parent == null) ? new JDialog() : new JDialog(SwingUtilities.getWindowAncestor(parent));

			_dialog.setTitle(title);
			_dialog.setModal(false);
			_dialog.setComponentOrientation(this.getComponentOrientation());

			Container contentPane = _dialog.getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(this, BorderLayout.CENTER);

			if (JDialog.isDefaultLookAndFeelDecorated()) 
				if (supportsWindowDecorations())
					_dialog.getRootPane().setWindowDecorationStyle(JRootPane.FILE_CHOOSER_DIALOG);

			_dialog.pack();
			_dialog.setLocationRelativeTo(parent);

			return _dialog;
		}

	    @Override
		public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
	    	final int returnValue[] = new int[1];

	    	if(approveButtonText != null) {
			    setApproveButtonText(approveButtonText);
			    setDialogType(CUSTOM_DIALOG);
			}

			_dialog = createDialog(parent);
			_dialog.addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) {
					returnValue[0] = CANCEL_OPTION;
				}
			});

			returnValue[0] = WAITING_OPTION;
			rescanCurrentDirectory();
			_dialog.setVisible(true);

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
	    	if(_dialog == null) return;

	    	_dialog.setVisible(false);
	        firePropertyChange("JFileChooserDialogIsClosingProperty", _dialog, null);
	    	_dialog.removeAll();
	    	_dialog.dispose();
	    	_dialog = null;
	    }

		private boolean supportsWindowDecorations() {
			return UIManager.getLookAndFeel().getSupportsWindowDecorations();
		}
	}

	public static void main(String[] args) throws Exception {
		Consumer<File> selectionReceiver = new Consumer<File>() { @Override public void consume(File file) {
			System.out.println(file);
		}};

		JFileChooser chooser = new FileChoosersImpl().newFileChooser(selectionReceiver);
		chooser.showOpenDialog(null);
	}
}

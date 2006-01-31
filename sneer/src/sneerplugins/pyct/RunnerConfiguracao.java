package teste;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class RunnerConfiguracao {

	public static void main(String[] args) {
		Snipet snipet = new Snipet();
		Display display = new Display ();
		final Shell shell = new Shell (display);
		FileDialog dialog = new FileDialog (shell, SWT.OPEN);
		dialog.setFilterNames (new String [] {"All Files (*.*)"});
		dialog.setFilterExtensions (new String [] {"*.*"}); //Windows wild cards
		dialog.setFilterPath ("c:\\pyct\\"); //Windows path
		dialog.setFileName ("");
		dialog.open();
		display.close();
		snipet.Configuracao(dialog.getFileName());
	}

}

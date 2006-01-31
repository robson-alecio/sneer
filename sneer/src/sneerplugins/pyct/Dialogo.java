package teste;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Dialogo {

public void Abrir() {
	String salva;
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.open ();
	FileDialog dialog = new FileDialog (shell, SWT.SAVE);
	dialog.setFilterNames (new String [] {"Batch Files", "All Files (*.*)"});
	dialog.setFilterExtensions (new String [] {"*.bat", "*.*"}); //Windows wild cards
	dialog.setFilterPath ("c:\\"); //Windows path
	dialog.setFileName ("fred.bat");
	salva = dialog.open();
	System.out.println ("Save to: " + dialog.open ());
	System.out.println ("Save to: " + salva);
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 

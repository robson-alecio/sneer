package sneer.ui.plugins.pyct;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AbreImagem {
	Display display = new Display();
	
	Shell shell = new Shell(display);
	Label label = new Label (shell, SWT.NONE);
	String nome = "C:\\a";
	
	public void abreImagem(String nome)
	{	
		
		final Shell shell = new Shell (display);
		Label label = new Label (shell, SWT.NONE);
		
		shell.setBounds(50, 50, 300, 200);
		
		shell.open ();
	
		ImageLoader loader = new ImageLoader();
		ImageData data = loader.load(nome)[0];
		Image image = new Image(display, data);
		if (data != null) {
			image = new Image (display, data);
			label.setImage (image);
		}
		label.pack ();
		shell.pack ();

		GC gc = new GC(shell);
		gc.drawImage(image, 10, 10);

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		image.dispose ();
		display.dispose ();
	}
	
	
	
	
}
	


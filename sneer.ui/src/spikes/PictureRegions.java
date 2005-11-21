package spikes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PictureRegions {

	public static void main(String[] args) throws FileNotFoundException {
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setBounds(50, 50, 300, 200);
		shell.open ();

		
		ImageLoader loader = new ImageLoader();
		ImageData data = loader.load(new FileInputStream("C://Temp//indexes.bmp"))[0];
		Image image = new Image(null, data);

		GC gc = new GC(shell);
		gc.drawImage(image, 10, 10);
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		
		image.dispose ();
		display.dispose ();

		
		
	}

}

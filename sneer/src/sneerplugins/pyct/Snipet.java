package exercicios;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snipet {
	
		public static void main(String[] args) throws FileNotFoundException {
			
			String nome;
			
			nome=JOptionPane.showInputDialog("Digite:");
			
			Display display = new Display ();
			
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

			elastico(label);
			
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			
			image.dispose ();
			display.dispose ();
		
			System.exit(0);
	}

		private static void elastico(final Label label) {
			Listener listener = new Listener () {
				//Point point = null;
				//static final int JITTER = 8;
				public void handleEvent (Event event) {
					switch (event.type) {
						case SWT.MouseDown:
						//	point = new Point (event.x, event.y);
						//	break;
						//case SWT.MouseMove:
						//	if (point == null) return;
						//	int deltaX = point.x - event.x, deltaY = point.y - event.y;
						//	if (Math.abs (deltaX) < JITTER && Math.abs (deltaY) < JITTER) {
						//		return;
						//	}
							Tracker tracker = new Tracker(label.getParent(), SWT.RESIZE);
							//Rectangle rect = display.map(shell, shell, shell.getClientArea ());
							Rectangle rect = new Rectangle(event.x, event.y, 0, 0);
							//rect.x -= deltaX;
							//rect.y -= deltaY;

							printRectangle(rect);
							tracker.setRectangles (new Rectangle [] {rect});
							tracker.open ();  //Blocks thread.

							printRectangle(tracker.getRectangles()[0]);
							//FALL THROUGH
						//case SWT.MouseUp:
						//	point = null;
						//	break;
					}
				}
				
				private void printRectangle(Rectangle rect) {
					System.out.println("" + rect.width + ", " + rect.height);
				}
			};
			label.addListener (SWT.MouseDown, listener);
			//figura.addListener (SWT.MouseMove, listener);
			//figura.addListener (SWT.MouseUp, listener);
			
		}

		
}

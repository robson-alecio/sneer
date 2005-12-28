package exercicios;



import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;

public class Snipet {
	
		public static void main(String[] args) throws FileNotFoundException {
			
			String nome ;
			
			nome=JOptionPane.showInputDialog("Digite o caminho:");
			
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
				
				private long _contador = System.currentTimeMillis();
				private Properties _properties = new Properties();
				
				{
					try {
						_properties.load(new FileInputStream("C:\\cordenadas.properties"));
					} catch (IOException x) {x.printStackTrace();}
				}

				public void handleEvent (Event event) {
					switch (event.type) {
						case SWT.MouseDown:
						
							Tracker tracker = new Tracker(label.getParent(), SWT.RESIZE);
							Rectangle rect = new Rectangle(event.x, event.y, 0, 0);
							
							try {
								salvarCordenadas(rect);
							} catch (IOException e) {
								
								e.printStackTrace();
							}
							tracker.setRectangles (new Rectangle [] {rect});
							tracker.open ();  
							try {
								salvarCordenadas(tracker.getRectangles()[0]);
							} catch (IOException e) {
								e.printStackTrace();
							}		
					}
				}
				
				private void salvarCordenadas(Rectangle rect) throws IOException {
					
					_properties.put("" + _contador++, rect.width + "," + rect.height);
					_properties.store(new FileOutputStream("C:\\cordenadas.properties"), "Cabecalho");
					
						FileOutputStream is = new FileOutputStream("C:\\cordenadas.txt");
						DataOutputStream ds = new DataOutputStream(is);
						ds.writeChars(rect.width+"\n"+rect.height);
						System.out.println("" + rect.width + ", " + rect.height);
				}		
			};
			label.addListener (SWT.MouseDown, listener);
		}		
}

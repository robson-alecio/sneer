package sneer.ui.plugins.pyct;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;

public class Snipet {
	 
	private static String  cordenadas;
	
		public void Configuracao(String nome) {
			
			String nomeImagem = "C:\\pyct\\" ;
			
			nomeImagem+=nome;
			
			cordenadas = nomeImagem;
			
			Display display = new Display ();
			
			final Shell shell = new Shell (display);
			
			Label label = new Label (shell, SWT.NONE);
			
			shell.setBounds(50, 50, 300, 200);
			
			shell.open ();
			
			ImageLoader loader = new ImageLoader();
			
			ImageData data = loader.load(nomeImagem)[0];
			
			Image image = new Image(display, data);
			
			if (data != null) {
				image = new Image (display, data);
				label.setImage (image);
			}
			
			label.pack ();
			
			shell.pack ();

			GC gc = new GC(shell);
			
			gc.drawImage(image, 10, 10);

			elastico(label,shell);
			
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			
			image.dispose ();
			display.dispose ();
			
			System.exit(0);
	}
		
		private static void elastico(final Label label,final Shell shell) {
			Listener listener = new Listener () {
		
				private int _contador = 1;
				String nome,salva;
				
				private Properties _properties = new Properties();
				{
					cordenadas +=".properties" ;
					try {
						_properties.load(new FileInputStream(cordenadas));
						
						
					} catch (IOException x) {x.printStackTrace();}
				}

				public void handleEvent (Event event) {
					switch (event.type) {
						case SWT.MouseDown:
							
							Tracker tracker = new Tracker(label.getParent(), SWT.RESIZE);
							Rectangle rect = new Rectangle(event.x, event.y, 0, 0);
							
							tracker.setRectangles (new Rectangle [] {rect});
							tracker.open ();  
							//nome = JOptionPane.showInputDialog("Digite o caminho:");
							dialogo();
							try {
								salvarCordenadas(tracker.getRectangles()[0]);
							//	System.out.println("x = "+event.x+"y = "+event.y);
							} catch (IOException e) {
								e.printStackTrace();
							}		
					}
				}
				
				private void salvarCordenadas(Rectangle rect) throws IOException {
					
					_properties.put(  _contador ++ + "", " "+ rect.width  + " " +  rect.height+" "+rect.x+" "+rect.y+" "+nome);
					_properties.store(new FileOutputStream(cordenadas), " ");
						System.out.println("" + rect.width + ", " + rect.height+" ,"+rect.x+","+rect.y);
						
						System.out.println(nome);
				}	
				public void dialogo()
				{
					FileDialog dialog = new FileDialog (shell, SWT.SAVE);
					dialog.setFilterNames (new String [] {"All Files (*.*)"});
					dialog.setFilterExtensions (new String [] {"*.*"}); //Windows wild cards
					dialog.setFilterPath ("c:\\pyct\\"); //Windows path
					dialog.setFileName ("");
					dialog.open();
					nome = dialog.getFileName();
					System.out.println ("Save to: " + dialog.open());
					System.out.println ("Save to: " + dialog.getFileName());
					System.out.println ("Save to: " + salva);
				}
				
			};
			
			label.addListener (SWT.MouseDown, listener);
		}		
}

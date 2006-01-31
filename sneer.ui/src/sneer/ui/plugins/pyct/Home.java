package sneer.ui.plugins.pyct;

import java.io.IOException;

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


public class Home {
	
	Display display = new Display ();
	
	final Shell shell = new Shell (display);
	
	Label label = new Label (shell, SWT.NONE);
	
	AbreArquivos abreArquivo = new AbreArquivos();
	
	String vetorCaminho[]	 = new String[20];
	
	int vetorAltura[]	 	 = new int[10];
	int vetorLargura[]	 	 = new int [10];
	int indice = 0;
	int baixo = 10,alto = 15;
	
		public void TelaPincipal() throws NumberFormatException, IOException{
			
			String nome = "C:\\pyct\\a";
			
			shell.setBounds(50, 50, 300, 200);
			
			shell.open ();
		
			ImageLoader loader = new ImageLoader();
			
			ImageData data = loader.load(nome)[0];
			
			Image image = new Image(display, data);
			
			if (data != null) {
				image = new Image (display, data);
				label.setImage(image);
			}
			
			label.pack ();
			
			shell.pack ();

			GC gc = new GC(shell);
			
			gc.drawImage(image, 10, 10);
			
			nome+=".properties";
			
			abreArquivo.abreCordenadas(nome);
			Listener listener = new Listener () {
				
				public void handleEvent (Event event) {
					String caminho;
					
					switch (event.type) {
						case SWT.MouseDown:
							
							Rectangle rect = new Rectangle(event.x, event.y, 0, 0);
							System.out.println("p = "+rect );
							
							for(indice = 0;indice < 10;indice++)
							{
								//if(((abreArquivo.vetorAltura[indice] >= event.x && abreArquivo.vetorAltura[indice] <= event.y)&&(abreArquivo.vetorAltura[indice] != 0)) && ((abreArquivo.vetorLargura[indice] <= event.y && abreArquivo.vetorLargura[indice]>= event.x)&&( abreArquivo.vetorLargura[indice] !=0)))
								//if(((event.x >= abreArquivo.vetorAltura[indice] && event.x <= abreArquivo.vetorLargura[indice] && abreArquivo.vetorAltura[indice] != 0) &&(event.y <= abreArquivo.vetorLargura[indice] && event.y >= abreArquivo.vetorAltura[indice] && abreArquivo.vetorLargura[indice] != 0))||((event.x <= abreArquivo.vetorAltura[indice] && event.x >= abreArquivo.vetorLargura[indice] && abreArquivo.vetorAltura[indice] != 0) &&(event.y >= abreArquivo.vetorLargura[indice] && event.y <= abreArquivo.vetorAltura[indice] && abreArquivo.vetorLargura[indice] != 0)))
								if((event.x <= abreArquivo.xInicial[indice] && event.x >= abreArquivo.xFinal[indice] && abreArquivo.xInicial[indice] !=0 ) &&(event.y <= abreArquivo.yInicial[indice] && event.y >=abreArquivo.yFinal[indice]) && abreArquivo.yInicial[indice] !=0 )
								{
									System.out.println("trocou");
									
									ImageLoader loader = new ImageLoader();
									
									ImageData data = loader.load(abreArquivo.vetorCaminho[indice])[0];
									
									caminho = abreArquivo.vetorCaminho[indice];
									
									caminho+=".properties";
									
									try{
									abreArquivo.abreCordenadas(caminho);
									}
									catch(IOException e){
										e.printStackTrace()	;
									}
									
									Image image = new Image(display, data);
									
									if (data != null) {
										image = new Image (display, data);
										label.setImage(image);
									}
									
									label.pack ();
									
									shell.pack ();

								}
							}				
					}	
				}	
			};
			
			label.addListener (SWT.MouseDown, listener);
			
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			
			image.dispose ();
			display.dispose ();	
	}	
}

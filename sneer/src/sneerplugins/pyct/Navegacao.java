package MigracaoSwing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Navegacao extends JFrame implements MouseListener, MouseMotionListener {

	private JLabel statusBar;
	private JLabel foto;
	private int cordenadaInicialX;
	private int cordenadaFinalX;
	private int cordenadaInicialY;
	private int cordenadaFinalY;
	private String  cordenadas ;
	private Properties _properties = new Properties();
	public String nome ;
	public AbreArquivos abreArquivo = new AbreArquivos() ;
	String vetorCaminho[]	 = new String[20];
	Container container = getContentPane();
	int vetorAltura[]	 	 = new int[10];
	int vetorLargura[]	 	 = new int [10];
	int indice = 0;
	int baixo = 10,alto = 15;
	Icon bug ;
	String nomeFoto = "C:\\pyct\\a";
	String nomeImagem2 = "a";
	String nomeImagem = "C:\\pyct\\";
	String nomeFoto5 = "a";
	public void  Navegacao()
	{
	
		
		String nome = "C:\\pyct\\";
		
		try {
			
			abreArquivo.abreCordenadas(nome+nomeFoto5+".properties");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		statusBar = new JLabel();
		getContentPane().add( statusBar, BorderLayout.SOUTH );
 
		
		container.setLayout( new FlowLayout() );

		bug = new ImageIcon( nomeFoto );
		foto = new JLabel( bug, SwingConstants.CENTER );
		foto.setToolTipText( "************" );
   
		foto.addMouseListener( this );
		foto.addMouseMotionListener( this );
		
		container.add( foto );

		setSize( 800,600 );
		setVisible( true );
		
		
		nomeImagem+=nomeImagem2;
		
		cordenadas = "C:\\pyct\\"+nomeFoto5+".properties";
		
		try {
			_properties.load(new FileInputStream(cordenadas));
				
		} catch (IOException x) {x.printStackTrace();}
		
	}
	
	private void openFile()
	{
		JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG );
	    
	    int result = fileChooser.showSaveDialog( this );
	   
	    File file = fileChooser.getSelectedFile();
	    
	    nome = fileChooser.getName(file);
	   
	    System.out.println(nome);
	}  
	
	public static void main(String[] args) {
		Navegacao navegacao = new Navegacao();
		navegacao.Navegacao();
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		statusBar.setText( "Pressed at [" + event.getX() + ", " + event.getY() + "]" );
		indice = 1;
		//for(indice = 1;indice <=10 ;indice ++)
		//{
			System.out.println(abreArquivo.xInicial[indice]);
			//if(event.getX()==9)
			if((event.getX() >= abreArquivo.xInicial[indice] && event.getX() <= abreArquivo.xFinal[indice] && abreArquivo.xInicial[indice] !=0 ) &&(event.getY() >= abreArquivo.yInicial[indice] && event.getY()<=abreArquivo.yFinal[indice]) && abreArquivo.yInicial[indice] !=0 )
			{
				System.out.println("***********");
				
				container.remove(foto);
				String nomeImagemAberta = abreArquivo.vetorCaminho[indice];
				nomeFoto = nomeImagemAberta;
				nomeImagem2 = abreArquivo.vetorCaminho[indice];
				nomeFoto5 = abreArquivo.nome;
				System.out.println("**"+abreArquivo.nome);
				Navegacao();
				
				
			}
		//}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}

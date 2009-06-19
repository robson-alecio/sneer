package spikes.pyct;



import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;




public class MouseTracker extends JFrame implements MouseListener, MouseMotionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel statusBar;
	private JLabel foto;
	private int cordenadaInicialX;
	private int cordenadaFinalX;
	private int cordenadaInicialY;
	private int cordenadaFinalY;
	private String  cordenadas ;
	private Properties _properties = new Properties();
	private Properties relations = new Properties();
	private String _nome;
	private String nomeImagemGeral;
	private Container container;
	
	public MouseTracker()
	{
		super( "pyct" );
	}
	
	public void executa(String nome, String nomeArquivo)
	{
		   JMenu abrir = new JMenu( "Arquivo" );
		   abrir.setMnemonic( 'A' );

		  
		   JMenuItem abrirFoto = new JMenuItem( "Abrir Foto" );
		   abrirFoto.setMnemonic( 'F' );

		   abrirFoto.addActionListener(
		     
		      new ActionListener() {

		         
		         public void actionPerformed( ActionEvent event )
		         {
		            RunnerConfiguracao executa = new RunnerConfiguracao();
		            executa.executa();
		         }

		      } 

		   );
		   
		   JMenuItem criarFotoRelacionada = new JMenuItem( "Create Link" );
		   abrirFoto.setMnemonic( 'F' );

		   criarFotoRelacionada.addActionListener(
		     
		      new ActionListener() {

		         
		         public void actionPerformed( ActionEvent event )
		         {
		        	 openFile();
		        	 salvaFotosRelacionadas();
		         }

		      } 

		   );

		   abrir.add( abrirFoto );
		   abrir.add(criarFotoRelacionada);
		   
		   JMenuBar bar = new JMenuBar();  
		   setJMenuBar( bar );  
		   bar.add( abrir );   
		
		   statusBar = new JLabel();
		   getContentPane().add( statusBar, BorderLayout.SOUTH );
 
		   container = getContentPane();
		   container.setLayout( new FlowLayout() );
		
		   Icon bug = new ImageIcon( nome );
		   foto = new JLabel( bug, SwingConstants.CENTER );
		   foto.setToolTipText( "*******" );
   
		   foto.addMouseListener( this );
		   foto.addMouseMotionListener( this );
		   container.add( foto );

		   setSize( 1024, 760 );
		   setVisible( true );
		   String nomeImagem = "C:\\pyct\\" ;
		
		   nomeImagem+=nomeArquivo;
		
		   cordenadas = nomeArquivo+".properties";
		
		
		   try {
			   _properties.load(new FileInputStream(nomeImagem+".properties"));
				
		   } catch (IOException x) {x.printStackTrace();}
		   nomeImagemGeral = nomeArquivo;
		}

	private void salvarCordenadas() throws IOException 
	{
		String pasta = "C:\\pyct\\" ;
		String nomeImagem = nomeImagemGeral;
		int _contador = 0;
		String nomePasta = pasta+nomeImagem+".properties";
		
		cordenadas = nomePasta;
		_properties.put(_contador ++ + "", " "+cordenadaInicialX+" "+ cordenadaInicialY+ " " + cordenadaFinalX+ " " + cordenadaFinalY + " " + _nome+"" );
		_properties.store(new FileOutputStream(cordenadas), " ");
	}	
	
	private void salvaFotosRelacionadas()
	{
		int _contador = 0;
		String pastaRelacionamento = "C:\\pyct\\" ;
		String nomeImagemRelacionamento = nomeImagemGeral;
		String nomePasta = pastaRelacionamento+nomeImagemRelacionamento+".relations";
		
		
		relations.put(_contador ++ + "", " "+_nome+"" );
		try {
			relations.store(new FileOutputStream(nomePasta), " Relations");
		} 
		catch (FileNotFoundException e) 
		{
			
		}
		catch (IOException e) 
		{
			
		}
	}
		
	private void openFile()
	{
		JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
	    
	    
	    fileChooser.showSaveDialog( this );
	   
	    File file = fileChooser.getSelectedFile();
	    
	    _nome = fileChooser.getName(file);
	   
	    System.out.println(_nome);
	}  
	
	public void mouseClicked( MouseEvent event )
	{
		statusBar.setText( "Clicked at [" + event.getX() + ", " + event.getY() + "]" );
	}

	public void mousePressed( MouseEvent event )
	{
		statusBar.setText( "Pressed at [" + event.getX() + ", " + event.getY() + "]" );
		
		cordenadaInicialX = foto.getMousePosition().x;
		cordenadaInicialY = foto.getMousePosition().y;
		
		System.out.println("Inicial "+cordenadaInicialX+" , "+cordenadaInicialY);
	}

	public void mouseReleased( MouseEvent event )
	{
		statusBar.setText( "Released at [" + event.getX() +  ", " + event.getY() + "]" );
		
		cordenadaFinalX = foto.getMousePosition().x;
		cordenadaFinalY = foto.getMousePosition().y;
		
		System.out.println("Final "+cordenadaFinalX+" , "+cordenadaFinalY);
		
		openFile();
		try 
		{
			salvarCordenadas();
		} 
		catch (IOException e) 
		{
			
		}
	}

	public void mouseEntered( MouseEvent event )
	{
		
	}

	public void mouseExited( MouseEvent event )
	{
		statusBar.setText( "Fora da  Foto" );
	}

	public void mouseDragged( MouseEvent event )
	{
		statusBar.setText( "Dragged at [" + event.getX() +  ", " + event.getY() + "]" );
	}
	

	public void mouseMoved( MouseEvent event )
	{
		statusBar.setText( "Moved at [" + event.getX() + ", " + event.getY() + "]" );
	}

	public static void main( String args[] )
	{ 
		MouseTracker application = new MouseTracker();

		application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	
}  

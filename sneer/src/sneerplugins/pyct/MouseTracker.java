package MigracaoSwing;



import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.*;

import Desenhando.SelfContainerPanel;


public class MouseTracker extends JFrame implements MouseListener, MouseMotionListener
{
	private JLabel statusBar;
	private JLabel foto;
	private int cordenadaInicialX;
	private int cordenadaFinalX;
	private int cordenadaInicialY;
	private int cordenadaFinalY;
	private String  cordenadas ;
	private Properties _properties = new Properties();
	public String nome;
	public String nomeImagemGeral;
	SelfContainerPanel desenho;
	public MouseTracker()
	{
		
		super( "pyct" );

		
		
	}
	
	public void executa(String nome, String nomeArquivo)
	{
		statusBar = new JLabel();
		getContentPane().add( statusBar, BorderLayout.SOUTH );
 
		Container container = getContentPane();
		container.setLayout( new FlowLayout() );

		Icon bug = new ImageIcon( nome );
		foto = new JLabel( bug, SwingConstants.CENTER );
		foto.setToolTipText( "************" );
   
		foto.addMouseListener( this );
		foto.addMouseMotionListener( this );
		container.add( foto );

		setSize( 1024, 760 );
		setVisible( true );
		String nomeImagem = "C:\\pyct\\" ;
		
		nomeImagem+=nomeArquivo;
		
		cordenadas = nomeArquivo+".properties";
		
		try {
			_properties.load(new FileInputStream(cordenadas));
				
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
		
		_properties.put(_contador ++ + "", " "+cordenadaInicialX+" "+ cordenadaInicialY+ " " + cordenadaFinalX+ " " + cordenadaFinalY + " " + nome+"" );
		_properties.store(new FileOutputStream(cordenadas), " ");
	}	
		
	private void openFile()
	{
		JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
	    
	    
	    
	    int result = fileChooser.showSaveDialog( this );
	   
	    File file = fileChooser.getSelectedFile();
	    
	    nome = fileChooser.getName(file);
	   
	    System.out.println(nome);
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

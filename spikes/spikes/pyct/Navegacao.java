package spikes.pyct;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Navegacao extends JFrame implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private JLabel statusBar;
	private JLabel foto;
	private JComboBox imagesComboBox;
	private String  cordenadas ;
	private Properties _properties = new Properties();
	public String _nome ;
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
	int cont = 0;
	String imageRelations ;
	AbreRelations _relations = new AbreRelations();
	String relationsNome;
	
	public void  navegacao()
	{
		if(cont == 0)	
		{
			cordenadas = "C:\\pyct\\"+nomeFoto5+".properties";
			imageRelations = "C:\\pyct\\"+nomeFoto5+".relations";
		}
		
		if(cont > 0)
		{
			cordenadas = "C:\\pyct\\"+nomeFoto5;
			imageRelations = "C:\\pyct\\"+nomeFoto5;
		}
		
		abreRelations();
		
		
		String nome = "C:\\pyct\\";
		try {
			
			abreArquivo.abreCordenadas(nome+nomeFoto5+".properties");
			
		} catch (NumberFormatException e) {
				
		} catch (IOException e) {
				
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
		
		try {
			_properties.load(new FileInputStream(cordenadas));
				
		} catch (IOException x) {x.printStackTrace();}
		cont ++;
	}
	
//	private void openFile()
//	{
//		JFileChooser fileChooser = new JFileChooser();
//	    fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG );
//	    
//	    fileChooser.showSaveDialog( this );
//	   
//	    File file = fileChooser.getSelectedFile();
//	    
//	    _nome = fileChooser.getName(file);
//	   
//	    System.out.println(_nome);
//	}  
	
	
	private void abreRelations()
	{
		String imagemRelation = null;
		
		
		try {
			
			_relations.abreCordenadas("C:\\pyct\\"+nomeFoto5+".relations");
			imagemRelation = _relations.caminho;
			
		}
		catch (NumberFormatException e) 
		{
				
		} 
		catch (IOException e) 
		{
				
		}
	
		
		String names[] = { "Fotos relacionadas", imagemRelation};
		final Icon icons[] = { new ImageIcon( names[ 0 ] ), new ImageIcon( names[ 1 ] ) };
		imagesComboBox = new JComboBox( names );
		imagesComboBox.setMaximumRowCount( 3 );
		imagesComboBox.addItemListener(
        
		     
		      new ItemListener() {

		     
		         public void itemStateChanged( ItemEvent event )
		         {
		     
					   if ( event.getStateChange() == ItemEvent.SELECTED )
		               foto.setIcon( icons[ imagesComboBox.getSelectedIndex() ] );
		         }

		      }  

		   ); 

		   container.add( imagesComboBox );

	}
	
	
	public static void main(String[] args) {
		Navegacao navegacao = new Navegacao();
		navegacao.navegacao();
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
				container.remove(imagesComboBox);
				String nomeImagemAberta = abreArquivo.vetorCaminho[indice];
				nomeFoto = nomeImagemAberta;
				nomeImagem2 = abreArquivo.vetorCaminho[indice];
				nomeFoto5 = abreArquivo.nome;
				System.out.println("**"+nomeFoto5);
				navegacao();
				
				
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

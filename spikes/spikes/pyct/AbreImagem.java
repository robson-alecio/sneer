package spikes.pyct;


import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class AbreImagem extends JFrame {

	private JLabel label2;


	public AbreImagem( String caminho)
	{
	   super( "Pyct" );

	   caminho = "C:\\pyct\\a";
	   Container container = getContentPane();
	   container.setLayout( new FlowLayout() );

	   Icon bug = new ImageIcon( caminho );
	   label2 = new JLabel( bug, SwingConstants.CENTER );
	   label2.setToolTipText( "Essa e a amazonia" );
	   container.add( label2 );

	   

	   setSize( 275, 170 );
	   setVisible( true );
	}
	
	private static final long serialVersionUID = 1L;

	}  
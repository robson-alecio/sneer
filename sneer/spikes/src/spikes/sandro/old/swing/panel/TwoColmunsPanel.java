package spikes.sandro.old.swing.panel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import spikes.sandro.old.gui.community.share.SharePanel;
import spikes.sandro.old.image.IconFactory;
import spikes.sandro.old.swing.layout.StackLayout;

public class TwoColmunsPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private JPanel top;
	private JPanel botton;
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	
	private int lineNumber = 0;
	
	public TwoColmunsPanel(JPanel pleftPanel, JPanel prightPanel) {
		this.leftPanel=pleftPanel;
		this.rightPanel=prightPanel;
		botton = new JPanel();
		top = new JPanel();
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new StackLayout());

		this.add(top, StackLayout.TOP);
		this.add(botton, StackLayout.BOTTOM);

		top.setLayout(new GridBagLayout());
		botton.setLayout(new GridBagLayout());
		
		this.setOpaque(false);
		top.setOpaque(false);
		botton.setOpaque(false);
		

        GridBagConstraints c0 = new GridBagConstraints();
        c0.gridx = 0;
        c0.fill = GridBagConstraints.BOTH;
        c0.weightx = 0.0;
        c0.weighty = 0.0;
        c0.gridy = 0;
        c0.ipadx = 120;
        
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 1;
        c1.fill = GridBagConstraints.BOTH;
        c1.weightx = 1.0;
        c1.weighty = 1.0;
        c1.gridy = 0;

        botton.add(leftPanel, c0);
        botton.add(rightPanel, c1);
	}

	public void addLine(String smallIconName, String bigIconName, JPanel pnl) {
		addLine(smallIconName, bigIconName, pnl, 20, -30);
	}	
	
	public void addLine(String smallIconName, String bigIconName, JPanel pnl, int offsetX, int offsetY) {
		
		GridBagConstraints cLeft = new GridBagConstraints();
		cLeft.insets = new Insets(0, 0, 0, 0);
		cLeft.weightx = 0.0;
		cLeft.weighty = 1.0;
		cLeft.ipadx = 35;
		cLeft.gridy = lineNumber;	
		
		GridBagConstraints cRight = new GridBagConstraints();
		cLeft.insets = new Insets(5, 10, 5, 10);
		cRight.weightx = 1.0;
		cRight.weighty = 1.0;
		cRight.gridy = lineNumber;	
		cRight.fill = GridBagConstraints.BOTH;
	
		JPanel desc = new JPanel();	
		desc.setBorder(BorderFactory.createLineBorder(Color.white));
		desc.setOpaque(false);
		desc.setLayout(new GridBagLayout());

		JLabel lb = new AutoZoomImage(IconFactory.getIcon(smallIconName,true),
								      IconFactory.getIcon(bigIconName,true),
								      offsetX,offsetY);
		desc.add(lb, cLeft);
		desc.add(pnl, cRight);

		GridBagConstraints cTop = new GridBagConstraints();
		cTop.insets = new Insets(5, 10, 5, 10);
		cTop.weightx = 1.0;
		cTop.weighty = 1.0;
		cTop.gridy = lineNumber;
		cTop.fill = GridBagConstraints.BOTH;
		top.add(desc,cTop);			
		
		if(lineNumber==0){
			cTop.insets = new Insets(5, 10, 5, 10);
		}
		
		lineNumber++;
	}

	public JPanel getLeftPanel() {
		return leftPanel;
	}

	public JPanel getRightPanel() {
		return rightPanel;
	}
	
	public static void main(String[] args){	
		
		JFrame frame = new JFrame();		
		
		Color c1 = new Color( 40,  20, 110, 100);
		Color c2 = new Color(130, 170, 210, 50);
		
		JPanel  gradientPnl = new GradientPanel(c1,	c2, GradientPanel._TOP_DOWN);
		gradientPnl.setLayout(new GridBagLayout());
		gradientPnl.setOpaque(false);
		
		JPanel descPnl = new JPanel();
		descPnl.setLayout(new GridBagLayout());
		descPnl.setOpaque(false);
		
		TwoColmunsPanel panel = new TwoColmunsPanel(gradientPnl, descPnl);
		
//		TransparentPanel tp =new TransparentPanel();
//		tp.setRefreshFactor(10000);
//		frame.setContentPane(tp);
		frame.getContentPane().setBackground(c2);
		frame.getContentPane().setLayout(new StackLayout());		
		frame.getContentPane().add(panel, StackLayout.TOP);

		JPanel pnlVideo = new SharePanel("V�deos","Compartilhe seus V�deos! Quem sabe alguma v�deo cassetada, ou aquela aula que voc� gravou.");
		JPanel pnlFoto = new SharePanel("Fotos","Compartilhe suas Fotos! Que tal compartilhar as fotos da turma toda depois da viagem de f�rias?");
		JPanel pnlAudio = new SharePanel("Audio", "Voc� pode compartilhar alguma grava��o ou mesmo suas m�sicas preferidas");
		JPanel pnlFile = new SharePanel("Arquivos", "Aqui voc� disponibiliza, programas, arquivos ou mesmo jogos.");
	
		panel.addLine("small/cam.png",   "big/cam.png", pnlVideo);
		panel.addLine("small/camera.png","big/camera.png", pnlFoto);
		panel.addLine("small/xmms.png",  "big/xmms.png", pnlAudio);
		panel.addLine("small/folder_documents.png", "big/folder_documents.png", pnlFile);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setBounds(50, 50, 450, 500);
		frame.setVisible(true);	
	}
} 

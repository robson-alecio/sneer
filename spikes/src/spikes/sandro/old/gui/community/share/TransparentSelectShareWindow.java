package spikes.sandro.old.gui.community.share;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


import spikes.sandro.old.swing.layout.StackLayout;
import spikes.sandro.old.swing.panel.GradientPanel;
import spikes.sandro.old.swing.panel.TwoColmunsPanel;

public class TransparentSelectShareWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public TransparentSelectShareWindow() {
		Color c1 = new Color( 40,  20, 110);
		Color c2 = new Color(130, 170, 210);
		
		JPanel  gradientPnl = new GradientPanel(c1,	c2, GradientPanel._TOP_DOWN);
		gradientPnl.setLayout(new GridBagLayout());
		gradientPnl.setOpaque(false);
		
		JPanel descPnl = new JPanel();
		descPnl.setLayout(new GridBagLayout());
		descPnl.setOpaque(false);
		
		TwoColmunsPanel panel = new TwoColmunsPanel(gradientPnl, descPnl);
		
		getContentPane().setBackground(c2);
		getContentPane().setLayout(new StackLayout());		
		getContentPane().add(panel, StackLayout.TOP);

		JPanel pnlVideo = new SharePanel("V�deos","Compartilhe seus V�deos! Quem sabe alguma v�deo cassetada, ou aquela aula que voc� gravou.");
		JPanel pnlFoto = new SharePanel("Fotos","Compartilhe suas Fotos! Que tal compartilhar as fotos da turma toda depois da viagem de f�rias?");
		JPanel pnlAudio = new SharePanel("Audio", "Voc� pode compartilhar alguma grava��o ou mesmo suas m�sicas preferidas");
		JPanel pnlFile = new SharePanel("Arquivos", "Aqui voc� disponibiliza, programas, arquivos ou mesmo jogos.");
	
		panel.addLine("small/cam.png",   "big/cam.png", pnlVideo);
		panel.addLine("small/camera.png","big/camera.png", pnlFoto);
		panel.addLine("small/xmms.png",  "big/xmms.png", pnlAudio);
		panel.addLine("small/folder_documents.png", "big/folder_documents.png", pnlFile);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	public static void main(String[] args) {
		TransparentSelectShareWindow dlg = new TransparentSelectShareWindow();
		dlg.setBounds(50, 50, 480, 480);
		dlg.setVisible(true);
		dlg.requestFocus();
	}
}

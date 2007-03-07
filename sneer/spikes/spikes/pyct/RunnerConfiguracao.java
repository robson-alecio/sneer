package spikes.pyct;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class RunnerConfiguracao {
	
	public void executa() {
		String nome ="C:\\pyct\\";
		String nomeArquivo;
		JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG );
	    
	    fileChooser.showSaveDialog(fileChooser  );
	   
	    File file = fileChooser.getSelectedFile();
	    
	    nomeArquivo= fileChooser.getName(file);
	    nome+=nomeArquivo;
	    System.out.println(nome);
	    MouseTracker configuracao = new MouseTracker();
	    configuracao.executa(nome,nomeArquivo);
	}
	
	public static void main(String[] args) {
		
		String nome ="C:\\pyct\\a";
		String nomeArquivo = "a";
		
	    MouseTracker configuracao = new MouseTracker();
	    configuracao.executa(nome,nomeArquivo);
	    
	    configuracao.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

}

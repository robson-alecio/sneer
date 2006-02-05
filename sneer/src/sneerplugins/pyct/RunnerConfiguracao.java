package MigracaoSwing;

import java.io.File;

import javax.swing.JFileChooser;


public class RunnerConfiguracao {

	public static void main(String[] args) {
		String nome ="C:\\pyct\\";
		String nomeArquivo;
		JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
	    
	    int result = fileChooser.showSaveDialog(fileChooser  );
	   
	    File file = fileChooser.getSelectedFile();
	    
	    nomeArquivo= fileChooser.getName(file);
	    nome+=nomeArquivo;
	    System.out.println(nome);
	    
	    MouseTracker configuracao = new MouseTracker();
	    configuracao.executa(nome,nomeArquivo);
	}

}

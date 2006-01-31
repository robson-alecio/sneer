package sneer.ui.plugins.pyct;

import java.io.IOException;

public class CompilaAbre {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		AbreArquivos obj = new AbreArquivos();
		obj.abreCordenadas("C:\\pyct\\a.properties");
		
	}

}

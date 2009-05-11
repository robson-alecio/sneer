package spikes.klaus.prevaylerjr;

import java.util.ArrayList;
import java.util.List;

public class CadastroDeNomes implements Sistema {
	
	private final List<String> _nomes = new ArrayList<String>();
	
	public String processa(String comando) {
		_nomes.add(comando);
		
		return listarNomes();
	}

	private String listarNomes() {
		String result = "";
		for (String nome : _nomes)
			result = result + nome + "\n";
		return result;
	}

}

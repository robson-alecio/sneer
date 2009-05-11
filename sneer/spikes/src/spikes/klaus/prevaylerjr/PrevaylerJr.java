package spikes.klaus.prevaylerjr;

import java.io.IOException;
import java.io.RandomAccessFile;


public class PrevaylerJr {

	static public Sistema tornarInvulneravel(Sistema sistema) throws IOException {
		return new SistemaInvulneravel(sistema);
	}
}

class SistemaInvulneravel implements Sistema {

	private final Sistema _delegado;
	private RandomAccessFile _journal;

	SistemaInvulneravel(Sistema sistema) throws IOException {
		_delegado = sistema;
		_journal = new RandomAccessFile("comandosJournal", "rwd");
		
		recuperaEstado();
	}

	@Override
	public String processa(String comando) {
		armazena(comando);
		return _delegado.processa(comando);
	}
	
	private void recuperaEstado() throws IOException {
		while (true) {
			String comandoArmazenado = _journal.readLine();
			if (comandoArmazenado == null) break;
			_delegado.processa(comandoArmazenado);
		}
	}

	private void armazena(String comando) {
		try {
			_journal.write((comando + "\n").getBytes("utf8"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
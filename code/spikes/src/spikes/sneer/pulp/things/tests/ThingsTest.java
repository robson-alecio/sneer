package spikes.sneer.pulp.things.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Assert;
import org.junit.Test;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.testsupport.Daemon;
import spikes.sneer.pulp.things.Thing;
import spikes.sneer.pulp.things.ThingHome;
import spikes.wheel.testutil.TestDashboard;

@SuppressWarnings("deprecation")
public class ThingsTest extends BrickTest {
	
	private final ThingHome _subject = my(ThingHome.class);

	private static final int _THINGS_TO_ADD = 50;
	private static final String _LARGE_TEXT = generateLargeText();
	private volatile boolean _isConcurrencyTestDone = false;
	 
	@Test (timeout = 5000)
	public void testAddAndFind() {
		createApartmentAds();
		searchApartmentAds();
	}
	
	@Test (timeout = 120000)
	public void testConcurrency(){
		if (!TestDashboard.loadTestsShouldRun()) return;
		
		startAddingThread();
		startAddingThread();
		startAddingThread();
		startAddingThread();
		startAddingThread();
		startFindingThread(5);
		startFindingThread(5);
		startFindingThread(5);
		startFindingThread(5);
		startFindingThread(5);
		
		while (!_isConcurrencyTestDone)
			my(Threads.class).sleepWithoutInterruptions(10);

		System.out.println();

	}

	private void startAddingThread() {
		new Daemon("Thing Adder") { @Override public void run() {
			int i = 0;
			while (i <= _THINGS_TO_ADD) {
				_subject.create("thing" + i, "description thing" + i + " " + _LARGE_TEXT);
				i++;
				if (i % 5 == 0) {
					System.out.print(".");
					my(Threads.class).sleepWithoutInterruptions(50);
				}
			}
		}};
	}

	private void startFindingThread(final int thingsToFind) {
		new Daemon("Thing Finder") { @Override public void run() {
			int i = 0;
			while (i < _THINGS_TO_ADD) {
				SetSignal<Thing> found = _subject.search("thing"+i);
				
				int count = found.size().currentValue();
				Assert.assertTrue(count <= thingsToFind);
				
				if (count == thingsToFind) {
					i++;
				} else
					my(Threads.class).sleepWithoutInterruptions(10);
			}
			_isConcurrencyTestDone = true;
		}};
	}

	private void searchApartmentAds() {
		find("Apartamento", 3);
		find("+Apartamento +mobiliado", 1);
		
		find("Sala consultorio +3 +quartos", 3);
		find("Sala consultorio +\"3 quartos\" ", 2);
		find("\"Sala banheiro janelas\" ", 0);

		find("+Juveve", 1);
		find("+Juveves", 0);

	}

	private void find(String tags, int thingsToFind) {
		SetSignal<Thing> found = _subject.search(tags);
		
		Assert.assertSame(thingsToFind, found.size().currentValue());
	}

	private void createApartmentAds() {
		String name;
		String description;

		name = "Apartamento Juveve 2 Quartos";
		description = "apartamento Sala Comercial em Bom estado. Bom para consultorio. Face sul. Sito a Av Joao Gualberto 1673 sala 41. R$42700,00 a vista. Aceita troca por carro de menor valor.";
		_subject.create(name, description);
		
		name = "Apartamento Residencial - Bacacheri";
		description = "Valor: R$ 194.500,00 Area: 163,00m2, mobiliado, ocupado, com playground, BELISSIMO APTO CONTENDO 3 QUARTOS SENDO 1 SUITE COM ARMARIOS E PISO EM MADEIRA, AMPLA SALA EM (L) TAMBEM COM PISO EM MADEIRA, SANCAS DE GESSO, PINTURA TEXTURIZADA, QUARTOS DE SOLTEIRO COM ARMARIOS,ESQUADRIS DE ALUMINIO, COZINHA COM ARMARIOS E AQUECIMENTO, PREDIO COM 02 SALOES DE FESTAS, 02 CHURRASQUEIRAS COLETIVAS, SALAO DE JOGOS E SALA DE GINASTICA, ARQUITETURA MODERNA E PASTILHADA. PROXIMO AO WALL MART DO CABRAL, BANCOS ESCOLAS FARMACIAS E COMERCIO EM GERAL. ESTUDO PROPOSTA COM IMOVEL DE MAIOR VALOR EM CONDOMINIO FECHADO. CONFIRA E FACA SUA PROPOSTA";
		_subject.create(name, description);

		name = "Apartamento Residencial - Barreirinha";
		description = "Valor: R$ 66.000,00 Area: 50,00m2 apartamento no Cond. Sta. Efigenia II, 3º andar, 02 quartos, sala, cozinha, Area servico e bwc, piso taco, proximo ao Terminal Barreirinha, com onibus, escola, Posto de Saúde. Rua Professor Guilherme Butler";
		_subject.create(name, description);
		
		name = "Casa Residencial - Atuba";
		description = "Valor: R$ 130.000,00 Area: 120,00m2 Casa com 3 quartos sala cozinha banheiro com piso em parquet, janelas de ferro com grade toda murada e com grade na frente, quintal com deposito em alvenaria, cozinha com armarios.";
		_subject.create(name, description);
	}

	private static String generateLargeText() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < 1000; i++)
			result.append("word" + i + " ");
		return result.toString();
	}
}

package sneer.pulp.things.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.commons.threads.Daemon;
import sneer.pulp.things.Thing;
import sneer.pulp.things.ThingHome;
import wheel.lang.Threads;
import wheel.reactive.sets.SetSignal;
import wheel.testutil.TestDashboard;

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
			Threads.sleepWithoutInterruptions(10);

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
					Threads.sleepWithoutInterruptions(50);
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
					Threads.sleepWithoutInterruptions(10);
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

		find("+Juvevê", 1);
		find("+Juveve", 0);

		
	}

	private void find(String tags, int thingsToFind) {
		SetSignal<Thing> found = _subject.search(tags);
		
		Assert.assertSame(thingsToFind, found.size().currentValue());
	}

	private void createApartmentAds() {
		String name;
		String description;

		name = "Apartamento Juvevê 2 Quartos";
		description = "apartamento Sala Comercial em Bom estado. Bom para consultorio. Face sul. Sito a Av João Gualberto 1673 sala 41. R$42700,00 a vista. Aceita troca por carro de menor valor.";
		_subject.create(name, description);
		
		name = "Apartamento Residencial - Bacacheri";
		description = "Valor: R$ 194.500,00 Area: 163,00m², mobiliado, ocupado, com playground, BELÍSSIMO APTO CONTENDO 3 QUARTOS SENDO 1 SUITE COM ARMÁRIOS E PISO EM MADEIRA, AMPLA SALA EM (L) TAMBEM COM PISO EM MADEIRA, SANCAS DE GESSO, PINTURA TEXTURIZADA, QUARTOS DE SOLTEIRO COM ARMÁRIOS,ESQUADRIS DE ALUMINIO, COZINHA COM ARMÁRIOS E AQUECIMENTO Á GÁS, PREDIO COM 02 SALÕES DE FESTAS, 02 CHURRASQUEIRAS COLETIVAS, SALÃO DE JOGOS E SALA DE GINASTICA, ARQUITETURA MODERNA E PASTILHADA. PRÓXIMO AO WALL MART DO CABRAL, BANCOS ESCOLAS FARMÁCIAS E COMERCIO EM GERAL. ESTUDO PROPOSTA COM IMÓVEL DE MAIOR VALOR EM CONDOMINIO FECHADO. CONFIRA E FAÇA SUA PROPOSTA";
		_subject.create(name, description);

		name = "Apartamento Residencial - Barreirinha";
		description = "Valor: R$ 66.000,00 Area: 50,00m² apartamento no Cond. Sta. Efigênia II, 3º andar, 02 quartos, sala, cozinha, área serviço e bwc, piso taco, próximo ao Terminal Barreirinha, com ônibus, escola, Posto de Saúde. Rua Professor Guilherme Butler";
		_subject.create(name, description);
		
		name = "Casa Residencial - Atuba";
		description = "Valor: R$ 130.000,00 Area: 120,00m² Casa com 3 quartos sala cozinha banheiro com piso em parquet, janelas de ferro com grade toda murada e com grade na frente, quintal com deposito em alvenaria, cozinha com armarios.";
		_subject.create(name, description);
	}

	private static String generateLargeText() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < 1000; i++)
			result.append("word" + i + " ");
		return result.toString();
	}

	
}

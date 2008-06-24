package spikes.danielsantos.things;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class ThingsTest {

	private final List<Thing> _apAds = new ArrayList<Thing>();

	@Test
	public void test() {
		//if (!TestDashboard.newTestsShouldRun()) return;
		createApartmentAds();
		searchApartmentAds();
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
		Finder finder = new Finder();
		Collection<Thing> found = finder.find(_apAds, tags);
		
		assertSame(thingsToFind, found.size());
	}

	private void createApartmentAds() {
		String name;
		String description;

		name = "Apartamento Juvevê 2 Quartos";
		description = "apartamento Sala Comercial em Bom estado. Bom para consultorio. Face sul. Sito a Av João Gualberto 1673 sala 41. R$42700,00 a vista. Aceita troca por carro de menor valor.";
		_apAds.add(new Thing(name, description));
		
		name = "Apartamento Residencial - Bacacheri";
		description = "Valor: R$ 194.500,00 Area: 163,00m², mobiliado, ocupado, com playground, BELÍSSIMO APTO CONTENDO 3 QUARTOS SENDO 1 SUITE COM ARMÁRIOS E PISO EM MADEIRA, AMPLA SALA EM (L) TAMBEM COM PISO EM MADEIRA, SANCAS DE GESSO, PINTURA TEXTURIZADA, QUARTOS DE SOLTEIRO COM ARMÁRIOS,ESQUADRIS DE ALUMINIO, COZINHA COM ARMÁRIOS E AQUECIMENTO Á GÁS, PREDIO COM 02 SALÕES DE FESTAS, 02 CHURRASQUEIRAS COLETIVAS, SALÃO DE JOGOS E SALA DE GINASTICA, ARQUITETURA MODERNA E PASTILHADA. PRÓXIMO AO WALL MART DO CABRAL, BANCOS ESCOLAS FARMÁCIAS E COMERCIO EM GERAL. ESTUDO PROPOSTA COM IMÓVEL DE MAIOR VALOR EM CONDOMINIO FECHADO. CONFIRA E FAÇA SUA PROPOSTA";
		_apAds.add(new Thing(name, description));

		name = "Apartamento Residencial - Barreirinha";
		description = "Valor: R$ 66.000,00 Area: 50,00m² apartamento no Cond. Sta. Efigênia II, 3º andar, 02 quartos, sala, cozinha, área serviço e bwc, piso taco, próximo ao Terminal Barreirinha, com ônibus, escola, Posto de Saúde. Rua Professor Guilherme Butler";
		_apAds.add(new Thing(name, description));
		
		name = "Casa Residencial - Atuba";
		description = "Valor: R$ 130.000,00 Area: 120,00m² Casa com 3 quartos sala cozinha banheiro com piso em parquet, janelas de ferro com grade toda murada e com grade na frente, quintal com deposito em alvenaria, cozinha com armarios.";
		_apAds.add(new Thing(name, description));
	}

}

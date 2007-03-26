package sneer.kernel.install;

import sneer.kernel.SneerDirectories;
import wheel.io.ui.User;

public class InstallationDialog extends Dialog {

	public InstallationDialog(User user) {
		super(user);
		
		approveConditionOtherwiseExit(
				" Welcome to the Sneer Setup Wizard\n\n" +
				" This will install Sneer on your computer.",
				"Next >", "Cancel"
		);

		approveConditionOtherwiseExit(
				" Sneer is free software.\n\n" +
				" It is licensed under the terms of the General Public License version 2" +
				" as published by the Free Software Foundation:\n" +
				" http://www.gnu.org/copyleft/gpl.html\n\n" +
				" Do you accept these terms?",
				"I Accept", "No"
		);

		int TODO;
		
		approveConditionOtherwiseExit(
				" Será criado o diretório\n" +
				" " + SneerDirectories.sneerDirectory() + "\n\n" + 
				" Lá serão guardados, para este usuário, todos os dados e\n" +
				" programas do Sneer.\n\n" +
				" Este diálogo não será exibido novamente enquanto existir esse\n" +
				" diretório.",
				"Criar diretório", "Não criar"
		);
		
		approveConditionOtherwiseExit(
				" Esta versão mínima do Sneer tem uma única funcionalidade:\n" +
				" conectar-se ao servidor do projeto, baixar e executar suas atualizações.\n\n" +
				" Isso será feito automaticamente. Tratando-se do Sneer e todos seus\n" +
				" plugins soberanos, portanto, você nunca mais vai precisar instalar nada\n" +
				" para este usuário, nesta máquina.",
				"Massa"
		);

		approveConditionOtherwiseExit(
				" Futuramente, o Sneer será capaz de baixar suas atualizações com\n" +
				" segurança, através de qualquer contato soberano que você tenha.\n\n" +
				" Por enquanto, o Sneer vai baixar atualizações, quando houver, de\n" +
				" [server] na porta [porta].\n\n" +
				" Ainda não há checagem de segurança ou criptografia alguma para as\n" +
				" atualizações. A segurança é a mesma que você tem quando baixa\n" +
				" executáveis de sites web sem o cadeadinho (ex.: Sourceforge).\n\n" +
				" Beleza?",
				"Beleza", "Nem a pau"
		);

		approveConditionOtherwiseExit(
				" As tentativas de atualização são logadas num arquivo de log fácil de\n" +
				" achar, dentro do diretório do Sneer.\n\n" +
				" Não entre em pânico. O servidor de atualizações nem sempre está ligado.",
				"OK"
		);

		approveConditionOtherwiseExit(
				" Quando não encontra atualização, o Sneer simplesmente encerra em silêncio. Não\n" +
				" consome recursos da máquina, portanto.\n\n" +
				" Por favor, coloque o Sneer para ser executado na inicialização da sua máquina.\n" +
				" Linux: Chamar \"java -jar Sneer.jar\" num runlevel script.\n" +
				" Windows: Criar atalho para Sneer.jar no menu \"Iniciar > Programas > Inicializar\"",
				"Já coloquei", "Não sei colocar"
		);
		
		goodbye();
		
	}

}

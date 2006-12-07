package sneer.boot;

import javax.swing.JOptionPane;

public class User {

	void approveInstallationOtherwiseExit() {
		
		approveConditionOtherwiseExit(
				" Bem-vind@ ao Sneer, o peer soberano.\n\n" +
				" Pioneir@ da computação soberana, você poderá contar\n" +
				" para seus netos que participou da revolução desde a\n" +
				" primeira versão da primeira plataforma soberana.",
				"Legal  :)"
		);

		approveConditionOtherwiseExit(
				" Esta primeira versão é pouco mais que vaporware,\n" +
				" exibe informações técnicas detalhadas e só faz\n" +
				" sentido pra usuários avançados.\n\n" +
				" Por favor, não a divulgue fora desse público.",
				"Tá bom"
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
				" klaus.selfip.net na porta 4242.\n\n" +
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

		approveConditionOtherwiseExit(
				" Sabe aqueles textos gigantescos e pentelhos de licença, que aparecem\n" +
				" na instalação de todo software e que ninguém lê?\n\n" +
				" Usando software soberano, você nunca mais vai precisar ver um texto\n" +
				" daquele na sua frente.",
				" Uhu! Por que não?"
		);
		
		approveConditionOtherwiseExit(
				" O Sneer é software livre, licenciado pela General Public License 2 (GPL 2):\n" +
				" http://www.gnu.org/copyleft/gpl.html\n\n" +
				" Todas as versões do Sneer e de todos os plugins soberanos, que também\n" +
				" forem licenciados pela GPL 2, serão instalados e executados sem exibir o\n" +
				" texto da licença.\n\n" +
				" Você aceita esses termos?",
				"Claro", "Claro que não"
		);

		approveConditionOtherwiseExit(
				" Será criado um diretório chamado \".sneer\" dentro do diretório\n" +
				" " + homeDirectory() + "\n\n" + 
				" Lá serão guardados, para este usuário, todos os dados e\n" +
				" programas do Sneer.\n\n" +
				" Este diálogo não será exibido novamente enquanto existir esse\n" +
				" diretório.",
				"Criar diretório", "Não criar"
		);
		
		goodbye();

	}


	void goodbye() {
		choose(
				" Obrigado e até a próxima atualização do Sneer.  ;)",
				"Falou"
		);
	}


	private void approveConditionOtherwiseExit(String condition, Object... options) {
		boolean approved = choose(condition, options);
		if (!approved) exit();
	}


	protected boolean choose(String proposition, Object... options) {
		int chosen = JOptionPane.showOptionDialog(null, proposition + "\n\n", "Sneer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		return chosen == 0;
	}


	private void exit() {
		exit(" O Sneer será encerrado sem alterações em seu sistema.\n\n" +
				" Por favor, conte-nos o motivo da sua desistência na\n" +
				" lista do sneercoders no googlegroups."
		);
	}

	
	void exit(String goodbyeMessage) {
		choose(	goodbyeMessage, "Sair");
		System.exit(0);
	}

	
	String homeDirectory() {
		return System.getProperty("user.home");
	}


	void acknowledgeUnexpectedProblem(String description) {
		JOptionPane.showMessageDialog(null, description, "Sneer - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
	}
	
}

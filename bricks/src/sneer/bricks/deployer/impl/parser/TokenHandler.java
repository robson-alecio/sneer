package sneer.bricks.deployer.impl.parser;

interface TokenHandler {

	void handleNumber(double nval);

	void handleWord(String sval);

	void handleDoubleQuoted(String sval);

	void handleSingleQuoted(String sval);

	void handleEOF();

	void handleChar(char ttype);

}

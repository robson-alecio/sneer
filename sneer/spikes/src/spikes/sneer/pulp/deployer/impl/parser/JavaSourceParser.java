package spikes.sneer.pulp.deployer.impl.parser;

import java.io.File;
import java.io.IOException;

/**
 * Really naive source parser
 */
public class JavaSourceParser implements TokenHandler {

	private static final Tokenizer _tokenizer = new Tokenizer();

	private File _sourceFile;
	
	private JavaSource _source;
	
	private boolean _farAway = false;
	
	private String _previoustWord;
	private String _currentWord;
	
	@SuppressWarnings("unused")
	private char _previousChar;
	private char _currentChar;
	
	
	public JavaSourceParser(File sourceFile) {
		_sourceFile = sourceFile;
	}

	public JavaSource parse() throws IOException {
		_source = new JavaSource(_sourceFile);
		_tokenizer.parse(_sourceFile, this);
		return _source;
	}

	@Override
	public void handleChar(char c) {
		
		_previousChar = _currentChar;
		_currentChar = c;
		//System.out.print("c:"+ttype+" ");

		if(!_farAway && '{' == c) {
			_farAway = true;
		}
	}

	@Override
	public void handleWord(String sval) {
		
		_previoustWord = _currentWord;
		_currentWord = sval;
		//System.out.print("w:"+sval+" ");
		
		if(_farAway) return;
		
		if("interface".equals(sval) || "class".equals(sval)) {
			_source.setType(sval);
			_source.setAccessType(_previoustWord);
		}
	}

	@Override
	public void handleDoubleQuoted(String sval) {
	}

	@Override
	public void handleEOF() {
	}

	@Override
	public void handleNumber(double nval) {
	}

	@Override
	public void handleSingleQuoted(String sval) {
	}
}

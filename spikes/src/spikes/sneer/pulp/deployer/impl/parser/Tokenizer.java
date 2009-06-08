package spikes.sneer.pulp.deployer.impl.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

class Tokenizer {

	void parse(File file, TokenHandler handler) throws IOException {
		FileReader reader = new FileReader(file);
		StreamTokenizer st = new StreamTokenizer(reader);
		    
		st.parseNumbers();
		st.wordChars('_', '_');
		st.eolIsSignificant(true);
		    
		// If whitespace is not to be discarded, make this call
		//st.ordinaryChars(0, ' ');
		    
		// These calls caused comments to be discarded
		st.slashSlashComments(true);
		st.slashStarComments(true);
		    
		// Parse the file
		int token = st.nextToken();
		while (token != StreamTokenizer.TT_EOF) {
			token = st.nextToken();
			switch (token) {
			case StreamTokenizer.TT_NUMBER:
				handler.handleNumber(st.nval);
				break;
			case StreamTokenizer.TT_WORD:
				handler.handleWord(st.sval);
				break;
			case '"':
				handler.handleDoubleQuoted(st.sval);
				break;
			case '\'':
				handler.handleSingleQuoted(st.sval);
				break;
			case StreamTokenizer.TT_EOL:
				// End of line character found
				break;
			case StreamTokenizer.TT_EOF:
				handler.handleEOF();
				break;
			default:
				handler.handleChar((char)st.ttype);
			break;
			}
		}
		reader.close();
	}
}

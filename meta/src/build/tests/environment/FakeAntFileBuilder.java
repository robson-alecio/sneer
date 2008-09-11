package build.tests.environment;

import build.antFileGenerator.AntFileBuilder;

public class FakeAntFileBuilder implements AntFileBuilder {

	private final StringBuffer _statements = new StringBuffer();
	private final StringBuffer _built = new StringBuffer();

	public String getStatements() {
		return _built.toString();
	}

	@Override
	public void addClasspathEntry(final String lib) {
		if (_statements.length()!=0)
			_statements.append("\n");
		
		_statements.append("lib " + lib );
	}

	@Override
	public void addCompileEntry(final String src) {
		_statements.append("\n");
		_statements.append("compile " + src );
	}

	@Override
	public void build() {
		_built.append(_statements.toString());
	}

}

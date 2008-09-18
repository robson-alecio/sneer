package build.tests.environment;

import org.apache.commons.lang.StringUtils;

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
	public void addCompileEntry(final String src, final String output) {
		_statements.append("\n");
		_statements.append("compile " + src);
		if (!StringUtils.isEmpty(output)) {
			_statements.append(" to " + output);
		}
	}

	@Override
	public void build() {
		_built.append(_statements.toString());
	}

}

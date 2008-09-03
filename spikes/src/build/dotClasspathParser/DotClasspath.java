package build.dotClasspathParser;

import java.util.List;

public class DotClasspath {

	private final List<String> _sourcePaths;
	private final List<String> _libs;

	public DotClasspath(final List<String> sourcePaths, final List<String> libs) {
		_sourcePaths = sourcePaths;
		_libs = libs;
	}


	public List<String> getLibs() {
		return _libs;
	}

	public List<String> getSrcs() {
		return _sourcePaths;
	}


	

}

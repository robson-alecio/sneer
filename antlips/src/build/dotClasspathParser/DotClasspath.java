package build.dotClasspathParser;

import java.util.List;

public class DotClasspath {
	
	public static Entry entry(String src) {
		return new Entry(src, null);
	}
	
	public static Entry entry(String src, String output) {
		return new Entry(src, output);
	}
	
	public static class Entry {
		public final String _src;
		public final String _output;
		
		private Entry(String src, String output) {
			_src = src;
			_output = output;
		}
		
	}

	private final List<Entry> _sourcePaths;
	private final List<String> _libs;

	public DotClasspath(final List<Entry> sourcePaths, final List<String> libs) {
		_sourcePaths = sourcePaths;
		_libs = libs;
	}


	public List<String> getLibs() {
		return _libs;
	}

	public List<Entry> getSrcs() {
		return _sourcePaths;
	}


	

}

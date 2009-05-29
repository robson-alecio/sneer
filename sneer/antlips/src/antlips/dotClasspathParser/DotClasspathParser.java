package antlips.dotClasspathParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;


public class DotClasspathParser {

	private static final Pattern _libPattern = Pattern.compile(".+classpathentry kind=\"lib\"(.+)?\\spath=\"([^\"]+)\".+");
	private static final Pattern _sourcePattern = Pattern.compile(".+classpathentry kind=\"src\"(?:\\soutput=\"(.+)\")?(\\s)?path=\"(.+)\".+");

	public static DotClasspath parse(final String classpathString) {

		Validate.notNull(classpathString, "classpathString was null");
		Validate.notEmpty(classpathString, "classpathString was empty");

		final List<DotClasspath.Entry> sourcePaths = getSrcPaths(classpathString);
		final List<String> libs = getLibs(classpathString);
		
		final DotClasspath dotClassPath = new DotClasspath(sourcePaths, libs);

		return dotClassPath;
	}

	private static List<String> getLibs(final String classpathString) {
		final Matcher matcher = _libPattern.matcher(classpathString);

		final List<String> libPaths = new ArrayList<String>();
		while (matcher.find()){
			final String lib = matcher.group(2);
			libPaths .add(lib);
		}

		return libPaths;
	}

	private static List<DotClasspath.Entry> getSrcPaths(final String classpathString) {
		final Matcher matcher = _sourcePattern.matcher(classpathString);

		final List<DotClasspath.Entry> sourcePaths = new ArrayList<DotClasspath.Entry>();
		while (matcher.find()){
			final String outputPath = matcher.group(1);
			final String srcPath = matcher.group(3);
			sourcePaths.add(DotClasspath.entry(srcPath, outputPath));
		}

		return sourcePaths;
	}
}

package build.dotClasspathParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;


public class DotClasspathParser {
	
	public static DotClasspath parse(final String classpathString) {

		Validate.notNull(classpathString, "classpathString was null");
		Validate.notEmpty(classpathString, "classpathString was empty");

		final List<String> sourcePaths = getSrcPaths(classpathString);
		final List<String> libs = getLibs(classpathString);
		
		final DotClasspath dotClassPath = new DotClasspath(sourcePaths, libs);

		

		return dotClassPath;

	}

	private static List<String> getLibs(final String classpathString) {
		final Pattern sourcePattern = Pattern.compile(".+classpathentry kind=\"lib\"(.+)?\\spath=\"([^\"]+)\".+");
		final Matcher matcher = sourcePattern.matcher(classpathString);

		final List<String> libPaths = new ArrayList<String>();
		while (matcher.find()){
			final String lib = matcher.group(2);
			libPaths .add(lib);
		}
		return libPaths;

	}

	private static List<String> getSrcPaths(final String classpathString) {
		final Pattern sourcePattern = Pattern.compile(".+classpathentry kind=\"src\"(.+)?(\\s)?path=\"(.+)\".+");
		final Matcher matcher = sourcePattern.matcher(classpathString);

		final List<String> sourcePaths = new ArrayList<String>();
		while (matcher.find()){
			final String srcPath = matcher.group(3);
			sourcePaths .add(srcPath);
		}
		return sourcePaths;
	}
	

}

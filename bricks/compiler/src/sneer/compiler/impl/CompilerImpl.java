package sneer.compiler.impl;

import static wheel.i18n.Language.translate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import sneer.compiler.CompilationException;
import sneer.compiler.Compiler;
import sneer.compiler.Result;
import sneer.lego.Brick;
import wheel.io.Log;

public class CompilerImpl implements Compiler {

	@Brick
	private Logger log;
	
	@Override
	public Result compile(File source, File destination) throws CompilationException {
		return null;
	}
	
	private void compile(File[] sources, File sourceDirectory, File targetDirectory) 
		throws IOException, CompilationFailure {
		for (File source : sources) {
			System.out.println("Compiling " + source.getName());
			String[] parameters = {
					"-classpath", sneerJarLocation + File.pathSeparator + targetClassesDirectory.getAbsolutePath(),
					"-sourcepath", sourceDirectory.getAbsolutePath(),
					"-d", targetClassesDirectory.getAbsolutePath(),
					"-encoding", "UTF-8",
					source.getAbsolutePath() };
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (com.sun.tools.javac.Main.compile(parameters, new PrintWriter(out))!=0) {
				Log.log(out.toString());
				_user.acknowledgeNotification(translate("Compile Error. See the Sneer log file for details."));
				throw new CompilationFailure();
			}
		}
	}
}

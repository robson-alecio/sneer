package antlips.codegeneration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import sneer.foundation.commons.io.Streams;
import sneer.foundation.commons.lang.Functor;


public class Template {

	@SuppressWarnings("unchecked")
	public static <ARG> String evaluate(String template, ARG value) {
		return compile(template, (Class<ARG>) (value != null ? value.getClass() : Object.class)).evaluate(value);
	}

	public static <ARG> Functor<ARG, String> compile(String template, Class<ARG> argClass) {
		final String javaCode = emitJavaCodeFor(template, argClass);
		return instantiate(Template.<ARG>compileJavaCode(javaCode));
	}

	private static <ARG> String emitJavaCodeFor(String template, Class<ARG> argClass) {
		final StringWriter writer = new StringWriter();

		final String argClassName = argClass.getCanonicalName();
		writer.write("import sneer.commons.lang.*;"
				+ "\nimport antlips.codegeneration.*;"
				+ "\nimport java.io.*;"
					+ "\npublic class Template implements Functor<" + argClassName + ", String> {"
						+ "\npublic String evaluate(" + argClassName + " value) {"
							+ "\nfinal StringWriter writer = new StringWriter();" + "\n"
							+ "\nfinal PrintWriter printer = new PrintWriter(writer);" + "\n");
		
		emitEvaluationCode(writer, template);

		writer.write(
							"return writer.toString();" +
						"\n}" +
					"\n}");
		
		return writer.toString();
	}

	private static void emitEvaluationCode(final StringWriter writer, String template) {
		int lastIndex = 0;
		int index = template.indexOf("<%");
		while (index > -1) {
			emitJavaString(writer, template.substring(lastIndex, index));
			lastIndex = template.indexOf("%>", index + 2);
			if (lastIndex < 0)
				throw new IllegalArgumentException("expected %>");
			
			if (template.charAt(index + 2) == '=') {
				// <%= %>
				emitPrint(writer, template.substring(index + 3, lastIndex));
			} else {
				// <% %>
				writer.write(template.substring(index + 2, lastIndex));
			}
			
			lastIndex += 2;
			index = template.indexOf("<%", lastIndex);
		}
		emitJavaString(writer, template.substring(lastIndex));
	}

	private static void emitJavaString(final StringWriter writer, final String text) {
		if (text.isEmpty())
			return;
		emitPrint(writer, javaStringFor(text));
	}

	private static String javaStringFor(String text) {
		final StringBuilder buffer = new StringBuilder(text.length() + 2);
		buffer.append('"');
		for (char ch : text.toCharArray()) {
			switch (ch) {
			case '\\':
				buffer.append("\\\\");
				break;
			case '\r':
				buffer.append("\\r");
				break;
			case '\n':
				buffer.append("\\n");
				break;
			case '"':
				buffer.append("\\\"");
				break;
			default:
				if (ch < 32 || ch > 126) {
					buffer.append("\\u");
					final String hexString = Integer.toHexString(ch);
					for (int i=4-hexString.length(); i>0; --i)
						buffer.append('0');
					buffer.append(hexString);
				} else {
					buffer.append(ch);
				}
			}
		}
		buffer.append('"');
		return buffer.toString();
	}

	private static void emitPrint(StringWriter writer, String value) {
		writer.write("printer.print(");
		writer.write(value);
		writer.write(");\n");
	}

	private static <ARG> Functor<ARG, String> instantiate(Class<Functor<ARG, String>> templateClass) {
		try {
			return templateClass.newInstance();
		} catch (InstantiationException e) {
			throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(e);
		} catch (IllegalAccessException e) {
			throw new sneer.foundation.commons.lang.exceptions.NotImplementedYet(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <ARG> Class<Functor<ARG, String>> compileJavaCode(String javaCode) {
		final JavaCompiler compiler = compiler();
		final RAMJavaFileManager fileManager = new RAMJavaFileManager(compiler.getStandardFileManager(null, null, null));
		try {
			final JavaFileObject javaFileObject = new StringJavaFileObject("Template", javaCode);

			final CompilationTask task = compiler.getTask(null, fileManager, null, Arrays.asList("-classpath", System
					.getProperty("java.class.path")), null, Arrays.asList(javaFileObject));

			if (!task.call())
				throw new IllegalArgumentException(javaCode);

			final ClassLoader cl = new RAMFileObjectClassLoader(fileManager._output);
			return (Class<Functor<ARG, String>>) cl.loadClass("Template");

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		} finally {
			Streams.crash(fileManager);
		}
	}

	private static JavaCompiler compiler() {
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) throw new RuntimeException("System Java Compiler not found. You must run this using a JDK, not merely a JRE.");
		return compiler;
	}

	private static final class RAMFileObjectClassLoader extends ClassLoader {
		private final Map<String, RAMJavaFileObject> _output;

		private RAMFileObjectClassLoader(Map<String, RAMJavaFileObject> output) {
			_output = output;
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			final JavaFileObject jfo = _output.get(name);
			if (jfo != null) {
				byte[] bytes = ((RAMJavaFileObject) jfo)._bytes.toByteArray();
				return defineClass(name, bytes, 0, bytes.length);
			}
			return super.findClass(name);
		}
	}

	private static final class RAMJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
		public final Map<String, RAMJavaFileObject> _output = new HashMap<String, RAMJavaFileObject>();

		private RAMJavaFileManager(StandardJavaFileManager delegate) {
			super(delegate);
		}

		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String name, JavaFileObject.Kind kind,
				FileObject sibling) {
			RAMJavaFileObject file = new RAMJavaFileObject(name, kind);
			_output.put(name, file);
			return file;
		}
	}

	public static class StringJavaFileObject extends SimpleJavaFileObject {
		private final String _code;

		StringJavaFileObject(String name, String code) {
			super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
			_code = code;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return _code;
		}
	}

	static class RAMJavaFileObject extends SimpleJavaFileObject {

		private ByteArrayOutputStream _bytes;

		RAMJavaFileObject(String name, Kind kind_) {
			super(URI.create(name), kind_);
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			throw new UnsupportedOperationException();
		}

		@Override
		public InputStream openInputStream() {
			return new ByteArrayInputStream(_bytes.toByteArray());
		}

		@Override
		public OutputStream openOutputStream() {
			return _bytes = new ByteArrayOutputStream();
		}

	}
}

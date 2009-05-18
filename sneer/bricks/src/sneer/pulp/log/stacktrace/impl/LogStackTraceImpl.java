package sneer.pulp.log.stacktrace.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import sneer.pulp.log.stacktrace.LogStackTrace;

public class LogStackTraceImpl implements LogStackTrace{

	@Override
	public byte[] toByteArray(Throwable throwable) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter _log = new PrintWriter(out, true);
		throwable.printStackTrace(_log);
		return out.toByteArray();
	}

}

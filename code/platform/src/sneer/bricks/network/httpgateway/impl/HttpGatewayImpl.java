package sneer.bricks.network.httpgateway.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.net.URL;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;
import sneer.bricks.network.httpgateway.HttpGateway;
import sneer.foundation.lang.Consumer;

public class HttpGatewayImpl implements HttpGateway {

	@Override
	public void get(final String httpUrl, final Consumer<byte[]> response, final Consumer<IOException> exception) {
		my(Threads.class).startDaemon(httpUrl, new Runnable(){ @Override public void run() {
				try {
					response.consume(my(IO.class).streams().readBytesAndClose(new URL(httpUrl).openStream()));
				} catch (IOException e) {
					exception.consume(e);
				}
		}});
	}

	@Override
	public void get(String httpUrl, Consumer<byte[]> response) {
		get(httpUrl, response, new Consumer<IOException>(){ @Override public void consume(IOException exception) {
			my(ExceptionLogger.class).log(exception);
		}});
	}
}
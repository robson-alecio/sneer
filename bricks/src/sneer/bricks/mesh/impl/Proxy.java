package sneer.bricks.mesh.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wheel.lang.Casts;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

abstract class Proxy extends AbstractParty {

	protected final Map<String, Register<Object>> _registersByRemotePath = new HashMap<String, Register<Object>>();

	@Override
	public <S> Signal<S> signal(String signalPath) {
		Register<S> register = produceRegisterFor(signalPath);
		return register.output();   //Fix: Signal type mismatch between peers is possible. 
	}

	private <T> Register<T> produceRegisterFor(String remoteSignalPath) {
		Register<Object> register = _registersByRemotePath.get(remoteSignalPath);
		if (register == null) {
			register = new RegisterImpl<Object>(null);
			_registersByRemotePath.put(remoteSignalPath, register);
			subscribeTo(new ArrayList<String>(), remoteSignalPath);
		}
		return Casts.uncheckedGenericCast(register);
	}

	abstract void subscribeTo(ArrayList<String> nicknamePath, String remoteSignalPath);

}

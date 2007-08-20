package prevayler.bubble;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

public class PrevalentBubbleBlower {

	public PrevalentBubbleBlower(String directory) {
		_directory = directory;
	}
	
	private final String _directory;
	private boolean _wasAlreadyUsed = false;

	synchronized public <STATE_MACHINE> STATE_MACHINE blowBubble(STATE_MACHINE stateMachine) throws IOException, ClassNotFoundException {
		if (_wasAlreadyUsed) throw new IllegalStateException();
		try {
			return wrap(stateMachine);
		} finally {
			_wasAlreadyUsed = true;
		}
	}

	@SuppressWarnings("unchecked")
	private <STATE_MACHINE> STATE_MACHINE wrap(STATE_MACHINE stateMachine) throws IOException, ClassNotFoundException {
		PrevaylerFactory prevaylerFactory = new PrevaylerFactory();
		prevaylerFactory.configurePrevalenceDirectory(_directory);
		prevaylerFactory.configurePrevalentSystem(stateMachine);
		Prevayler prevayler = prevaylerFactory.create();
		
		return (STATE_MACHINE)Bubble.wrapStateMachine(prevayler);
	}
	
}

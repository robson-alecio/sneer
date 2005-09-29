package wheelexperiments.reactive;

public interface Receiver<VC> {
	void receive(VC valueChange);
}

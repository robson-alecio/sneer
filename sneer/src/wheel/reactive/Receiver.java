package wheel.reactive;

public interface Receiver<VC> {
	void receive(VC valueChange);
}

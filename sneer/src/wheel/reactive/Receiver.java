package wheel.reactive;

public interface Receiver<VC> {
	void receive(VC valueChange); //Refactor: consider not sending the valueChange (even for lists). The receiver asks for currentValue() or something like "lastDelta()" for more complex objects.
}

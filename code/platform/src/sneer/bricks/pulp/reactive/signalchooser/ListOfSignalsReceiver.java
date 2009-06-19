package sneer.bricks.pulp.reactive.signalchooser;


public interface ListOfSignalsReceiver<T> {

   SignalChooser<T> signalChooser();
   void elementSignalChanged(T element);}
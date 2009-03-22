package sneer.pulp.reactive.signalchooser;

import wheel.reactive.lists.ListChange;

public interface ListOfSignalsReceiver<T> extends ListChange.Visitor<T> {

   SignalChooser<T> signalChooser();
   void elementSignalChanged(int index, T element);}
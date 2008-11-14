package sneer.pulp.reactive.signalchooser;

import wheel.reactive.lists.ListValueChange;

public interface ListOfSignalsReceiver<T> extends ListValueChange.Visitor<T> {

   SignalChooser<T> signalChooser();
   void elementSignalChanged(int index, T element);}
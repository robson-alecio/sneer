package sneer.skin.widgets.reactive;

import javax.swing.ListModel;

import sneer.pulp.reactive.signalchooser.ElementListener;

public interface ListSignalModel<T> extends ListModel, ElementListener<T>{

}
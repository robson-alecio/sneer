package sneer.skin.widgets.reactive.impl;

import static sneer.commons.environments.Environments.my;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;

class RListImpl<ELEMENT> extends JList implements ListWidget<ELEMENT> {

	private static final long serialVersionUID = 1L;

	protected final ListSignal<ELEMENT> _source;
	protected LabelProvider<ELEMENT> _labelProvider;
	
	private final Register<ELEMENT> _selectedElement = my(Signals.class).newRegister(null);

	RListImpl(ListSignal<ELEMENT> source, LabelProvider<ELEMENT> labelProvider, ListCellRenderer cellRenderer ) {
		_source = source;
		_labelProvider = labelProvider;
		initModel();
		
		setCellRenderer(cellRenderer == null
			? new RListSimpleCellRenderer<ELEMENT>(this)
			: cellRenderer
		);

		addSelectionSupport();
	}
	
	private void initModel() {
		SignalChooser<ELEMENT> chooser = new SignalChooser<ELEMENT>(){	@Override public Signal<?>[] signalsToReceiveFrom(ELEMENT element) {
			return new Signal<?>[]{_labelProvider.imageFor(element), 
								   	   _labelProvider.labelFor(element)};}};
		ListModel model = new ListSignalModel<ELEMENT>(_source, chooser);
		setModel(model);
	}

	@Override
	public JList getMainWidget() {
		return this;
	}

	@Override
	public JComponent getComponent() {
		return this;
	}
	
	private void addSelectionSupport() {
		ListSelectionModel selectionModel = getSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		selectionModel.addListSelectionListener(new ListSelectionListener(){ @Override public void valueChanged(ListSelectionEvent e) {
			ELEMENT element = (ELEMENT) getSelectedValue();
			_selectedElement.setter().consume(element);
		}});

		getModel().addListDataListener(new ListDataListener(){
			@Override public void contentsChanged(ListDataEvent e) { changeSelectionGuiToSelectedContact();}
			@Override public void intervalAdded(ListDataEvent e) { changeSelectionGuiToSelectedContact();}
			@Override public void intervalRemoved(ListDataEvent e) { changeSelectionGuiToSelectedContact();}

			private void changeSelectionGuiToSelectedContact() {
				final ELEMENT element = _selectedElement.output().currentValue();
				my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
					setSelectedValue(element, true);
				}});
			}
		});
	}	
	
	@Override
	public Signal<ELEMENT> selectedElement(){
		return _selectedElement.output();
	}
	
	@Override
	public void clearSelection(){
		my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
			_selectedElement.setter().consume(null);
			getSelectionModel().clearSelection();
		}});
	}	
}
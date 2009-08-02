package sneer.bricks.skin.widgets.reactive.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;
import sneer.bricks.skin.widgets.reactive.LabelProvider;
import sneer.bricks.skin.widgets.reactive.ListWidget;

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
		
		addMouseListener(new MouseAdapter(){@Override public void mouseReleased(MouseEvent e) {
			int index = locationToIndex(e.getPoint());
			if (index == -1) return;
			final ELEMENT element = (ELEMENT) getModel().getElementAt(index);
			if(getSelectedValue()  != element)
				setSelectedValue(element, true);
			
			changeSelectedElement(element);
		}});
		
		addKeyListener(new KeyAdapter(){ @Override public void keyReleased(KeyEvent e) {
			ELEMENT element = (ELEMENT) getSelectedValue();
			changeSelectedElement(element);
		}});

		getModel().addListDataListener(new ListDataListener(){
			@Override public void contentsChanged(ListDataEvent e) { changeSelectionGuiToSelectedContact();}
			@Override public void intervalAdded(ListDataEvent e) { changeSelectionGuiToSelectedContact();}
			@Override public void intervalRemoved(ListDataEvent e) { changeSelectionGuiToSelectedContact();}

			private void changeSelectionGuiToSelectedContact() {
				final ELEMENT element = _selectedElement.output().currentValue();
				my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
					if(getSelectedValue()==element)
						return;
					
					setSelectedValue(element, true);
				}});
			}
		});
	}	
	
	private void changeSelectedElement(ELEMENT element) {
		_selectedElement.setter().consume(element);
	}
	
	@Override
	public Signal<ELEMENT> selectedElement(){
		return _selectedElement.output();
	}
	
	@Override
	public void clearSelection(){
		my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
			getSelectionModel().clearSelection();
			changeSelectedElement(null);
		}});
	}	
}
package wheel.io.ui.widgets;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

//FixUrgent: this class is unfinished!!!! its in progress...

public abstract class ReactiveField<U> extends JPanel{
	
	protected Signal<U> _source;
	protected Consumer<U> _setter;
	protected boolean _editable;
	
	public ReactiveField(Signal<U> source, Consumer<U> setter){
		_source = source;
		_setter = setter;
		_editable = (setter != null); //if setter == null, different behaviour
		initComponents();
		insertView();
		updateView(_editable);
	}
	
	public void initComponents(){
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
	}

	public abstract void insertView();
	public abstract void updateView(boolean editable);

}

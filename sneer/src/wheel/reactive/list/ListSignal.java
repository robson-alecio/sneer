package wheel.reactive.list;

import java.util.Calendar;
import java.util.List;

import wheel.reactive.Receiver;

public interface ListSignal<VO> {
	
	public interface ListValueChangeVisitor { //Refactor: remove elements, keep only indexes. For removal do a "pre"Removal notification.
		void elementAdded(int index);
		void elementRemoved(int index);
		void elementReplaced(int index);
		void listReplaced();
	}

	public interface ListValueChange {		
		void accept(ListValueChangeVisitor visitor);
	}

	public void addListReceiver(Receiver<ListValueChange> receiver);

	public List<VO> currentValue();
	

}

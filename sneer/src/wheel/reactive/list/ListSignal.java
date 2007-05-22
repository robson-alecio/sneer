package wheel.reactive.list;

import java.util.Calendar;
import java.util.List;

import sneer.kernel.business.Contact;

import wheel.reactive.Receiver;

public interface ListSignal<VO> {
	
	public interface ListValueChangeVisitor { //Refactor: Consider: For removal and replacement do a "pre"Removal/Replacement notification.
		void elementAdded(int index);
		void elementRemoved(int index);
		void elementReplaced(int index);
		void listReplaced();
	}

	public interface ListValueChange {		
		void accept(ListValueChangeVisitor visitor);
	}

	public void addListReceiver(Receiver<ListValueChange> receiver);

	public VO currentGet(int index);
	public int currentSize();
	

}

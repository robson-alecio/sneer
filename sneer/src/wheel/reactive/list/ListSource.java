package wheel.reactive.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wheel.reactive.AbstractNotifier;
import wheel.reactive.Receiver;
import wheel.reactive.list.ListSignal.ListValueChange;

public class ListSource<VO> extends AbstractNotifier<ListValueChange> 
	implements ListSignal<VO>, Serializable {  //Refactor: Make into interface with a ListSignal output() instead of implementing ListSignal.

	private final List<VO> _list = new ArrayList<VO>();
	
	@Override
	protected void initReceiver(Receiver<ListValueChange> receiver) {
		receiver.receive(new ListReplaced());
		
	}

	public void addListReceiver(Receiver<ListValueChange> receiver) {
		addReceiver(receiver);		
	}

	public void add(VO element){
		synchronized (_list){
			_list.add(element);
			notifyReceivers(new ListElementAdded(_list.size() - 1));
		}
	}
	
	private static final long serialVersionUID = 0L;

	public List<VO> currentValue() {
		return Collections.unmodifiableList(_list); 
	}

	public boolean remove(VO element) {
		synchronized (_list){
			int index = _list.indexOf(element);
			if (!_list.remove(element))
				return false;
			
			notifyReceivers(new ListElementRemoved(index));
			return true;
		}
	}
}

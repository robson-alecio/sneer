package wheel.reactive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wheel.reactive.ListSignal.ListValueChange;

public class ListSource<VO> extends AbstractNotifier<ListValueChange<VO>> 
	implements ListSignal<VO>, Serializable {  //Refactor: Make into interface with a ListSignal output() instead of implementing ListSignal.

	private final List<VO> _list = new ArrayList<VO>();
	
	@Override
	protected void initReceiver(
			Receiver<ListValueChange<VO>> receiver) {
		receiver.receive(new ListReplaced<VO>(_list));
		
	}

	public void addListReceiver(
			Receiver<ListValueChange<VO>> receiver) {
		addReceiver(receiver);		
	}

	public void add(VO element){
		synchronized (_list){
			_list.add(element);
			notifyReceivers(new ListElementAdded<VO>(_list.size() - 1));
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
			
			notifyReceivers(new ListElementRemoved<VO>(index, element));
			return true;
		}
	}
}

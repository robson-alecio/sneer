package wheel.reactive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListSource<VO> extends AbstractNotifier<wheel.reactive.ListSignal.ListValueChange<VO>> 
	implements ListSignal<VO>, Serializable {

	private final List<VO> _list = new ArrayList<VO>();
	
	@Override
	protected void initReceiver(
			Receiver<wheel.reactive.ListSignal.ListValueChange<VO>> receiver) {
		receiver.receive(new ListReplaced<VO>(_list));
		
	}

	public void addListReceiver(
			Receiver<wheel.reactive.ListSignal.ListValueChange<VO>> receiver) {
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

package wheel.reactive;

import java.util.Calendar;
import java.util.List;

public interface ListSignal<VO> {
	
	public interface ListValueChangeVisitor<VO> {
		void elementAdded(int index);
		void elementRemoved(int index, VO element);
		void elementReplaced(int index, VO oldElement);
		void listReplaced(List<VO> newList);
	}

	public interface ListValueChange<VO> {		
		void accept(ListValueChangeVisitor<VO> visitor);
	}

	public void addListReceiver(Receiver<ListValueChange<VO>> receiver);

	public List<VO> currentValue();
	

}

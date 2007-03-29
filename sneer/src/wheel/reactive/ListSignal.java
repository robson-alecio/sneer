package wheel.reactive;

import java.util.List;

public interface ListSignal<VO> {
	
	public interface Receiver<VO> {
		void elementAdded(int index);
		void elementRemoved(int index, VO element);
		void elementReplaced(int index, VO oldElement);
		void listReplaced(List<VO> newList);
	}

	public void addReceiver(Receiver<VO> receiver);

}

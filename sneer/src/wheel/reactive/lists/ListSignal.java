package wheel.reactive.lists;

import java.util.Calendar;
import java.util.List;

import sneer.kernel.business.contacts.Contact;

import wheel.reactive.Receiver;

public interface ListSignal<VO> extends Iterable<VO> {
	
	public void addListReceiver(Receiver<ListValueChange> receiver);

	public VO currentGet(int index);
	public int currentSize();

}

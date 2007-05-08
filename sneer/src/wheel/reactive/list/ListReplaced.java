package wheel.reactive.list;

import java.util.List;

import wheel.reactive.list.ListSignal.ListValueChange;
import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public final class ListReplaced<VO> implements ListValueChange<VO> {

	private final List<VO> _contents;

	public ListReplaced(List<VO> contents) {
		_contents = contents;
	}

	public void accept(ListValueChangeVisitor<VO> visitor) {
		visitor.listReplaced(_contents);
	}
	
	@Override
	public String toString() {
		return "List replaced with: " + _contents.toString();
	}

}

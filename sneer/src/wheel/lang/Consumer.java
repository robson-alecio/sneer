package wheel.lang;

import wheel.lang.exceptions.IllegalArgument;

public interface Consumer<VO> {
	
	void consume(VO valueObject) throws IllegalArgument;

}

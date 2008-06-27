package wheel.lang;


public interface Consumer<VO> {

	void consume(VO valueObject); //throws IllegalParameter; 	// For now, throwing of the exception has been removed because of a bug in the Eclipse compiler . Refactor: See wheel.lang.exceptions.IllegalParameter and throw the Exception again. 

}

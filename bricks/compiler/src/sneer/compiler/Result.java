package sneer.compiler;

public interface Result {

	boolean success();

	void setError(String errorString);
}

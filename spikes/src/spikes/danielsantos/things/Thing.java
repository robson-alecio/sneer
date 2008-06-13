package spikes.danielsantos.things;

public class Thing {

	private final String _name;
	private final String _description;

	public Thing(String name, String description) {
		_name = name;
		_description = description;
	}

	public String name() {
		return _name;
	}

	public String description() {
		return _description;
	}

	
	
}

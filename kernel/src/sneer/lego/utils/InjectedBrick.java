package sneer.lego.utils;

public class InjectedBrick {
	
	private String _brickName;
	
	public InjectedBrick(String brickName) {
		_brickName = brickName;
	}

	public String brickName() {
		return _brickName;
	}

}

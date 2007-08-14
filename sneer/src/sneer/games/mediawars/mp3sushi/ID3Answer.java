package sneer.games.mediawars.mp3sushi;

public class ID3Answer {
	
	private ID3Summary _id3Summary;
	private long _time;
	
	public ID3Answer(ID3Summary id3Summary, long time) {
		super();
		this._id3Summary = id3Summary;
		this._time = time;
	}

	public ID3Summary getId3Summary() {
		return _id3Summary;
	}

	public long getTime() {
		return _time;
	}
	

}

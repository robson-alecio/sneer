package sneer.old.remote;

class Envelope  {

	Object _contents;
	final int _stamp;

	
	Envelope(Object contents, int stamp) {
		_contents = contents;
		_stamp = stamp;
	}

}

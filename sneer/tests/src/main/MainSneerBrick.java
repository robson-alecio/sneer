package main;

import sneer.container.NewBrick;


@NewBrick
public interface MainSneerBrick {

	void start(String ownName, String dynDnsUserName, String dynDnsPassword);

}

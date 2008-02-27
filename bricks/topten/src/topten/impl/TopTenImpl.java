package topten.impl;

import java.util.List;

import sneer.lego.Brick;
import sneer.lego.Startable;
import topten.TopTen;
import console.Console;

public class TopTenImpl implements TopTen, Startable {

	@Brick
	private Console console;
	
	@Override
	public List<?> topTen() {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void start() {
		console.out("top 10");
	}
}

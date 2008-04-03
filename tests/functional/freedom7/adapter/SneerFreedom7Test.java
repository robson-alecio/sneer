package functional.freedom7.adapter;

import functional.freedom7.BrickPublished;
import functional.freedom7.Freedom7Test;
import functional.freedom7.Peer;


public class SneerFreedom7Test extends Freedom7Test {

	@Override
	protected Peer createPeer() {
		return new Peer(){

			@Override
			public void deploy(BrickPublished brick) {
				// Implement Auto-generated method stub
				throw new wheel.lang.exceptions.NotImplementedYet();
			}

			@Override
			public Object lookup(String string) {
				// Implement Auto-generated method stub
				throw new wheel.lang.exceptions.NotImplementedYet();
			}

			@Override
			public BrickPublished publishBrick(String string) {
				// Implement Auto-generated method stub
				throw new wheel.lang.exceptions.NotImplementedYet();
			}};
	}

}

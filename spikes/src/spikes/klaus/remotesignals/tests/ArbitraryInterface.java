package spikes.klaus.remotesignals.tests;

import java.io.Serializable;

import wheel.reactive.Signal;

interface ArbitraryInterface extends Serializable {

	Signal<String> signal1();
	Signal<Integer> signal2();
	
}

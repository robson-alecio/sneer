package sneerapps.giventake.impl;

import java.util.Map.Entry;

import sneer.bricks.mesh.Me;
import sneer.bricks.things.Thing;
import sneer.bricks.things.ThingHome;
import sneer.lego.Inject;
import sneerapps.giventake.GiveNTake;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.maps.MapRegister;
import wheel.reactive.maps.MapSignal;
import wheel.reactive.maps.impl.MapRegisterImpl;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.SetSignal.SetValueChange;
import wheel.reactive.sets.impl.SetRegisterImpl;

class GiveNTakeImpl implements GiveNTake, Omnivore<SetValueChange<String>> {

	@Inject
	static private Me _me;
	@Inject
	static private ThingHome _thingHome;
	
	private final Omnivore<SetValueChange<GiveNTake>> _myPeerReceiver = createMyPeerReceiver();
	private final MapRegister<String, SetRegister<Thing>> _resultSetByActiveSearch = new MapRegisterImpl<String, SetRegister<Thing>>();

	private final MapRegister<String, SetSignal<Thing>> _localResults = new MapRegisterImpl<String, SetSignal<Thing>>();
	private final Omnivore<SetValueChange<Entry<String, SetSignal<Thing>>>> _myRemoteResultsReceiver = createMyRemoteResultsReceiver();

	{
		SetSignal<GiveNTake> peers = _me.allImmediateContactBrickCounterparts(GiveNTake.class);
		peers.addSetReceiver(_myPeerReceiver);
		
		for (GiveNTake peer : peers)
			peer.activeSearches().addSetReceiver(this);
	}
	
	@Override
	public void advertise(Thing thing) {
		//Refactor This is wierd. This method has to do nothing because things are already "advertised" (they are directly searchable).
	}

	private Omnivore<SetValueChange<Entry<String, SetSignal<Thing>>>> createMyRemoteResultsReceiver() {
		return new Omnivore<SetValueChange<Entry<String,SetSignal<Thing>>>>() {@Override public void consume(SetValueChange<Entry<String, SetSignal<Thing>>> newResults) {
			if (newResults.elementsAdded().size() != 1) throw new NotImplementedYet();
			if (	newResults.elementsRemoved().size() != 0) throw new NotImplementedYet();
			
			Entry<String, SetSignal<Thing>> newResult = newResults.elementsAdded().iterator().next();
			String tags = newResult.getKey();
			
			System.out.println("Search results received for tags: " + tags + " " + this);
			SetRegister<Thing> resultsSoFar = _resultSetByActiveSearch.output().currentGet(tags);
			for (Thing thing : newResult.getValue())
				resultsSoFar.add(thing); //Fix Deal with duplicates.
		}};
	}

	private Omnivore<SetValueChange<GiveNTake>> createMyPeerReceiver() {
		return new Omnivore<SetValueChange<GiveNTake>>(){@Override public void consume(SetValueChange<GiveNTake> changeInPeers) {
			if (	changeInPeers.elementsAdded().size() != 1) throw new NotImplementedYet();
			if (	changeInPeers.elementsRemoved().size() != 0) throw new NotImplementedYet();
			
			GiveNTake newPeer = changeInPeers.elementsAdded().iterator().next();
			System.out.println("New peer detected");
			newPeer.activeSearches().addSetReceiver(GiveNTakeImpl.this);
			newPeer.localResults().addSetReceiver(_myRemoteResultsReceiver);
		}};
	}

	@Override
	public SetSignal<Thing> search(String tags) {
		SetRegister<Thing> resultsSoFar = new SetRegisterImpl<Thing>();
		_resultSetByActiveSearch.put(tags, resultsSoFar); //Fix: handle duplicates

		System.out.println("Search results wanted for tags: " + tags + " " + this);

		return resultsSoFar.output();
	}

	@Override
	public SetSignal<String> activeSearches() {
		return _resultSetByActiveSearch.output().keys();
	}

	@Override
	public MapSignal<String, SetSignal<Thing>> localResults() {
		return _localResults.output();
	}
	
	@Override
	public void consume(SetValueChange<String> changeInSearches) {
		if (	changeInSearches.elementsAdded().size() != 1) throw new NotImplementedYet();
		if (	changeInSearches.elementsRemoved().size() != 0) throw new NotImplementedYet();

		String tags = changeInSearches.elementsAdded().iterator().next();
		
		System.out.println("someone searched for " + tags);
		
		_localResults.put(tags, _thingHome.search(tags)); //Fix Handle duplicates.
	}


}

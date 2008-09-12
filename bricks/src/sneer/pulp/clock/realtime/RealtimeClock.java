package sneer.pulp.clock.realtime;

public interface RealtimeClock {

	long currentTimeMillis();

	void setOwner(Runnable owner);
	
}

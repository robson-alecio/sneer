package spikes.legobricks.security;

public interface Sentinel {

	void check(String resourceName) throws Sorry;

}

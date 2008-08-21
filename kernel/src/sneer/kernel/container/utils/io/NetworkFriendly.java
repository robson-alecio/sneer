package sneer.kernel.container.utils.io;

import java.io.IOException;

public interface NetworkFriendly {

	/**
	 * Called on the original object before a copy is made
	 */
	void beforeSerialize() throws IOException;
	
	/**
	 * Called on the original object after a copy is made
	 */
	void afterSerialize() throws IOException;
	
}

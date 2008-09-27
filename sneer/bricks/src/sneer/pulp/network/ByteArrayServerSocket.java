//Copyright (C) 2008 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Alexandre Nodari.

package sneer.pulp.network;

import java.io.IOException;

import sneer.kernel.container.Crashable;


public interface ByteArrayServerSocket extends Crashable {

	ByteArraySocket accept() throws IOException;
	
}
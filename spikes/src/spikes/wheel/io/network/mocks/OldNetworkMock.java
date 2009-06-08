//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Alexandre Nodari.

package spikes.wheel.io.network.mocks;

import java.io.IOException;

import spikes.wheel.io.network.ObjectServerSocket;
import spikes.wheel.io.network.ObjectSocket;
import spikes.wheel.io.network.OldNetwork;



public class OldNetworkMock extends BaseNetworkMock 
                         implements OldNetwork {

	
	public synchronized ObjectSocket openSocket(String serverIpAddress, int serverPort) throws IOException {
	    crashIfNotLocal(serverIpAddress);
        return startClient(serverPort);
		
	}

	public synchronized ObjectServerSocket openObjectServerSocket(int serverPort) throws IOException {
	    return startServer(serverPort);
	}
}

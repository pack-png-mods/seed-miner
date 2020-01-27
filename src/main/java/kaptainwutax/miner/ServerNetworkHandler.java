package kaptainwutax.miner;

import kaptainwutax.miner.net.connection.ServerListener;

public class ServerNetworkHandler {

	protected ServerListener serverListener = null;

	public void start(int port) {
		this.serverListener = new ServerListener(port);
	}

	public void stop() {
		if(this.serverListener != null) {
			this.serverListener.disconnect();
		}
	}

}

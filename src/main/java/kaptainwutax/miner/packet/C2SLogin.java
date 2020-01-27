package kaptainwutax.miner.packet;

import javafx.application.Platform;
import kaptainwutax.miner.ServerMiner;
import kaptainwutax.miner.net.context.ServerContext;
import kaptainwutax.miner.net.packet.Packet;
import kaptainwutax.miner.net.packet.util.C2SMessage;

public class C2SLogin extends Packet implements C2SMessage {

	private String username;

	public C2SLogin(String username) {
		this.username = username;
	}

	@Override
	public Packet onPacketReceived(ServerContext context) {
		ServerMiner.UserEntry userEntry = ServerMiner.entries.get(context.listener.getListenerId());

		if(userEntry != null) {
			Platform.runLater(() -> userEntry.username.setText(this.username));
		}

		return this.username.isEmpty() ? new S2CDisconnect() : null;
	}

}

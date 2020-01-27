package kaptainwutax.miner.packet;

import kaptainwutax.miner.net.context.ClientContext;
import kaptainwutax.miner.net.packet.Packet;
import kaptainwutax.miner.net.packet.util.S2CMessage;

public class S2CDisconnect extends Packet implements S2CMessage {

	public S2CDisconnect() {

	}

	@Override
	public Packet onPacketReceived(ClientContext context) {
		context.listener.disconnect();
		return null;
	}

}

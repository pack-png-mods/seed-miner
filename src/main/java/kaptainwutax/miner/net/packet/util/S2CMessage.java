package kaptainwutax.miner.net.packet.util;

import kaptainwutax.miner.net.context.ClientContext;
import kaptainwutax.miner.net.packet.Packet;

public interface S2CMessage {

    Packet onPacketReceived(ClientContext context);

}

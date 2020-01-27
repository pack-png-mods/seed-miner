package kaptainwutax.miner.net.packet.util;

import kaptainwutax.miner.net.context.ServerContext;
import kaptainwutax.miner.net.packet.Packet;

public interface C2SMessage {

    Packet onPacketReceived(ServerContext context);

}

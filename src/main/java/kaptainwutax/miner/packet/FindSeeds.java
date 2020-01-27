package kaptainwutax.miner.packet;

import kaptainwutax.miner.ClientWork;
import kaptainwutax.miner.ServerMiner;
import kaptainwutax.miner.net.context.ClientContext;
import kaptainwutax.miner.net.context.ServerContext;
import kaptainwutax.miner.net.packet.Packet;
import kaptainwutax.miner.net.packet.util.C2SMessage;
import kaptainwutax.miner.net.packet.util.S2CMessage;

import java.util.List;

public class FindSeeds extends Packet implements S2CMessage, C2SMessage {

	private long lowerBound;
	private long upperBound;
	private List<Long> seeds;

	public FindSeeds(int seedGroupSize, int n) {
		this.lowerBound = (long)seedGroupSize * n;
		this.upperBound = lowerBound + (long)seedGroupSize;
	}

	public FindSeeds(List<Long> seeds) {
		this.seeds = seeds;
	}

	@Override
	public Packet onPacketReceived(ClientContext context) {
		ClientWork.getSeeds(this.lowerBound, this.upperBound, context.listener);
		return null;
	}

	@Override
	public Packet onPacketReceived(ServerContext context) {
		ServerMiner.seedSplitter.onProgressReceived(context.listener, this.seeds);
		return null;
	}

}

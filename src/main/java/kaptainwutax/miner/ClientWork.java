package kaptainwutax.miner;

import kaptainwutax.miner.net.connection.Listener;
import kaptainwutax.miner.packet.FindSeeds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientWork {

	public static final ExecutorService SERVICE = Executors.newFixedThreadPool(1);

	public static void getSeeds(long lowerBound, long upperBound, Listener listener) {
		SERVICE.submit(() -> {
			List<Long> seeds = new ArrayList<>();

			for(long seed = lowerBound; seed < upperBound; seed++) {
				if(checkSeed(seed))seeds.add(seed);
			}

			listener.sendPacket(new FindSeeds(seeds));
		});
	}

	public static boolean checkSeed(long seed) {
		return seed % 10_000_000 == 0;
	}

}

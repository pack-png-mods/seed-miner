package kaptainwutax.miner;

import javafx.application.Platform;
import kaptainwutax.miner.net.connection.Listener;
import kaptainwutax.miner.packet.FindSeeds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SeedSplitter {

	public static final int SEED_GROUP_SIZE = 1 << 30;
	protected Map<Listener, List<Integer>> tasks = new HashMap<>();

	public void addUser(Listener listener, String task) {
		task = task.trim();

		if(task.isEmpty()) {
			throw new IllegalArgumentException();
		}

		String[] ss = task.split(Pattern.quote(","));
		List<Integer> groups = new ArrayList<>();

		for(String s: ss) {
			int upper;
			int lower;

			if(s.contains("-")) {
				String[] sa = s.split(Pattern.quote("-"));
				lower = Integer.parseInt(sa[0].trim());
				upper = Integer.parseInt(sa[1].trim());
			} else {
				upper = Integer.parseInt(s.trim());
				lower = upper;
			}

			for(int i = lower; i <= upper; i++) {
				groups.add(i);
			}
		}

		this.tasks.put(listener, groups);
		this.sendNext(listener);
	}

	public void onProgressReceived(Listener listener, List<Long> seeds) {
		System.out.println("[" + ServerMiner.entries.get(listener.getListenerId()).username.getText() + "]: " + seeds);
		this.sendNext(listener);
	}

	private void sendNext(Listener listener) {
		List<Integer> task = this.tasks.get(listener);
		if(task == null || task.isEmpty())return;
		int n = task.remove(0);

		Platform.runLater(() -> {
			ServerMiner.entries.get(listener.getListenerId()).progress.setText("Seed Group: " + n);
		});

		listener.sendPacket(new FindSeeds(SEED_GROUP_SIZE, n));
	}

}

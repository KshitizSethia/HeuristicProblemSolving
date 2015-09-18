/*package runner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exactChange.ExactChangeCalculator;

public class BruteForceRunner {

	private static final int NUM_DENOMINATIONS = 6;
	private static final int NUM_CORES = 4;
	private static final ExecutorService threadPool = Executors
			.newFixedThreadPool(NUM_CORES - 1);

	private static final ConcurrentHashMap<List<Integer>, int[]> costsForDenominations = new ConcurrentHashMap<List<Integer>, int[]>(
			Integer.MAX_VALUE);

	public static void main(String[] args) {
		List<Integer> denominations = new ArrayList<Integer>();
		denominations.add(1);

		long startTime = System.currentTimeMillis();
		calculateCosts(denominations);
		long duration = System.currentTimeMillis()-startTime;
		
		System.out.println(duration);
	}

	private static void calculateCosts(List<Integer> denominations) {
		if (denominations.size() == NUM_DENOMINATIONS) {
			threadPool.submit(new Runnable() {
				public void run() {
					// todo: collect results
					costsForDenominations.put(denominations, ExactChangeCalculator.getCostsForDenominations(denominations));
				}
			});
		} else {
			for (int start = denominations.get(denominations.size() - 1) + 1; start < ExactChangeCalculator.PENCES_IN_POUND
					- NUM_DENOMINATIONS + denominations.size(); start++) {
				denominations.add(start);
				calculateCosts(denominations);
				denominations.remove(denominations.size() - 1);
			}
		}
	}

}
*/
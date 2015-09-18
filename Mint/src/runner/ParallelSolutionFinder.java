package runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import common.Helper;
import common.SolutionCollector;
import common.SolutionOptimizer;

public final class ParallelSolutionFinder {

	private static final int NUM_THREADS = 1;

	private Object solutionUpdateLock;
	private float bestCost;
	private int[] bestDenominations;

	ParallelSolutionFinder() {
		bestCost = Helper.INF;
		solutionUpdateLock = new Object();
	}

	public void offerNewSolution(int[] denominations, float cost) {
		synchronized (solutionUpdateLock) {
			if (cost < bestCost) {
				bestCost = cost;
				bestDenominations = denominations;
			}
		}
	}
	
	public int[] getBestDenominations(){
		synchronized (solutionUpdateLock) {
			return bestDenominations.clone();
		}
	}

	public static int[] getStartingDenominations() {
		Random random = new Random();
		// generate multiples of 5
		return new int[] { 1, (random.nextInt(48) + 1) * 5,
				(random.nextInt(48) + 1) * 5, (random.nextInt(48) + 1) * 5,
				(random.nextInt(48) + 1) * 5, (random.nextInt(48) + 1) * 5,
				(random.nextInt(48) + 1) * 5 };
	}

	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		ParallelSolutionFinder solver = new ParallelSolutionFinder();

		List<SolutionOptimizer> runnerPool = new ArrayList<SolutionOptimizer>();

		for (int threadNum = 0; threadNum < NUM_THREADS; threadNum++) {
			SolutionOptimizer runner = new SolutionOptimizer(solver, N);
			runnerPool.add(runner);
			runner.start();
		}

		Timer timer = new Timer();
		TimerTask collectBestSolutionFromAllThreads = new SolutionCollector(
				solver, runnerPool);
		timer.schedule(collectBestSolutionFromAllThreads, 115000);
	}
}
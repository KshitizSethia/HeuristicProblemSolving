package runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import api.SolutionOptimizer;
import common.Helper;
import common.SolutionCollector;
import common.solutionOptimizer.StartsWithFivesStepsRandomly;
import common.solutionOptimizer.StartsWithFivesStepsRandomlyThenWithOneStepSize;

public final class ParallelSolutionFinder {

	private static final int NUM_THREADS = 4;

	private Object solutionUpdateLock;
	private double bestCost;
	private int[] bestDenominations;

	ParallelSolutionFinder() {
		bestCost = Helper.INF;
		solutionUpdateLock = new Object();
	}

	public void offerNewSolution(int[] denominations, double cost) {

		synchronized (solutionUpdateLock) {
			if (cost < bestCost) {
				if (Helper.DEBUG_MODE) {
					System.out.println("accepted "
							+ Arrays.toString(denominations) + ", " + cost);
				}
				bestCost = cost;
				bestDenominations = denominations;
			}
		}
	}

	public int[] getBestDenominations() {
		synchronized (solutionUpdateLock) {
			return bestDenominations.clone();
		}
	}

	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		
		if (Helper.DEBUG_MODE) {
			System.out.println("Log for N=" + args[0]);
		}
		
		ParallelSolutionFinder solver = new ParallelSolutionFinder();

		List<SolutionOptimizer> runnerPool = new ArrayList<SolutionOptimizer>();

		for (int threadNum = 0; threadNum < NUM_THREADS; threadNum++) {
			SolutionOptimizer runner = new StartsWithFivesStepsRandomly(solver,
					N);
			runnerPool.add(runner);
			runner.start();
		}

		Timer timer = new Timer();
		TimerTask collectBestSolutionFromAllThreads = new SolutionCollector(
				solver, runnerPool);
		// todo: changed below value for debugging, restore to 115000
		timer.schedule(collectBestSolutionFromAllThreads, 115000);
	}
}

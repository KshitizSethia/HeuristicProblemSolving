package runner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lkh.LKH_Thread;
import lkh.LKH_Thread_random;
import common.Helper;
import common.Input;
import common.SolutionCollector;
import common.SolutionOptimizer;

public final class ParallelSolutionFinder {

	private static final int NUM_THREADS = 7;

	private Object solutionUpdateLock;
	private double bestCost;
	private int[] bestTour;
	private final int[] cityIds;

	ParallelSolutionFinder(int[] cityIds) {
		bestCost = Double.MAX_VALUE;
		solutionUpdateLock = new Object();
		this.cityIds = cityIds;
	}

	public void offerNewSolution(int[] tour, double cost) {

		synchronized (solutionUpdateLock) {
			if (cost < bestCost) {

				if (Helper.DEBUG_MODE) {
					System.out.println("accepted " + Arrays.toString(tour)
							+ ", " + cost);
				}
				System.out.println("cost: " +cost);
				bestCost = cost;
				bestTour = tour.clone();
			}
		}
	}

	public int[] getBestTour() {
		synchronized (solutionUpdateLock) {
			int[] result = new int[cityIds.length];
			for (int index = 0; index < result.length; index++) {
				result[index] = cityIds[bestTour[index]];
			}
			return result;
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		Input.init(args);

		ParallelSolutionFinder solutionFinder = new ParallelSolutionFinder(
				Input.getInstance().cityIds);

		List<SolutionOptimizer> runnerPool = new ArrayList<SolutionOptimizer>();

		for (int threadNum = 0; threadNum < NUM_THREADS; threadNum++) {
			SolutionOptimizer runner = new LKH_Thread_random(solutionFinder,
					Input.getInstance().locations);
			runnerPool.add(runner);
			runner.start();
		}

		SolutionOptimizer exactRunner = new LKH_Thread(solutionFinder,
				Input.getInstance().locations);
		runnerPool.add(exactRunner);
		exactRunner.start();

		Timer timer = new Timer();
		TimerTask collectBestSolutionFromAllThreads = new SolutionCollector(
				solutionFinder, runnerPool);
		timer.schedule(collectBestSolutionFromAllThreads, 115000); // 115000);
	}
}

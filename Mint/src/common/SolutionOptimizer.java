package common;

import java.util.Random;

import runner.ParallelSolutionFinder;
import exactChange.ExactChangeCalculator;

public class SolutionOptimizer extends Thread {

	private ParallelSolutionFinder model;

	private int N;

	private Object denominationUpdateLock;
	private int[] bestDenominations;
	private float bestCost;

	public SolutionOptimizer(ParallelSolutionFinder model, int N) {
		this.model = model;
		this.N = N;
		denominationUpdateLock = new Object();
	}

	@Override
	public void run() {
		while (true) {
			int[] startingDenominations = model.getStartingDenominations();
			float startingCost = ExactChangeCalculator.getCost(
					startingDenominations, N);
			setBestSolution(startingDenominations, startingCost);

			// TODO: make threshold configurable
			optimizeTillConvergence(0.004f);
			model.offerNewSolution(bestDenominations, bestCost);
		}
	}

	/**
	 * force overwrite of best solution
	 * 
	 * @param startingDenominations
	 * @param startingCost
	 */
	private void setBestSolution(int[] startingDenominations, float startingCost) {
		synchronized (denominationUpdateLock) {
			bestDenominations = startingDenominations;
			bestCost = startingCost;
		}
	}

	public ImmutableSolution getBestSolution() {
		synchronized (denominationUpdateLock) {
			return new ImmutableSolution(bestDenominations, bestCost);
		}
	}

	/**
	 * accept new solution if it is better
	 * 
	 * @param newCost
	 * @param newDenominations
	 */
	private void offerNewSolution(float newCost, int[] newDenominations) {
		synchronized (denominationUpdateLock) {
			if (newCost < bestCost) {
				bestCost = newCost;
				bestDenominations = newDenominations;
			}
		}
	}

	/**
	 * optimize till gain is above threshold
	 * 
	 * @param minGainToRun
	 */
	private void optimizeTillConvergence(final float minGainToRun) {
		while (true) {
			// create new solution
			int[] newDenominations = takeStep();
			float newCost = ExactChangeCalculator.getCost(newDenominations, N);
			float delta = bestCost - newCost;
			
			// accept if better solution
			offerNewSolution(newCost, newDenominations);

			// break out if not gaining much
			if (delta < minGainToRun) {
				break;
			}
		}
	}

	private static Random random = new Random();

	/**
	 * take random step on random direction
	 * 
	 * @return
	 */
	private int[] takeStep() {
		int[] newDenom = bestDenominations.clone();
		int indexToBeChanged = random.nextInt(Helper.NUM_DENOMINATIONS);
		newDenom[indexToBeChanged] = Math
				.abs((newDenom[indexToBeChanged] + random.nextInt()))
				% Helper.PENCES_IN_POUND;
		return newDenom;
	}
}
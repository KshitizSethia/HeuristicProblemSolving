package common.solutionOptimizer;

import changeCalculator.ExactChangeCalculator;
import changeCalculator.ExchangeCalculator;
import changeCalculator.ExchangeCorrect;
import runner.ParallelSolutionFinder;
import api.SolutionOptimizer;
import common.Helper;
import common.ImmutableSolution;

public class StartsWithFivesStepsRandomly extends SolutionOptimizer {

	public StartsWithFivesStepsRandomly(ParallelSolutionFinder model, double N) {
		super(model, N);
	}

	/*
	 * private static volatile long count = 0; private static Object countLock =
	 * new Object();
	 * 
	 * private static void incrementCount() { synchronized (countLock) {
	 * count++; } }
	 * 
	 * public static long getCount() { synchronized (countLock) { return count;
	 * } }
	 */
	public int[] getStartingDenominations() {
		// incrementCount();

		// generate multiples of 5
		return new int[] { 1, (random.nextInt(47) + 1) * 5,
				(random.nextInt(47) + 1) * 5, (random.nextInt(47) + 1) * 5,
				(random.nextInt(47) + 1) * 5, (random.nextInt(47) + 1) * 5,
				(random.nextInt(47) + 1) * 5 };
	}

	@Override
	public void run() {
		while (true) {
			int[] startingDenominations = getStartingDenominations();
			double startingCost = 0;
			if (Helper.USE_EXACT) {
				startingCost = ExactChangeCalculator.getCost(
						startingDenominations, N);
			} else {
				startingCost = ExchangeCalculator.getCost(
						startingDenominations, N);
			}
			setBestSolution(startingDenominations, startingCost);

			// TODO: make threshold configurable
			optimizeTillConvergence(0.004);
			model.offerNewSolution(bestDenominations, bestCost);
		}
	}

	/**
	 * force overwrite of best solution
	 * 
	 * @param startingDenominations
	 * @param startingCost
	 */
	protected void setBestSolution(int[] startingDenominations,
			double startingCost) {
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
	protected void offerNewSolution(double newCost, int[] newDenominations) {
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
	protected void optimizeTillConvergence(final double minGainToRun) {
		while (true) {
			// create new solution
			int[] newDenominations = takeStep();

			double newCost = 0;
			if (Helper.USE_EXACT) {
				newCost = ExactChangeCalculator.getCost(newDenominations, N);
			} else {
				newCost = ExchangeCorrect.getCost(newDenominations, N);
			}

			double gain = bestCost - newCost;
			if (gain < 0) {
				// came to worse solution
				continue;
			}
			// accept if better solution
			offerNewSolution(newCost, newDenominations);

			// break out if not gaining much
			if (gain < minGainToRun) {
				break;
			}
		}
	}

	/**
	 * take random step on random direction
	 * 
	 * @return
	 */
	protected int[] takeStep() {
		int[] newDenom = bestDenominations.clone();

		int indexToBeChanged = random.nextInt(Helper.NUM_DENOMINATIONS - 1) + 1;

		do {
			newDenom[indexToBeChanged] = Math
					.abs((newDenom[indexToBeChanged] + random.nextInt()))
					% Helper.PENCES_IN_POUND;
		} while (newDenom[indexToBeChanged] == 0);

		return newDenom;
	}
}

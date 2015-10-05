package common.solutionOptimizer;

import runner.ParallelSolutionFinder;
import changeCalculator.ExactChangeCalculator;
import changeCalculator.ExchangeCalculator;

import common.Helper;

public class StartsWithFivesStepsRandomlyThenWithOneStepSize extends
		StartsWithFivesStepsRandomly {

	public StartsWithFivesStepsRandomlyThenWithOneStepSize(
			ParallelSolutionFinder model, double N) {
		super(model, N);
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

	@Override
	protected void optimizeTillConvergence(double minGainToRun) {
		while (true) {
			// create new solution
			int[] newDenominations = takeRandomStep();

			double newCost = 0;
			if (Helper.USE_EXACT) {
				newCost = ExactChangeCalculator.getCost(newDenominations, N);
			} else {
				newCost = ExchangeCalculator.getCost(newDenominations, N);
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
				optimizeTillConvergenceWithSingleSteps(minGainToRun);
				break;
			}
		}
	}

	private void optimizeTillConvergenceWithSingleSteps(double minGainToRun) {
		// todo: remove for competition
		double bestCostBeforeSingleStepping = bestCost;
		while (true) {
			boolean gotGainOnSomeIndex = false;
			for (int index = 1; index < bestDenominations.length; index++) {
				int[] newDenom = bestDenominations.clone();
				// try going up one step
				newDenom[index] = newDenom[index] + 1;
				if (newDenom[index] < Helper.PENCES_IN_POUND) {

					double upCost = 0;
					if (Helper.USE_EXACT) {
						upCost = ExactChangeCalculator.getCost(newDenom, N);
					} else {
						upCost = ExchangeCalculator.getCost(newDenom, N);
					}

					double upGain = bestCost - upCost;

					if (upGain > 0) {
						gotGainOnSomeIndex = true;
						offerNewSolution(upCost, newDenom.clone());
						if (upGain < minGainToRun) {
							return;
						}
						break;
					}
				}

				// try going down one step
				newDenom[index] = newDenom[index] - 2;
				if (newDenom[index] > 0) {

					double downCost = 0;
					if (Helper.USE_EXACT) {
						downCost = ExactChangeCalculator.getCost(newDenom, N);
					} else {
						downCost = ExchangeCalculator.getCost(newDenom, N);
					}

					double downGain = bestCost - downCost;

					if (downGain > 0) {
						gotGainOnSomeIndex = true;
						offerNewSolution(downCost, newDenom.clone());
						if (downGain < minGainToRun) {
							return;
						}
						break;
					}
				}

				// reset
				newDenom[index] = newDenom[index] + 1;
			}
			if (!gotGainOnSomeIndex) {
				break;
			}
		}

		if (Helper.DEBUG_MODE) {
			double gainDueToSingleStepping = bestCostBeforeSingleStepping
					- bestCost;
			System.out.println("Gain due to single stepping: "
					+ gainDueToSingleStepping);
		}
	}

	private int[] takeRandomStep() {
		return super.takeStep();
	}
}

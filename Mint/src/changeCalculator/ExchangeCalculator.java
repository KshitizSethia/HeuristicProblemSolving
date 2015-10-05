package changeCalculator;

//min[value] = Math.min(min[value], ExactChange(240 + coin[i] - value)) change for 236 = ExactChange(240 + 1 - 236)
import java.util.Arrays;

import common.Helper;
import common.InvalidDenominationsException;

public class ExchangeCalculator {

	public static double getCost(int[] denominations, double N) {
		ExactChangeCalculator calciExact = new ExactChangeCalculator(
				denominations, N);
		calciExact.calculateMinCoinsDriver();
		int[] exactChange = calciExact.getMinCoins();
		ExchangeCalculator calci = new ExchangeCalculator(denominations, N,
				exactChange);
		calci.calculateMinCoinsDriver();
		return calci.calculateCost();
	}

	private int[] denominations;
	private int[] minCoinsNeeded;
	private int[] exactChange;
	private double N;

	private ExchangeCalculator(int[] denominations, double N,
			int[] exactChangeMinCoins) {
		this.denominations = denominations;
		this.N = N;
		this.exactChange = exactChangeMinCoins;
		this.minCoinsNeeded = new int[Helper.PENCES_IN_POUND];
		for (int index = minCoinsNeeded.length - 1; index >= 0; index--) {
			minCoinsNeeded[index] = Helper.INF;
		}
	}

	private double calculateCost() {
		double cost = 0.0;
		minCoinsNeeded[0] = 0;

		for (int coinCount : minCoinsNeeded) {
			cost += coinCount;
		}

		if (N > 1.0) {
			double penalty = 0.0;
			for (int index = 5; index < Helper.PENCES_IN_POUND; index += 5) {
				penalty += minCoinsNeeded[index];
			}
			cost += (penalty * (N - 1.0));
		}
		return cost;
	}

	private void calculateMinCoinsDriver() {
		for (int denomination : denominations) {
			minCoinsNeeded[denomination] = 1;
		}
		minCoinsNeeded[0] = 0;

		calculateMinCoins();

		if (Helper.DEBUG_MODE) {
			for (int index = minCoinsNeeded.length - 1; index > 0; index--) {
				if (minCoinsNeeded[index] == Helper.INF) {
					System.out.println("denominations: "
							+ Arrays.toString(denominations));
					System.out.println("index: " + index);
					throw new InvalidDenominationsException();
				}
			}
		}
	}

	private void calculateMinCoins() {

		for (int index = 1; index < minCoinsNeeded.length; index++) {
			for (int denomination : denominations) {
				int queryIndex = index - denomination;
				if (queryIndex > 0) {
					minCoinsNeeded[index] = Math.min(minCoinsNeeded[index],
							1 + minCoinsNeeded[queryIndex]);
				} else {// queryIndex<=1;
					minCoinsNeeded[index] = Math.min(minCoinsNeeded[index],
							1 + exactChange[0 - queryIndex]);
				}
				try {
					minCoinsNeeded[index] = Math.min(minCoinsNeeded[index],
							1 + (exactChange[Helper.PENCES_IN_POUND
									+ denomination - index]));
				} catch (Exception ignored) {

				}
			}
			minCoinsNeeded[index] = Math.min(minCoinsNeeded[index],
					Helper.PENCES_IN_POUND - index);
		}

		/*
		 * if (index < 1) { return exactChange[0 - index]; } for (int
		 * denomination : denominations) { int downCost = 1 +
		 * calculateMinCoins(index - denomination); minCoinsNeeded[index] =
		 * Math.min(minCoinsNeeded[index], downCost); } minCoinsNeeded[index] =
		 * Math.min(minCoinsNeeded[index], exactChange[Helper.PENCES_IN_POUND -
		 * index]); return minCoinsNeeded[index];
		 */

	}
}

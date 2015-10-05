package changeCalculator;

import common.Helper;
import common.InvalidDenominationsException;

public class ExactChangeCalculator {

	public static double getCost(int[] denominations, double N) {
		ExactChangeCalculator calci = new ExactChangeCalculator(denominations,
				N);
		calci.calculateMinCoinsDriver();
		return calci.calculateCost();
	}

	private int[] minCoinsNeeded;
	private int[] denominations;
	private double N;

	private ExactChangeCalculator(int[] denominations, double N) {
		minCoinsNeeded = new int[Helper.PENCES_IN_POUND];
		for (int index = minCoinsNeeded.length - 1; index >= 0; index--) {
			minCoinsNeeded[index] = Helper.INF;
		}
		minCoinsNeeded[0] = 0;

		this.denominations = denominations;
		this.N = N;
	}

	private void calculateMinCoinsDriver() {
		for (int denomination : denominations) {
			minCoinsNeeded[denomination] = 1;
		}
		calculateMinCoins(minCoinsNeeded.length - 1);
	}

	private int calculateMinCoins(int index) {
		if (index < 1) {
			return Helper.INF;
		}

		if (minCoinsNeeded[index] == Helper.INF) {
			// TODO: never try denomination bigger than parent call's
			for (int denomination : denominations) {
				if (denomination <= index) {
					int costWithThisDenomination = calculateMinCoins(index
							- denomination) + 1;
					if (costWithThisDenomination < minCoinsNeeded[index]) {
						// better result found
						minCoinsNeeded[index] = costWithThisDenomination;
					}
				}
			}
			if (Helper.DEBUG_MODE) {
			if (minCoinsNeeded[index] == Helper.INF) {
				throw new InvalidDenominationsException();
			}}
		}
		return minCoinsNeeded[index];
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
}

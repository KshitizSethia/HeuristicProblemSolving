package changeCalculator;

import java.util.Arrays;

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

	ExactChangeCalculator(int[] denominations, double N) {
		minCoinsNeeded = new int[Helper.PENCES_IN_POUND];
		for (int index = minCoinsNeeded.length - 1; index >= 0; index--) {
			minCoinsNeeded[index] = Helper.INF;
		}
		this.denominations = denominations;
		this.N = N;
	}

	int[] getMinCoins() {
		return minCoinsNeeded;
	}

	void calculateMinCoinsDriver() {
		for (int denomination : denominations) {
			minCoinsNeeded[denomination] = 1;
		}
		minCoinsNeeded[0] = 0;

		calculateMinCoins();// minCoinsNeeded.length - 1);

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

	private void calculateMinCoins() {// int index) {

		for (int index = 1; index < minCoinsNeeded.length; index++) {
			for (int denomination : denominations) {
				int queryIndex = index - denomination;
				if (queryIndex > 0) {
					minCoinsNeeded[index] = Math.min(minCoinsNeeded[index],
							minCoinsNeeded[queryIndex] + 1);
				}
			}
		}

		/**
		 * 
		 *
		 * if (minCoinsNeeded[index] == Helper.INF) { // TODO: never try
		 * denomination bigger than parent call's for (int denomination :
		 * denominations) { if (denomination <= index) { int
		 * costWithThisDenomination = calculateMinCoins(index - denomination) +
		 * 1; if (costWithThisDenomination < minCoinsNeeded[index]) { // better
		 * result found minCoinsNeeded[index] = costWithThisDenomination; } } }
		 *
		 * } return minCoinsNeeded[index];
		 */
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

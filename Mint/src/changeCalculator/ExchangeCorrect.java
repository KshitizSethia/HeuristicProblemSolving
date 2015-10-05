package changeCalculator;

import java.util.Arrays;

import common.Helper;
import common.InvalidDenominationsException;

public class ExchangeCorrect {
	public static double getCost(int[] denominations, double N) {
		ExactChangeCalculator calciExact = new ExactChangeCalculator(
				denominations, N);
		calciExact.calculateMinCoinsDriver();
		int[] exactChange = calciExact.getMinCoins();
		ExchangeCorrect calci = new ExchangeCorrect(denominations, N,
				exactChange);
		calci.calculateMinCoinsDriver();
		return calci.calculateCost();
	}

	private int[] denominations;
	private int[] minCoinsNeeded;
	private int[] exactChange;
	private double N;

	private ExchangeCorrect(int[] denominations, double N,
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
		// int exactChange = exactChangeNumberCache[value];
		int temp2 = 0;
		for (int valueTemp = 1; valueTemp < 240; valueTemp++) {

			int minx = Integer.MAX_VALUE;
			int x, y = 0;
			for (int i = 0; i < denominations.length; i++) {
				int diff = denominations[i] - valueTemp;
				int diffFrom240 = 240 - valueTemp;
				minx = Math.min(exactChange[diffFrom240], minx);
				int g240 = denominations[i] + 240 - valueTemp;
				try {
					temp2 = 1 + exactChange[g240];
					minx = Math.min(minx, temp2);
				} catch (Exception e) {

				}
				y = exactChange[valueTemp];
				if (diff > 0) {
					x = exactChange[diff] + 1;
					int temp = Math.min(x, y);
					minx = Math.min(minx, temp);
				} else {
					minx = Math.min(minx, y);
				}
			}
			minCoinsNeeded[valueTemp] = minx;
		}

	}
}

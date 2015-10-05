package runner;

import java.util.Arrays;

import changeCalculator.ExactChangeCalculator;
import changeCalculator.ExchangeCalculator;

import common.Helper;

public class SurfaceVisualizer {

	public static void main(String[] args) {
		int[] denominations = new int[Helper.NUM_DENOMINATIONS];
		denominations[0] = 1;

		double[][] nonMultipleCosts = new double[Helper.PENCES_IN_POUND - 1][Helper.PENCES_IN_POUND - 1];
		double[][] multipleCosts = new double[Helper.PENCES_IN_POUND - 1][Helper.PENCES_IN_POUND - 1];

		for (int first = 1; first < Helper.PENCES_IN_POUND; first++) {
			denominations[1] = first;
			for (int second = 1; second < Helper.PENCES_IN_POUND; second++) {
				denominations[2] = second;
				double n1Cost = 0;
				double n2Cost = 0;
				if (Helper.USE_EXACT) {
					n1Cost = ExactChangeCalculator.getCost(denominations, 1.0);
					n2Cost = ExactChangeCalculator.getCost(denominations, 2.0);
				} else {
					n1Cost = ExchangeCalculator.getCost(denominations, 1.0);
					n2Cost = ExchangeCalculator.getCost(denominations, 2.0);
				}
				double multipleCost = n2Cost - n1Cost;
				double nonMultipleCost = n1Cost - multipleCost;

				nonMultipleCosts[first - 1][second - 1] = nonMultipleCost;
				multipleCosts[first - 1][second - 1] = multipleCost;
			}
		}

		System.out.println("non multiples:");
		printArray(nonMultipleCosts);

		System.out.println("multiples:");
		printArray(multipleCosts);
	}

	private static void printArray(double[][] twoDArray) {
		System.out.print("[");
		for (double[] row : twoDArray) {
			System.out.println(Arrays.toString(row) + ",");
		}
		System.out.println("]");
	}
}

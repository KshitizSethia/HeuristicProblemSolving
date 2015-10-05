package runner;

import java.util.Arrays;

import changeCalculator.ExactChangeCalculator;
import changeCalculator.ExchangeCalculator;
import changeCalculator.ExchangeCorrect;
import common.Helper;

public class GetCost {

	public static void main(String[] args) {
		if (args.length != 9) {
			System.out.println("usage: <ct/ch> <N> <denomination list>");
			System.exit(-1);
		}

		double N = Double.parseDouble(args[1]);
		int[] denominations = readDenominations(args);

		System.out.println("N: " + N + ", denominations: "
				+ Arrays.toString(denominations));
		if (args[0].equals("ct")) {
			System.out.println("using exact");
			System.out.println(ExactChangeCalculator.getCost(denominations, N));
		} else {
			System.out.println("using exchange");
			System.out.println(ExchangeCorrect.getCost(denominations, N));
		}
	}

	private static int[] readDenominations(String[] args) {
		int[] denom = new int[Helper.NUM_DENOMINATIONS];
		for (int index = 2; index < args.length; index++) {
			denom[index - 2] = Integer.parseInt(args[index]);
		}
		return denom;
	}
}

package runner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import changeCalculator.ExactChangeCalculator;
import changeCalculator.ExchangeCalculator;
import common.Helper;

public class BruteForceRunner {

	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);

		int[] denominations = new int[Helper.NUM_DENOMINATIONS];
		denominations[0] = 1;

		int[] bestDenominations = new int[Helper.NUM_DENOMINATIONS];
		double bestCost = 100000;
		double roundNum = 0;

		System.out.println("Started at " + getCurrentTime());

		for (int first = 1; first < Helper.PENCES_IN_POUND; first++) {
			denominations[1] = first;
			for (int second = 1; second < Helper.PENCES_IN_POUND; second++) {
				denominations[2] = second;
				for (int third = 1; third < Helper.PENCES_IN_POUND; third++) {
					denominations[3] = third;
					for (int fourth = 1; fourth < Helper.PENCES_IN_POUND; fourth++) {
						denominations[4] = fourth;
						for (int fifth = 1; fifth < Helper.PENCES_IN_POUND; fifth++) {
							denominations[5] = fifth;
							for (int sixth = 1; sixth < Helper.PENCES_IN_POUND; sixth++) {
								denominations[6] = sixth;

								double thisCost = 0;
								if (Helper.USE_EXACT) {
									thisCost = ExactChangeCalculator.getCost(
											denominations, N);
								} else {
									thisCost = ExchangeCalculator.getCost(
											denominations, N);
								}

								if (thisCost < bestCost) {
									System.out.println("best cost yet: "
											+ thisCost);
									bestCost = thisCost;
									bestDenominations = denominations.clone();
								}
								roundNum++;
								if (roundNum % 100000 == 0) {
									System.out.println("progress: " + roundNum);
								}
							}
						}
					}
				}
			}
		}

		System.out.println("Ended at " + getCurrentTime());
		System.out.println("Global optima: " + bestCost);
		System.out.println("denominations: "
				+ Arrays.toString(bestDenominations));
	}

	static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
}

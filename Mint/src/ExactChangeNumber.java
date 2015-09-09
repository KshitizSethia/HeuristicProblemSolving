public class ExactChangeNumber {

	//private static final int NUM_DENOMINATIONS = 3;
	private static final int PENCES_IN_POUND = 100;
	private static final int INF = 10000;// max can be if all denominations are
											// 1

	/**
	 * 
	 * @return ignore index 0
	 */
	public final static int[] getCosts(int[] denominations) {
		int[] results = new int[PENCES_IN_POUND];
		for (int index = results.length - 1; index >= 0; index--) {
			results[index] = INF;
		}
		for(int denomination: denominations){
			results[denomination]=1;
		}

		for (int index = results.length - 1; index > 0; index--) {
			if (results[index] == INF) {
				calculateResult(index, results, denominations);
			}
		}

		return results;
	}

	private final static int calculateResult(int index, int[] minCosts,
			int[] denominations) {
		if (index < 1) {
			return INF;
		}

		if (minCosts[index] == INF) {
			for (int denomination : denominations) {
				int costWithThisDenomination = calculateResult(index
						- denomination, minCosts, denominations);
				if (costWithThisDenomination < minCosts[index]) {
					// better result found
					minCosts[index] = costWithThisDenomination+1;
				}else if(costWithThisDenomination==INF) {
					throw new InvalidDenominationsException();
				}
			}
		}
		return minCosts[index];
	}
}

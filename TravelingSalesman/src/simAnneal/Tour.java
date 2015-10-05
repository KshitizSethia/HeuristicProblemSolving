package simAnneal;

import java.util.Random;

public class Tour {
	static final int tourLength = 1000;
	static City[] tour;
	private static Tour instance = null;
	static double[][] euclidDistance = new double[tourLength + 1][tourLength + 1];

	public static Tour getInstance() {
		if (instance == null) {
			instance = new Tour();
		}

		return instance;
	}

	private Tour() {
		tour = new City[tourLength];
	}

	static void modifyTour(City[] q) {
		for (int i = 0; i < 1000; i++) {
			tour[i] = q[i];
		}
	}

	/*
	 * static void ThreeOptTour(int i1, int i2, int i3) { City a, b, c; int
	 * aIndex, bIndex, cIndex; double[] distances = new double[6]; for(aIndex
	 * =0; aIndex<tour.length; aIndex++) { bIndex = aIndex + 1; if(bIndex >=
	 * tour.length) bIndex =0;
	 * 
	 * cIndex = bIndex + 1; if(cIndex >= tour.length) cIndex =0;
	 * 
	 * a = tour[aIndex]; b = tour[bIndex]; c = tour[cIndex];
	 * 
	 * //ABC --> ABC BAC CAB // ACB BCA CBA distances[0] = distance(a, b, c);
	 * distances[1] = distance(a, c, b); distances[2] = distance(b, a, c);
	 * distances[3] = distance(b, c, a); distances[4] = distance(c, a, b);
	 * distances[5] = distance(c, b, a);
	 * 
	 * //find min int minIndex = 0; for(int i=0; i < distances.length; i++) {
	 * if(distances[i] < distances[minIndex]) minIndex = i; } switch(minIndex) {
	 * case 0://ABC break; case 1://ACB-swap B and C swap(path, bIndex, cIndex);
	 * break; case 2://BAC-swap A and B swap(path, aIndex, bIndex); break; case
	 * 3://BCA-swap A and C and then C and B swap(path, aIndex, cIndex);
	 * swap(path, cIndex, bIndex); break; case 4://CAB-swap A and C and then B
	 * and A swap(path, aIndex, cIndex); swap(path, bIndex, aIndex); break; case
	 * 5://CBA-swap A and C swap(path, aIndex, cIndex); break; } } }
	 */
	static void TwoOptTour(int i1, int i2) {
		if (i1 > i2) {
			int t = i1;
			i1 = i2;
			i2 = t;
		}
		int i = i1;
		int j = i2 / 2;
		// int x = i2 - i1;
		int k = 0;
		for (i = i1; i < j; i++) {
			City temp = tour[i];
			tour[i] = tour[i2 - k];
			tour[i2 - k] = temp;
			k++;
		}
	}

	static void shuffleTour() {
		Random rnd = new Random();
		for (int i = tour.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			City a = tour[index];
			tour[index] = tour[i];
			tour[i] = a;
		}
	}

	static City[] getCitiesInTour() {
		return tour;
	}

	static double getTourCost(boolean cycleAround) {
		double cost = 0;

		for (int i = 0; i < tourLength - 1; i++) {
			cost += getEuclideanDistance(tour[i], tour[i + 1]);
		}

		if (cycleAround) {
			cost += getEuclideanDistance(tour[tour.length - 1], tour[0]);
		}

		return cost;
	}

	static double getTourCost2(boolean cycleAround) {
		double cost = 0;

		for (int i = 0; i < tourLength - 1; i++) {
			cost += getEuclideanDistance2(tour[i], tour[i + 1]);
		}

		if (cycleAround) {
			cost += getEuclideanDistance2(tour[tour.length - 1], tour[0]);
		}

		return cost;
	}

	static double getSwappedNewCost(double cost, int i, int j) {
		double a, b, c, d, e, f, g, h;
		a = b = c = d = e = f = g = h = 0;

		try {
			a = getEuclideanDistance(tour[i - 1], tour[j]);
		} catch (Exception ee) {
		}
		;
		try {
			b = getEuclideanDistance(tour[j], tour[i + 1]);
		} catch (Exception ee) {
		}
		;
		try {
			c = getEuclideanDistance(tour[i], tour[j - 1]);
		} catch (Exception ee) {
		}
		;
		try {
			d = getEuclideanDistance(tour[i], tour[j + 1]);
		} catch (Exception ee) {
		}
		;

		try {
			e = getEuclideanDistance(tour[i - 1], tour[i]);
		} catch (Exception ee) {
		}
		;
		try {
			f = getEuclideanDistance(tour[i], tour[i + 1]);
		} catch (Exception ee) {
		}
		;
		try {
			g = getEuclideanDistance(tour[j], tour[j - 1]);
		} catch (Exception ee) {
		}
		;
		try {
			h = getEuclideanDistance(tour[j], tour[j + 1]);
		} catch (Exception ee) {
		}
		;

		cost = cost + a + b + c + d - (e + f + g + h);

		return cost;
	}

	static void swapCitiesInTour(int i, int j) {
		City c = tour[i];
		tour[i] = tour[j];
		tour[j] = c;
	}

	public static double getEuclideanDistance(City c1, City c2) {
		double res = 0;

		res = Math.pow((c1.ux - c2.ux), 2) + Math.pow((c1.uy - c2.uy), 2)
				+ Math.pow((c1.uz - c2.uz), 2);

		return Math.sqrt(res);
	}

	public static double getEuclideanDistance2(City c1, City c2) {
		double res = 0;

		res = Math.pow((c1.x - c2.x), 2) + Math.pow((c1.y - c2.y), 2)
				+ Math.pow((c1.z - c2.z), 2);

		return Math.sqrt(res);
	}

	@Override
	public String toString() {
		String geneString = "|";
		for (int i = 0; i < tour.length; i++) {
			geneString += tour[i].toString() + "|";
		}
		return geneString;
	}
}

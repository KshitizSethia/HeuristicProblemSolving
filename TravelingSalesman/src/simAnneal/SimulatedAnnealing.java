package simAnneal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import common.Helper;

public class SimulatedAnnealing {
	static HashMap<Integer, City> cityMap = new HashMap<Integer, City>();
	static HashMap<Integer, City> cityMap2 = new HashMap<Integer, City>();
	// static HashMap<cityPair, Double> cityPair = new HashMap<>();
	static int[] path;

	public static double anneal2(double initialTemp, int coolingSteps,
			int stepsPerTemp, double alpha, boolean cycleAround) {

		Random r = new Random();
		double temp = initialTemp;

		double cost = Tour.getTourCost(cycleAround);
		if (Helper.DEBUG_MODE) {
			System.out.println("Initial Cost = " + cost);
		}
		initialTemp = initialTemp / alpha;
		long x = System.currentTimeMillis();
		// boolean f = true;
		while (System.currentTimeMillis() - x < 120000) {
			temp = temp * alpha;

			double initial_cost = cost;

			for (int j = 0; j < stepsPerTemp; j++) {
				int i1 = r.nextInt(1000);
				int i2 = r.nextInt(1000);

				// int i3 = r.nextInt(1000);
				// int i4 = r.nextInt(1000);

				Tour.swapCitiesInTour(i1, i2);
				// Tour.TwoOptTour(i1, i2);
				double newCost = Tour.getTourCost(cycleAround);

				if (newCost > cost) {
					// Then keep the change by some probability
					double prob = Math.exp((cost - newCost) / temp);
					if (prob < Math.random()) {
						// Discard the change
						Tour.swapCitiesInTour(i1, i2);
						// Tour.TwoOptTour(i1, i2);
					} else {
						// Keep the change
						cost = newCost;
						// System.out.println(cost);
					}
				}

				else {
					cost = newCost;
					if (Helper.DEBUG_MODE) {
						System.out.println(cost + ", "
								+ Tour.getTourCost2(false));
					}
				}
			}

			// restore temperature if progress has been made
			if (cost < initial_cost) {
				temp = temp / alpha;
				// System.out.println(cost);
			}
		}
		// simplifiedLKH();
		return cost;
	}

	public static Tour getInputTour(String path) throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		City[] temp = new City[1000];
		String line = "";
		// int i = 0;
		double maxa, maxb, maxc, mina, minb, minc;
		maxa = maxb = maxc = Integer.MIN_VALUE;

		mina = minb = minc = Integer.MAX_VALUE;

		for (int i = 0; i < 1000; i++) {
			line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);
			int index = Integer.parseInt(st.nextToken());

			double a = Double.parseDouble(st.nextToken());
			double b = Double.parseDouble(st.nextToken());
			double c = Double.parseDouble(st.nextToken());

			maxa = Math.max(maxa, a);
			mina = Math.min(mina, a);

			maxb = Math.max(maxb, b);
			minb = Math.min(minb, b);

			maxc = Math.max(maxc, c);
			minc = Math.min(minc, c);

			temp[i] = new City(index, a, b, c);
			cityMap.put(index, temp[i]);
			// cityMap2.put(i, temp[i]);
			// System.out.println(temp[i] + ", " + i);
		}

		for (int i = 0; i < 1000; i++) {
			temp[i].ux = (temp[i].x - mina) / (maxa - mina);
			temp[i].uy = (temp[i].y - minb) / (maxb - minb);
			temp[i].uz = (temp[i].z - minc) / (maxc - minc);
		}

		br.close();
		// Initializing tour
		Tour instance = Tour.getInstance();
		Tour.modifyTour(temp);
		return instance;
	}

	public static Tour readOutputTour(String path) throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		City[] temp = new City[1000];
		String line = "";
		int i = 0;
		for (i = 0; i < 1000; i++) {
			// String line = br.readLine();
			// StringTokenizer st = new StringTokenizer(line);
			// st.nextToken();
			temp[i] = cityMap.get(Integer.parseInt(br.readLine()));

			System.out.println(temp[i]);
			System.out.println(i);

			// i++;
		}
		br.close();
		// Initializing tour
		Tour instance = Tour.getInstance();
		Tour.modifyTour(temp);
		return instance;
	}

	public static void NearestNeighbor(double[][] distanceMatrix, int startCity) {

		path = new int[distanceMatrix[0].length];

		path[0] = startCity;
		int currentCity = startCity;

		/**
		 * until there are cities that are not yet been visited
		 */
		int i = 1;
		while (i < path.length) {
			// find next city
			int nextCity = findMin(distanceMatrix[currentCity]);
			// if the city is not -1 (meaning if there is a city to be visited
			if (nextCity != -1) {
				// add the city to the path
				path[i] = nextCity;
				// update currentCity and i
				currentCity = nextCity;
				i++;
			}
		}
	}

	/**
	 * Find the nearest city that has not yet been visited
	 * 
	 * @param row
	 * @return next city to visit
	 */
	private static int findMin(double[] row) {

		int nextCity = -1;
		int i = 0;
		double min = Double.MAX_VALUE;

		while (i < row.length) {
			if (isCityInPath(i) == false && row[i] < min) {
				min = row[i];
				nextCity = i;
			}
			i++;
		}
		return nextCity;
	}

	public static boolean isCityInPath(int city) {
		for (int i = 0; i < path.length; i++) {
			if (path[i] == city) {
				return true;
			}
		}
		return false;
	}

	public static void main(String args[]) throws Exception {
		Tour instance = getInputTour(args[0]);
		// Tour instance2 =
		// readOutputTour("/Users/mihirjadhav2/Downloads/OUT(1).TXT");

		Random r = new Random();
		City[] temp = Tour.getCitiesInTour();
		City[] ans = new City[1000];
		int count = 0;
		HashMap<City, Integer> map = new HashMap<>();
		ans[count] = temp[r.nextInt(1000)];
		map.put(temp[0], 1);
		City current = temp[0];
		double min = Double.MAX_VALUE;

		while (count < 1000) {
			City next = null;
			for (City c : temp) {
				if (!map.containsKey(c)) {
					double x = Tour.getEuclideanDistance(current, c);
					if (x < min) {
						min = x;
						next = c;
					}
				}
			}

			map.put(next, 1);
			count++;
			if (count < 1000) {
				ans[count] = next;
			}

			min = Double.MAX_VALUE;
		}

		Tour.modifyTour(ans);
		if (Helper.DEBUG_MODE) {
			System.out.println(Tour.getTourCost(false));
		}
		// anneal3();
		anneal2(0.08, 0, 800, 0.999, false);
		// System.out.println("Original cost = " + Tour.getTourCost2(false));

	}
}

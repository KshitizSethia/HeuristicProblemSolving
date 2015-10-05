package lkh;

import common.Helper;

class Tour {
	private int[] tour;
	private final Object tourLock;
	private final int problemSize;

	Tour(int[] tour, int problemSize) {
		this.tour = tour.clone();
		tourLock = new Object();
		this.problemSize = problemSize;
	}

	public Tour(Tour tourToCopy) {
		this(tourToCopy.getCopy(), tourToCopy.getProblemSize());
	}

	public int getProblemSize() {
		return problemSize;
	}

	int getNextInTour(int index) {
		synchronized (tourLock) {
			return tour[index];
		}
	}

	void setNextInTour(int index, int value) {
		synchronized (tourLock) {
			tour[index] = value;
			if (Helper.DEBUG_MODE) {
				System.out.println(this);
			}
		}
	}

	public String toString() {
		synchronized (tourLock) {
			StringBuffer sbr = new StringBuffer();

			sbr.append("main tour: ");
			for (int node = 0; true; node = tour[node]) {
				sbr.append(node + "-->");
				if (sbr.toString().contains(Integer.toString(tour[node]))) {
					sbr.append(tour[node]);
					break;
				}
			}
			String mainTourDescription = sbr.toString();

			sbr.append("\npointing to tour:\n");
			for (int index = 0; index < problemSize; index++) {
				if (!mainTourDescription.contains(Integer.toString(index))) {
					sbr.append(index + "-->" + tour[index] + "\n");
				}
			}
			return sbr.toString();
		}
	}

	void reverseTour(int start, int end) {
		synchronized (tourLock) {
			int current = start;
			int next = getNextInTour(start);
			int nextNext;

			do {
				nextNext = getNextInTour(next);

				tour[next] = current;

				current = next;
				next = nextNext;
			} while (current != end);
		}
	}

	boolean isValid() {
		synchronized (tourLock) {
			int count = 1;
			int node = tour[0];
			while (node != 0) {
				node = getNextInTour(node);
				count++;
			}
			return count == problemSize;
		}
	}

	int[] getCopy() {
		synchronized (tourLock) {
			return tour.clone();
		}
	}

	void setTour(int[] tour) {
		synchronized (tourLock) {
			tour = tour.clone();
			if (Helper.DEBUG_MODE) {
				System.out.println("tour set");
				System.out.println(this);
			}
		}
	}

	public int[] getAdjacencyList() {
		synchronized (tourLock) {
			int[] result = new int[problemSize];

			int node = 0;
			for (int index = 0; index < problemSize; index++, node = tour[node]) {
				result[index] = node;
			}
			if (node != 0) {
				throw new IllegalStateException();
			}
			return result;
		}
	}
}
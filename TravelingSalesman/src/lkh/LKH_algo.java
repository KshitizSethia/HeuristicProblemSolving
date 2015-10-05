/**
 * Literature: http://www.complex-systems.com/pdf/13-4-1.pdf (terminology used from here)
 * Video: https://www.youtube.com/watch?v=GsMZYDBFJv4 (for boundary conditions and accepting a removal/insertion)
 * Parallel implementation in c++ : https://github.com/lingz/LK-Heuristic
 */

package lkh;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import common.Helper;
import common.ImmutableSolution;

public class LKH_algo {

	final int problemSize;
	final double[][] distances;

	Tour tour;

	public LKH_algo(List<double[]> coordinates) {

		problemSize = coordinates.size();

		distances = new double[problemSize][problemSize];
		for (int i = 0; i < problemSize; i++) {
			for (int j = i; j < problemSize; j++) {
				double[] coord_i = coordinates.get(i);
				double[] coord_j = coordinates.get(j);

				double distance = Math.sqrt(Math
						.pow(coord_i[0] - coord_j[0], 2)
						+ Math.pow(coord_i[1] - coord_j[1], 2)
						+ Math.pow(coord_i[2] - coord_j[2], 2));

				distances[i][j] = distance;
				distances[j][i] = distance;
			}
		}

		this.tour = new Tour(initTour(problemSize), problemSize);
	}

	private int[] initTour(int size) {
		int[] result = new int[size];
		for (int index = 0; index < size; index++) {
			
			result[index] = (index+ 1) % size;
		}
		return result;
	}

	public void optimizeTour() {
		double old_distance = getDistance(tour);
		for (int round = 0; round < 100; round++) {
			for (int startIndex = 0; startIndex < problemSize; startIndex++) {
				executeOneLKRound(startIndex);
			}

			double new_distance = getDistance(this.tour);
			double gain = old_distance - new_distance;

			if (round != 0) {
				/*
				 * if (!(gain > 0)) { throw new IllegalStateException(); }
				 */
				if (gain == 0) {
					break;
				}
			}
			old_distance = new_distance;
			if (Helper.DEBUG_MODE) {
				System.out.println("cost: " + new_distance);
			}
		}
	}

	double getDistance(Tour inputTour) {
		double totalDistance = 0;
		for (int index = 0; index < problemSize - 1; index++) {
			totalDistance += distances[index][inputTour.getNextInTour(index)];
		}
		return totalDistance;
	}

	private void executeOneLKRound(int startIndex) {
		if (Helper.DEBUG_MODE) {
			System.out.println("At start of LK round, starting at "
					+ startIndex);
			System.out.println(tour);
		}
		Set<String> broken_edges = new HashSet<String>();
		Set<String> joined_edges = new HashSet<String>();

		Tour localCopyOfTour = new Tour(tour);

		double gain_thisRound = 0;
		double max_gain = 0;

		// u1 -- u2 will be removed if gain is there
		// u1 starts at startIndex and jumps to u3 each round
		int u1 = startIndex;
		int u2 = localCopyOfTour.getNextInTour(u1);

		// u4 will be the node preceding u3, u4 -- u3 will be removed if gain is
		// there
		int u4 = -1;

		// this is the node which will be chosen for starting next round and the
		// preceding edge will be removed
		int u3 = -1;
		do {
			u3 = -1;

			String u1_to_u2 = getEdgeName(u1, u2);
			double u1_to_u2_cost = distances[u1][u2];

			if (joined_edges.contains(u1_to_u2)) {
				break;
			}

			for (int u3_candidate = localCopyOfTour.getNextInTour(u2); u3 == -1
					&& u3_candidate != startIndex; u3_candidate = localCopyOfTour
					.getNextInTour(u3_candidate)) {

				if (Helper.DEBUG_MODE) {
					System.out
							.format("trying %d as u3 || startIndex: %d, u1: %d, u2: %d\n",
									u3_candidate, startIndex, u1, u2);
				}
				final boolean chooseThisu3_candidate = shouldThis_u3_candidate_BeAccepted(
						localCopyOfTour, broken_edges, joined_edges,
						gain_thisRound, u2, u4, u1_to_u2_cost, u3_candidate);

				if (chooseThisu3_candidate) {
					u3 = u3_candidate;
				} else {
					u4 = u3_candidate;
					continue;
				}
			}

			// u3 has been selected
			if (u3 != -1) {

				// edges to be broken and added are stored
				broken_edges.add(u1_to_u2);
				joined_edges.add(getEdgeName(u2, u3));

				double u2_to_startIndex = distances[u2][startIndex];

				double gain_when_closing_tour = gain_thisRound + u1_to_u2_cost
						- u2_to_startIndex;

				if (gain_when_closing_tour > max_gain) {
					// close the tour
					max_gain = gain_when_closing_tour;

					tour = new Tour(localCopyOfTour);
					tour.setNextInTour(startIndex, u2);

					if (Helper.DEBUG_MODE) {
						System.out.println("printing object's tour");
						System.out.println(tour);
					}
				}

				gain_thisRound += u1_to_u2_cost - distances[u2][u3];

				localCopyOfTour.reverseTour(u2, u4);
				localCopyOfTour.setNextInTour(u2, u3);

				// move u1 and u2 ahead
				u1 = u3;
				u2 = u4;
			}
			// while changes are possible
		} while (u3 != -1);

		if (!tour.isValid()) {
			throw new IllegalStateException();
		}
	}

	boolean shouldThis_u3_candidate_BeAccepted(Tour localCopyOfTour,
			final Set<String> broken_edges, final Set<String> joined_edges,
			final double gain_thisRound, final int u2, int u4,
			double u1_to_u2_cost, int u3_candidate) {

		double gainInRemoving_u1_to_u2_andAdding_u2_to_u3 = u1_to_u2_cost
				- distances[u2][u3_candidate];

		String u4_to_u3 = getEdgeName(u3_candidate, u4);
		double gain_in_replacing_k_edges_without_closing_tour = gain_thisRound
				+ gainInRemoving_u1_to_u2_andAdding_u2_to_u3;

		// see if this p2 is to be accepted
		boolean u2_to_u3_candidate_alreadyBroken = broken_edges
				.contains(getEdgeName(u2, u3_candidate));
		boolean positive_gain = gain_in_replacing_k_edges_without_closing_tour > 0;
		//
		boolean u4_to_u3_alreadyJoined = joined_edges.contains(u4_to_u3);
		boolean u3_NotLastNodeOnTour = localCopyOfTour
				.getNextInTour(u3_candidate) != 0;
		boolean u3_NotNextOf_u2 = u3_candidate != localCopyOfTour
				.getNextInTour(u2);

		boolean result = !u2_to_u3_candidate_alreadyBroken
				&& !u4_to_u3_alreadyJoined && positive_gain
				&& u3_NotLastNodeOnTour && u3_NotNextOf_u2;

		if (Helper.DEBUG_MODE) {
			if (!result) {
				System.out.print("\t" + u3_candidate + " not chosen because: ");
				if (u2_to_u3_candidate_alreadyBroken) {
					System.out.print("u3 to u2 was broken earlier, ");
				}
				if (u4_to_u3_alreadyJoined) {
					System.out.print("u4 to u3 was joined earlier, ");
				}
				if (!positive_gain) {
					System.out.print("gain is not positive, ");
				}
				if (!u3_NotLastNodeOnTour) {
					System.out.print("it is last node on tour, ");
				}
				if (!u3_NotNextOf_u2) {
					System.out.print("it is next node of u2, ");
				}
				System.out.println("");
			} else {
				System.out.format("\t%d chosen!\n", u3_candidate);
			}
		}

		return result;
	}

	private String getEdgeName(int node1, int node2) {
		if (node1 < node2) {
			return node1 + "--" + node2;
		} else {
			return node2 + "--" + node1;
		}
	}

	public ImmutableSolution getBestSolution() {
		synchronized (tour) {
			Tour bestTour = tour;
			ImmutableSolution solution = new ImmutableSolution(
					bestTour.getAdjacencyList(), getDistance(bestTour));
			return solution;
		}
	}

}

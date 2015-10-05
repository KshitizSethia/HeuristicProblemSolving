#include "LKMatrix.h"
#include <vector>
#include <cmath>
#include <set>
#include <iostream>
#include <cassert>
#include <cstdlib>
#include <ctime>
#include <sys/time.h>
#include <stdio.h>
#include <unistd.h>

using namespace std;

pair<int, int> make_sorted_pair(int x, int y) {
	if (x < y) {
		return make_pair(x, y);
	} else {
		return make_pair(y, x);
	}
}

LKMatrix::LKMatrix(vector<vector<double> > &coords, vector<int> &ids) {
	this->coords = coords;
	this->ids = ids;
	size = ids.size();

	// initialize tour
	tour = vector<int>(size, 0);

	// initial 'random' tour
	for (int i = 0; i < size - 1; i++) {
		tour[i] = (i + 1) % size;
	}

	// sets the distanceVector
	edgeDistances = vector<vector<double> >(size, vector<double>(size, 0));

	double edgeDistance;
	for (int i = 0; i < size; i++) {
		for (int j = i; j < size; j++) {
			// Compute the edge distance
			edgeDistance = sqrt(
					pow((coords[i][0] - coords[j][0]), 2)
							+ pow((coords[i][1] - coords[j][1]), 2)
							+ pow((coords[i][2] - coords[j][2]), 2));
			edgeDistances[i][j] = edgeDistance;
			edgeDistances[j][i] = edgeDistance;

			//cout << "distance between " << i << " and " << j << ": ";
			//cout <<edgeDistance<<endl; //<<edgeDistance<<endl;

		}
	}
}

double LKMatrix::getCurrentTourDistance() {
	//int currentIndex = 0;
	double distance = 0;
	for (int i = 0; i < size - 1; i++) {
		//cout << edgeDistances[i][tour[i]] << "; ";
		distance += edgeDistances[i][tour[i]];
	}
	//cout << endl;

	return distance;
}

void LKMatrix::LKMove(int tourStart) {
	set<pair<int, int> > broken_set, joined_set;
	vector<int> tour_opt = tour;
	double max_gain__g_opt = 0;
	double gain_thisRound_g = 0; // := G_i
	//double g_local; // := g_i
	int maybe_t1 = tourStart;
	int maybe_t2;
	int p1_nextV;
	//int temp__nextFromV;
	int p3_lastPossibleNextV;
	pair<int, int> broken_edge;
	//double y_opt_length;
	double broken_edge_length;
	//double g_opt_local;

	maybe_t2 = tour[maybe_t1];
	long initialTourDistance = getCurrentTourDistance();

	do {
		// default, no nextV is found
		p1_nextV = -1;

		broken_edge = make_sorted_pair(maybe_t1, maybe_t2); // := x_i
		broken_edge_length =
				edgeDistances[broken_edge.first][broken_edge.second];

		//cout << "Breaking " << lastNextV << " " << fromV << endl;
		//cout << "Testing from " << fromV << endl;;
		//cout << "Breaking of length: " << broken_edge_length << endl;

		// Condition 4(c)(1)
		if (joined_set.count(broken_edge) > 0)
			break;		//store current tour as optimal and print it

		// y_i := (fromV, nextV)
		for (int p2_possibleNextV = tour[maybe_t2];
				p1_nextV == -1 && p2_possibleNextV != tourStart;
				p2_possibleNextV = tour[p2_possibleNextV]) {
			//cout << "Testing " << possibleNextV << endl;
			//cout << (broken_set.count(make_sorted_pair(fromV, possibleNextV)) == 0) << endl;
			//cout << (possibleNextV != fromV) << endl;
			//cout << (g + g_local > 0) << endl;
			//cout << (joined_set.count(make_sorted_pair(possibleNextV, tour[possibleNextV])) == 0) << endl;

			// calculate local gain
			double g_local = broken_edge_length
					- edgeDistances[maybe_t2][p2_possibleNextV];

			// condition 4(c)(2)
			bool brokenSetDoesntHaveT2toT3 = (broken_set.count(
					make_sorted_pair(maybe_t2, p2_possibleNextV)) == 0);
			// condition 4(d)
			bool totalGainPositive = (gain_thisRound_g + g_local) > 0;
			// condition 4(e)
			// x_{i+1} has never been joined before
			bool XiPlus1HasNeverBeenJoinedBefore = (joined_set.count(
					make_sorted_pair(p3_lastPossibleNextV, p2_possibleNextV)) == 0);
			bool T3NotLastNodeInTour = (tour[p2_possibleNextV] != 0);
			bool t3IsNotNextOfT2 = p2_possibleNextV != tour[maybe_t2];
			bool conditionsWhichMakeThisT3AValidT3 = brokenSetDoesntHaveT2toT3
					&& totalGainPositive && XiPlus1HasNeverBeenJoinedBefore
					&& T3NotLastNodeInTour && t3IsNotNextOfT2;

			// conditions that make this edge not a valid y_i
			if (!conditionsWhichMakeThisT3AValidT3) {
				p3_lastPossibleNextV = p2_possibleNextV;
				continue;
			}

			// If we are here, then y_i := (fromV, possibleNextV)
			p1_nextV = p2_possibleNextV;
			//cout << "Moving to " << nextV << endl;
		}

		// a next y_i exists
		if (p1_nextV != -1) {

			// add to our broken_set and joined_set
			broken_set.insert(broken_edge);
			joined_set.insert(make_sorted_pair(maybe_t2, p1_nextV));

			//TODO can we remove this to make path instead of tour?
			// condition 4(f)
			double y_opt_length = edgeDistances[maybe_t2][tourStart]; // y_i_opt

			// The tour length if we exchanged the broken edge (x_i)
			// with y_opt, (t_{2i}, t_0)
			double cumulativeGain__g_opt_local = gain_thisRound_g + (broken_edge_length - y_opt_length);

			if (cumulativeGain__g_opt_local > max_gain__g_opt) {
				//vector<int> temp_tour = tour;
				//temp_tour[tourStart] = fromV;
				//tour = tour_opt;
				//printTour();
				//long old_distance = getCurrentTourDistance();
				//tour = temp_tour;
				//printTour();
				//long new_distance = getCurrentTourDistance();
				//cout << "(Temp) Joining of distance: " << y_opt_length << endl;
				//cout << "Old distance: " << old_distance << endl;
				//cout << "New distance: " << new_distance << endl;
				//assert(new_distance <= old_distance);
				max_gain__g_opt = cumulativeGain__g_opt_local;
				tour_opt = tour;
				// join the optimal tour
				tour_opt[tourStart] = maybe_t2;
			}

			//cout << "Joining of distance: " << edgeDistances[fromV][nextV] << endl;

			// recalculate g
			gain_thisRound_g += broken_edge_length - edgeDistances[maybe_t2][p1_nextV];

			// reverse tour direction between newNextV and fromV
			// implicitly breaks x_i
			reverse(maybe_t2, p3_lastPossibleNextV);

			// remember our new t_{2i+1}
			int temp__nextFromV = p3_lastPossibleNextV;
			//cout << "Joined to " << nextFromV << endl;

			// build y_i
			tour[maybe_t2] = p1_nextV;

			// set new fromV to t_{2i+1}
			// and out lastNextV to t_{2i}
			maybe_t1 = p1_nextV;
			maybe_t2 = temp__nextFromV;

		}

	} while (p1_nextV != -1);

	// join up
	//cout << "terminated" << endl;
	tour = tour_opt;
	long distanceAfter = getCurrentTourDistance();
	//assert(distanceAfter <= initialTourDistance);//FIXME

	printTour();
	assert(isTour());

}

void LKMatrix::optimizeTour() {
	// we need to test for convergence and also the difference from last time
	int diff;
	int old_distance = 0;
	int new_distance = 0;

	for (int j = 0; j < 100; j++) {
		for (int i = 0; i < size; i++) {
			LKMove(i);
			//cout<<"done with round " <<i<<endl;
		}
		new_distance = getCurrentTourDistance();
		diff = old_distance - new_distance;
		if (j != 0) {
			assert(diff >= 0);
			if (diff == 0) {
				//cout << "Converged after " << j << " iterations" << endl;
				break;
			}
		};
		old_distance = new_distance;
		cout << "cost: " << new_distance << endl;
	}
}

/*
 * Reverse the tour between indices start and end
 */
void LKMatrix::reverse(int start, int end) {
	int current = start;
	int next = tour[start];
	int nextNext;
	do {
		//cout << "reversing" << endl;
		// look ahead to where we need to go after this iteration
		nextNext = tour[next];

		// reverse the direction at this point
		tour[next] = current;

		// move to the next pointer
		current = next;
		next = nextNext;
	} while (current != end); // terminate once we've reversed up to end
}

// Sanity check function
bool LKMatrix::isTour() {
	int count = 1;
	int start = tour[0];
	while (start != 0) {
		start = tour[start];
		count++;
	}
	return (count == size);
}

void LKMatrix::printTour() {
	int current = 0;
	do {
		//cout << current << " ; ";
		current = tour[current];
	} while (current != 0);
	//cout << endl;
}

void LKMatrix::printTourIds() {
	int current = 0;
	do {
		cout << ids[current] << endl;
		current = tour[current];
	} while (current != 0);
}

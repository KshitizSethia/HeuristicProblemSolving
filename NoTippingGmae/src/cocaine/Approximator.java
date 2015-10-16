package cocaine;

import java.util.Random;

public class Approximator {

	public static double approx(GameState c, boolean isMaxNode) {
		if (c.isTipping_old()) {
			// If this is a tipping case
			return !isMaxNode ? 1.0 : -1.0;
		}

		if (c.stage == Stage.addition) {
			// Adding Stage ; Equal weight to all 4 heuristics for now.
			// Randomness gets a low weight
			return (addHeuristic1(c, isMaxNode) + 0.32
					* addHeuristic2(c, isMaxNode)
					/* + 0.24 * addHeuristic3(c, isMaxNode) */+ 0.33
					* addHeuristic4(c, isMaxNode) + 0.02 * genRandomNo());
		} else {
			// Removing Stage; Equal weight to all 3 heuristics for now.
			// Randomness gets a low weight
			return (0.49 * removeHeuristic1(c, isMaxNode) + 0.49
					* removeHeuristic2(c, isMaxNode) /*
													 * + 0.33
													 * removeHeuristic3(c,
													 * isMaxNode)
													 */+ 0.02 * genRandomNo());
		}

		// Stage end...
		// return 0.0;
	}

	public static double approx_new(GameState c, boolean isMaxNode) {
		double absoluteCenterOfMass = Math.abs(getCenterOfMassShiftedByPivots(c));

		double result;
		if (absoluteCenterOfMass <= 1) {
			result = -1.999 * absoluteCenterOfMass + 1;
		} else {
			result = -1;
		}
		System.out.format("Converted center of mass %f to value %f.\n",
				absoluteCenterOfMass, result);
		return result;
	}

	private static double getCenterOfMassShiftedByPivots(GameState c) {
		double massByPosition = 0;//GameState.POS_INITIAL_WEIGHT*GameState.WGT_INITIAL_WEIGHT;
		double mass = 0;//GameState.WGT_INITIAL_WEIGHT;
		for (CocaineWeight weight : c.board) {
			if (weight == null) {
				continue;
			}
			massByPosition += weight.weight * (weight.position+2);
			mass += weight.weight;
		}
		final double centerOfMass = massByPosition / mass;
		return centerOfMass;
	}

	/**
	 * Heuristic 1 (Self Feasible Moves - Opponent Feasible Moves)/ Total No of
	 * Moves Feasible move means where tipping doesn't happen This is time
	 * consuming.
	 */
	static double addHeuristic1(GameState c, boolean isMaxNode) {
		// Value returned will be absolute. If its max node, this config is good
		// then value will be positive. If its min node,
		// and this config is good for min, then value will be negative

		int i, j, vacant_spaces;
		// Compute : Feasible Moves for self
		int self_feasible_moves = 0;
		int self_remaining_moves = 0;
		int opponent_feasible_moves = 0;
		int opponent_remaining_moves = 0;

		// Go through the board and figure out vacant spots
		vacant_spaces = 0;
		for (i = -GameState.HALF_BOARD; i <= GameState.HALF_BOARD; i = i + 1) {
			if (c.getWeight(i) == 0) {
				vacant_spaces++;
			}
		}

		for (i = 1; i <= GameState.MAX_WEIGHT; i = i + 1) {
			// Check if self have the weight
			if (c.IHaveWeight(i)) {
				self_remaining_moves++;
				// Go through all the possible positions
				for (j = -GameState.HALF_BOARD; j < GameState.HALF_BOARD; j++) {
					if (c.getWeight(j) == 0) {
						// Place it and see if you tip
						c.makeMove(i, j, PlayerName.none);
						if (!c.isTipping_old()) {
							self_feasible_moves++;
						}
						// Now remove the weight
						c.removeMove(j);
					}
				}
			}
			// Check if opponent has this weight
			if (c.opponentHasWeight(i)) {
				opponent_remaining_moves++;
				// Go through all the possible positions
				for (j = -GameState.HALF_BOARD; j < GameState.HALF_BOARD; j++) {
					// Place the weight and see if you tip
					if (c.getWeight(j) == 0) {
						c.makeMove(i, j, PlayerName.none);
						if (!c.isTipping_old()) {
							opponent_feasible_moves++;
						}
						// Now remove the weight
						c.removeMove(j);
					}
				}
			}
		}

		// Need to take the max because one of the players may have played one
		// chance less
		if (self_remaining_moves == 0 && opponent_remaining_moves == 0) {
			return 0.0;
		} else if (self_remaining_moves > opponent_remaining_moves)
			return (double) (self_feasible_moves - opponent_feasible_moves)
					/ (double) (self_remaining_moves * vacant_spaces);
		else
			return (double) (self_feasible_moves - opponent_feasible_moves)
					/ (double) (opponent_remaining_moves * vacant_spaces);
	}

	// (-self_total_weight + opponent_total_weight)/78
	// Key is to have less Heavy weights
	// Value returned is absolute
	static double addHeuristic2(GameState c, boolean isMaxNode) {
		int i;
		int self_total_weight = 0;
		int opponent_total_weight = 0;

		for (i = 1; i <= GameState.MAX_WEIGHT; i = i + 1) {
			if (c.IHaveWeight(i)) {
				self_total_weight += i;
			}
			if (c.opponentHasWeight(i)) {
				opponent_total_weight += i;
			}
		}

		return (double) (-self_total_weight + opponent_total_weight) / 78.0;
	}

	// todo figure out ideal board config then turn this on
	/*
	 * //Measure of how far is the config away from ideal one // Ideal config
	 * has more heavy weights near the center // Value returned is relative //
	 * Larger the difference better is config for current player because other
	 * player will be changing the board which is away from ideal double
	 * addHeuristic3(GameState c, int max) { int total_board_weight = 0; int
	 * curWeight = 0; int sum_of_abs_diff = 0; int i;
	 * 
	 * for (i = -GameState.HALF_BOARD; i <= GameState.HALF_BOARD; i = i + 1) {
	 * curWeight = config_board(c, i); if (curWeight > 0) { //There is a weight
	 * total_board_weight += curWeight; sum_of_abs_diff += (abs(curWeight -
	 * ideal_board_config[i + GameState.HALF_BOARD])); } }
	 * 
	 * if (total_board_weight == 0) { return 0.0; } else if (max == 1) { return
	 * ((double) sum_of_abs_diff / (double) total_board_weight); } else { return
	 * (-1.0 * ((double) sum_of_abs_diff / (double) total_board_weight)); }
	 * 
	 * }
	 */

	// |Sum of two torques| / (Max possible torque)
	static double addHeuristic4(GameState c, boolean isMaxNode) {
		// (-306 , 6) = 300 is the largest possible sum which is possible
		if (isMaxNode) {
			return (double) (Math.abs(c.torque_1() + c.torque_2()))
					/ (double) 300.0;
		} else {
			return -(double) (Math.abs(c.torque_1() + c.torque_2()))
					/ (double) 300.0;
		}
	}

	// (Feasible Remove Moves/ Total No of weights currently on the board)
	// Relative value
	static double removeHeuristic1(GameState c, boolean isMaxNode) {
		int i;
		int curWeight = 0;
		int feasible_remove_moves = 0;
		int total_moves = 0;
		for (i = -GameState.HALF_BOARD; i <= GameState.HALF_BOARD; i = i + 1) {
			curWeight = c.getWeight(i);
			if (curWeight != 0) {
				total_moves++;
				// There is a weight at this position. Try Removing it
				CocaineWeight w = c.removeMove(i);
				// Check if the board tipped
				if (c.isTipping_old() == false) {
					feasible_remove_moves++;
				}
				// Place the weight back
				c.makeMove(curWeight, i, w.player);
			}
		}
		if (total_moves == 0) {
			return 0.0;
		} else if (isMaxNode) {
			return ((double) (feasible_remove_moves) / (double) (total_moves));
		} else {
			return (-1.0 * ((double) (feasible_remove_moves) / (double) (total_moves)));
		}
	}

	// |Sum of two torques| (max possible sum)
	// Relative value
	static double removeHeuristic2(GameState c, boolean isMaxNode) {
		return addHeuristic4(c, isMaxNode);
	}

	// todo restore this heuristic once ideal distribution is known
	/*
	 * //How much away is it from the ideal distribution //Relative Value
	 * //Almost Same as addHeuristic3 double removeHeuristic3(config* c, int
	 * max) { int total_ideal_board_weight = 0; int curWeight = 0; int
	 * sum_of_abs_diff = 0; int i;
	 * 
	 * for (i = -GameState.HALF_BOARD; i <= GameState.HALF_BOARD; i = i + 1) {
	 * curWeight = config_board(c, i); if (curWeight != 0) { //There is a weight
	 * total_ideal_board_weight += ideal_board_config[i + GameState.HALF_BOARD];
	 * sum_of_abs_diff += (abs(curWeight - ideal_board_config[i +
	 * GameState.HALF_BOARD])); } }
	 * 
	 * if (total_ideal_board_weight == 0) { return 0.0; } else if (max == 1) {
	 * return (double) sum_of_abs_diff / (double) total_ideal_board_weight; }
	 * else { return -1.0 ((double) sum_of_abs_diff / (double)
	 * total_ideal_board_weight); }
	 * 
	 * }
	 */

	// Generates a random no between -1 and 1
	static double genRandomNo() {
		// A random no between -1 and 1
		return 2.0 * new Random().nextDouble() - 1.0;
	}
}

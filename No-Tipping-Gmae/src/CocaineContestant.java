import java.util.Random;

public final class CocaineContestant extends NoTippingPlayer {
	public static final int INT_MAX = Integer.MAX_VALUE;

	CocaineContestant() {
		super(8014);
	}

	CocaineContestant(int x) {
		super(x);
	}

	private static boolean hasGameStarted = false;
	private static GameState state;
	private static PlayerName player;
	private static boolean firstRemove = false;

	@Override
	protected String process(String command) {
		if (!hasGameStarted) {
			hasGameStarted = true;
			state = new GameState();
			player = PlayerName.Player2;
			state.setPlayer(player);
		}

		String[] commands = command.split("\\s+");

		Stage stage = Stage.addition;
		switch (commands[0]) {
		case "REMOVING":
			stage = Stage.removal;
			break;
		default:
			stage = Stage.addition;
		}
		state.stage = stage;
		int position = Integer.parseInt(commands[1]);
		int weight = Integer.parseInt(commands[2]);

		if (position == 0 && weight == 0) {
			player = PlayerName.Player1;
			firstRemove = true;
			state.setPlayer(player);
		} else {
			if (stage == Stage.removal) {
				if (state.isFull() && firstRemove) {
					state.makeMove(weight, position, getOtherPlayer());
					firstRemove = false;
				} else {
					state.removeMove(position);
				}
			} else {
				// Add
				state.makeMove(weight, position, getOtherPlayer());
			}
		}

		state.setDepth(decideDepth(state));

		String move = prune(state, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		String[] moveParts = move.split("\\s+");
		String returnValue;
		if (stage == Stage.addition) {
			state.makeMove(Integer.parseInt(moveParts[1]),
					Integer.parseInt(moveParts[0]), player);
			if (state.count == GameState.MAX_WEIGHT * 2 + 1) {
				returnValue = moveParts[0] + " " + moveParts[1];
			} else {
				returnValue = moveParts[0] + " " + moveParts[1];
			}
		} else {
			state.removeMove(Integer.parseInt(moveParts[0]));
			returnValue = moveParts[0] + " " + moveParts[1];
		}

		return returnValue;
	}

	private int decideDepth(GameState c) {
		int result = 1;
		if (c.stage == Stage.addition) {
			// Adding Stage
			
			// If count is less than 5 , crucial state of game go deep
			if (c.count < 5) {
				result = 3;
			}
		} else if (c.stage == Stage.removal) {
			// Removing Stage
			result = 3;
			int count = 0;
			for (int position = -GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
				if (c.getWeight(position) > 0) {
					count += 1;
				}
			}
			// Depending on number of remaining weights change your depth
			if (count < 14) {
				// Crucial Stage go to depth
				result = 15;
			} else if (count < 22) {
				result = 4;
			} else {
				// printf("0,0\n0\n");//todo uncomment this
			}

		}

		// Randomize this depth
		if (result > 1) {
			if (new Random().nextDouble() < GameState.DEPTH_RATIO) {
				result = result - 1;
			}
		}

		return result;
	}

	public PlayerName getOtherPlayer() {
		if (this.player == PlayerName.Player1) {
			return PlayerName.Player2;
		}
		return PlayerName.Player1;
	}

	String getReturnString(int position, int weight, double alpha) {
		return position + " " + weight + " " + alpha;
	}

	String prune(GameState c, double alpha, double beta) {
		GameState next;
		int chosen_weight = INT_MAX, chosen_position = INT_MAX;
		double score;

		if (c.stage == Stage.addition) {
			// Adding Stage
			for (int weight = 1; weight <= GameState.MAX_WEIGHT; weight++) {
				// Check if you have this weight
				if (c.IHaveWeight(weight)) {
					for (int position = -GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
						// Try this board position
						if (c.boardFreeAt(position)) {
							next = (GameState) c.clone();
							next.makeMove(weight, position, player);
							if (!next.isTipping()) {

								// This move didn't cause tipping; Extract the
								// score from each of my min nodes; Choose the
								// child with max score
								score = prunemin(next, 1, alpha, beta);
								if (alpha < score) { // score > alpha) {
									alpha = score;
									chosen_weight = weight;
									chosen_position = position;
								}
								// If the score was 1.0. Bingo found one.This is
								// called "Solve Optimization over minimax"
								if (alpha == 1.0) {
									return getReturnString(chosen_position,
											chosen_weight, alpha);
								}
							}
						}
					}
				}
			}

			if (chosen_weight == INT_MAX && chosen_position == INT_MAX) {
				// No optimal answer found; i.e every move caused a tipping . I
				// will loose just choose a placement
				alpha = -1.0;
				for (int weight = 1; weight <= GameState.MAX_WEIGHT; weight++) {
					if (c.IHaveWeight(weight)) {
						for (int position = -GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
							// Try this board position
							if (c.boardFreeAt(position)) {
								chosen_weight = weight;
								chosen_position = position;
								break;
							}
						}
						break;
					}
				}
			}

		} else if (c.stage == Stage.removal) {

			// Removing Stage
			for (int position = -1 * GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
				if (c.getWeight(position) > 0) {
					// There exist a weight at this position
					next = (GameState) c.clone();
					next.removeMove(position);
					if (!next.isTipping()) {
						// Not tipping ; Compute a score for your min childs
						score = prunemin(next, 1, alpha, beta);
						if (score > alpha) {
							alpha = score;
							chosen_weight = c.getWeight(position);
							chosen_position = position;
						}
						if (alpha == 1.0) {
							// printf("%d,%d\n", weight, board);//todo uncomment
							// this
							return getReturnString(chosen_position,
									chosen_weight, alpha);
						}
					}
				}
			}

			if (chosen_weight == INT_MAX && chosen_position == INT_MAX) {
				// No optimal answer found; i.e every move caused a tipping . I
				// will loose just choose a placement
				alpha = -1.0;
				for (int position = -GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
					// Try this board position
					if (c.getWeight(position) > 0) {
						chosen_weight = c.getWeight(position);
						chosen_position = position;
						break;
					}
				}
			}

		} else {
			throw new IllegalStateException();
			// return "";
		}

		if (chosen_position > GameState.HALF_BOARD || chosen_position < -GameState.HALF_BOARD) {
			throw new IllegalStateException();
		}
		return getReturnString(chosen_position, chosen_weight, alpha);
	}

	// Max and Min functions will call themselves recursively
	double prunemax(GameState c, int depth, double alpha, double beta) {
		if (depth > c.getMaxDepth()) {
			// Its time to approx at this level
			return Approximator.approx(c, true);
		}

		GameState next;
		double score = 0.0;
		int p = 0;

		if (c.stage == Stage.addition) {
			// Adding Stage
			p = 0;
			for (int weight = 1; weight <= GameState.MAX_WEIGHT; weight = weight + 1) {
				if (c.IHaveWeight(weight)) {

					// Try this weight at all positions
					for (int position = -GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
						if (c.getWeight(position) == 0) {
							// There is no weight at this position
							next = (GameState) c.clone();
							next.makeMove(weight, position, player);
							if (!next.isTipping()) {
								p = 1; // Some Progress

								score = prunemin(next, depth + 1, alpha, beta); // Get

								if (score > alpha) {
									// Better score
									alpha = score;
								}

								if (alpha >= beta) {
									// During the entire game for every node
									// alpha should be less than beta. Remember
									// this prunemax will be called prunemin
									// and beta is best possible min value move
									// that it could make. so if this alpha will
									// be greater than that i will never
									// make this move. So Cut the search space
									// and just return beta
									// Intutively its like I have something
									// better but my opponent will never take
									// this move
									return beta;
								} else if (alpha == 1.0) {
									// Found the best possible score; Return it
									// to your opponent; Remember the goal is
									// assume that your opponent will
									// make the best possible move and if this
									// move makes me the winner I won't search
									// further because my opponent will know
									// this and thereby never make this move
									return alpha;
								}
							}
						}
					}
				}
			}

		} else if (c.stage == Stage.removal) {
			// Removing Stage
			p = 0;
			for (int position = -GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
				if (c.getWeight(position) > 0) {
					// There exist a weight at this position
					next = (GameState) c.clone();
					next.removeMove(position);
					if (!next.isTipping()) {
						// Not tipping ; Compute a score for your min childs
						score = prunemin(next, depth + 1, alpha, beta);
						if (score > alpha) {
							alpha = score;
						}

						if (alpha >= beta) {
							return beta;
						} else if (alpha == 1.0) {
							return alpha;
						}

					}
				}
			}

		} else {
			// Game Ended
			alpha = 0.0;
		}

		if (p == 0) {
			// I can't make a move so I loose
			alpha = -1.0;
		}

		return alpha;

	}

	// Max and Min functions will call themselves recursively
	double prunemin(GameState c, int depth, double alpha, double beta) {
		if (depth > c.getMaxDepth()) {
			// Its time to approx at this level
			return Approximator.approx(c, false);
		}

		GameState next;
		double score = 0.0;
		int p = 0;

		if (c.stage == Stage.addition) {
			// Adding Stage
			p = 0;
			for (int weight = 1; weight <= GameState.MAX_WEIGHT; weight = weight + 1) {
				if (c.IHaveWeight(weight)) {

					// Try this weight at all positions
					for (int position = -GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
						if (c.getWeight(position) == 0) {
							// There is no weight at this position
							next = (GameState) c.clone();
							next.makeMove(weight, position, player);
							if (!next.isTipping()) {
								p = 1; // Some Progress

								score = prunemax(c, depth + 1, alpha, beta); // Get
																				// the
																				// scores
																				// from
																				// all
																				// your
																				// min
																				// childrens
																				// and
																				// take
																				// the
																				// max
																				// one

								if (score < beta) {
									// Better score
									beta = score;
								}

								if (alpha >= beta) {
									// During the entire game for every node
									// alpha should be less than beta. Remember
									// this prunemax will be called prunemin
									// and beta is best possible min value move
									// that it could make. so if this alpha will
									// be greater than that i will never
									// make this move. So Cut the search space
									// and just return beta
									return alpha;
								} else if (beta == -1.0) {
									// Found the best possible score; Return it
									// to your opponent; Remember the goal is
									// assume that your opponent will
									// make the best possible move and if this
									// move makes me the winner I won't search
									// further because my opponent will know
									// this and thereby never make this move
									return beta;
								}
							}
						}
					}
				}
			}

		} else if (c.stage == Stage.removal) {
			// Removing Stage
			p = 0;
			for (int position = -GameState.HALF_BOARD; position <= GameState.HALF_BOARD; position++) {
				if (c.getWeight(position) > 0) {
					// There exist a weight at this position
					next = (GameState) c.clone();
					next.removeMove(position);// config_remove(&next, j);
					if (!next.isTipping()) {
						// Not tipping ; Compute a score for your min childs
						score = prunemax(next, depth + 1, alpha, beta);
						if (score < beta) {
							beta = score;
						}

						if (alpha >= beta) {
							return alpha;
						} else if (beta == -1.0) {
							return beta;
						}

					}
				}
			}

		} else {
			// Game Ended
			beta = 0.0;
		}

		if (p == 0) {
			// I can't make a move so I loose
			beta = 1.0;
		}

		return beta;

	}

	public static void main(String[] args) {
		new CocaineContestant();
	}
}

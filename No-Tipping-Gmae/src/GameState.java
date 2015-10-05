import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameState {

	public static final int MAX_WEIGHT = 15;
	public static final int BOARD_SIZE = 51;
	public static final int HALF_BOARD = BOARD_SIZE / 2;

	private static final int POS_PIVOT1 = -3;
	private static final int POS_PIVOT2 = 1;
	private static final int POS_INITIAL_WEIGHT = -4;
	private static final int WGT_INITIAL_WEIGHT = 3;
	public static final double DEPTH_RATIO = 8;

	public final List<Integer> my_weights;
	public final List<Integer> oppn_weights;
	// todo see diff between board and weights_on_board
	public final CocaineWeight[] board;
	//public final List<CocaineWeight> weights_on_board;
	// public final List<CocaineWeight> weights_on_board;
	public Stage stage;
	private int maxDepth;
	public int count;
	public PlayerName player;
	
	public GameState(){
		board = new CocaineWeight[BOARD_SIZE];
		//weights_on_board = new ArrayList<>();
		board[POS_INITIAL_WEIGHT+HALF_BOARD] = new CocaineWeight(WGT_INITIAL_WEIGHT, POS_INITIAL_WEIGHT,
				PlayerName.none);
		count = 1;
		my_weights = new ArrayList<Integer>();
		oppn_weights = new ArrayList<Integer>();
		
		for(int i = 1; i <= MAX_WEIGHT; i++) {
			my_weights.add(i);
			oppn_weights.add(i);
		}
		
		stage = Stage.addition;
		maxDepth = 1;
	}
	
	private GameState(Stage stage, List<Integer> my_weights,
			List<Integer> oppn_weights, CocaineWeight[] board, int maxDepth, int count, PlayerName player) {
		this.stage = stage;
		this.my_weights = new LinkedList<Integer>(my_weights);
		this.oppn_weights = new LinkedList<Integer>(oppn_weights);
		this.board = board;
		this.maxDepth = maxDepth;
		this.count = count;
		this.player = player;
	}
	
	/*public GameState(Stage stage, int player, List<Integer> weights,
			List<CocaineWeight> weights_on_board) {
		this.my_weights = weights;

		oppn_weights = new LinkedList<Integer>();
		for (int weight = 1; weight <= MAX_WEIGHT; weight++) {
			oppn_weights.add(weight);
		}
		board = new CocaineWeight[BOARD_SIZE];
		for (CocaineWeight w : weights_on_board) {
			board[w.position] = w;
			if (w.player != player) {
				oppn_weights.remove(new Integer(w.weight));
				// todo: see if this works
			}
		}
		this.stage = stage;

		this.maxDepth = decideDepth(this);
	}
*/
	public boolean isFull() {
		return count == MAX_WEIGHT * 2;
	}

	public boolean IHaveWeight(int i) {
		return my_weights.contains(new Integer(i));
	}

	public boolean opponentHasWeight(int i) {
		return oppn_weights.contains(new Integer(i));
	}

	public boolean boardFreeAt(int j) {
		return board[j+HALF_BOARD] == null;
	}

	@Override
	protected Object clone() {
		GameState clone = new GameState(stage, my_weights, oppn_weights,
				board.clone(), maxDepth, count, player);
		return clone;
	}

	public void makeMove(int weight, int position, PlayerName player) {
		board[position + HALF_BOARD] = new CocaineWeight(weight, position, player);
		count++;
		if(this.player==player){
			my_weights.remove(new Integer(weight));
		}else
			oppn_weights.remove(new Integer(weight));
	}

	public CocaineWeight removeMove(int i) {
		CocaineWeight removed = board[i+HALF_BOARD];
		board[i + HALF_BOARD] = null;
		count--;
		if(removed.player==this.player){
			my_weights.add(removed.weight);
		}else{
			oppn_weights.add(removed.weight);
		}
		
		return removed;
	}

	public boolean isTipping() {
		if (torque_1() < 0 || torque_2() > 0) {
			return true;
		}
		return false;
	}

	public int torque_2() {
		int ret = 0;
		for (int i = -HALF_BOARD; i <= HALF_BOARD; i++) {
			ret = ret + (i + 1) * getWeight(i);
		}
		return ret;
	}

	public int torque_1() {
		int ret = 0;
		//int i;

		for (int i = -HALF_BOARD; i <= HALF_BOARD; i++) {
			ret += (i + 3) * getWeight(i);
		}
		return ret;
	}

	public int getWeight(int i) {
		if (board[i + HALF_BOARD] == null) {
			return 0;
		}
		return board[i + HALF_BOARD].weight;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setPlayer(PlayerName player) {
		this.player = player;
		
	}

	public void setDepth(int depth) {
		maxDepth = depth;
	}

}

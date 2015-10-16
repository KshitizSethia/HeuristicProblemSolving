package cocaine;
import java.util.HashMap;
import java.util.Map;

class TableEntry {
	 int lowerBound = Integer.MIN_VALUE;
	 int upperBound = Integer.MAX_VALUE;
	 CocaineWeight bestMove = null;
	 int furthestDepthSearched = -1;
	 boolean isTerminal = false;
	 int outcome; //+1, 0, -1
	 
	 TableEntry(int lb, int ub, CocaineWeight bm, int fds, boolean it, int o) {
		 lowerBound = lb;
		 upperBound = ub;
		 bestMove = bm;
		 furthestDepthSearched = fds;
		 isTerminal = it;
		 outcome = o;
	 }
}

public class TranspositionTable {
	static Map<Integer, TableEntry> cache;// = new HashMap<>();
	static TranspositionTable instance;
	
	private TranspositionTable() {
		cache = new HashMap<Integer, TableEntry>();
	}
	
	public static TranspositionTable getInstance() {
		if(instance == null) {
			instance = new TranspositionTable();
		}
		return instance;
	}
	
	public static TableEntry Lookup(int hash, int depth) {
		/*
		 def lookup(self, node, depth): 
			 32          ttn=self.cache.get(node,None) 
			 33          if ttn is None: 
			 34              return ttn 
			 35          if depth<=ttn.furthestDepthSearched: 
			 36              return ttn 
			 37          if ttn.isTerminal: 
			 38              return ttn 
			 39          del self.cache[node] 
			 40          return None 
		*/
		TableEntry t = cache.get(hash);
		if(t == null) {
			return null;
		}
		if(depth <= t.furthestDepthSearched) {
			return t;
		}
		if(t.isTerminal) {
			return t;
		}
		
		cache.remove(hash);
		return null;
	}
	
	/*
	def mtdf(node, f, depth): 
		 75      #print "in MTD(f) with node %d, firstGuess %d, depth %d"%(node, f, depth) 
		 76      g=f 
		 77      upperBound=POSINFINITY 
		 78      lowerBound=NEGINFINITY 
		 79      while lowerBound<upperBound: 
		 80          if g==lowerBound: 
		 81              beta = g+1 
		 82          else: 
		 83              beta = g 
		 84          g = alphaBetaWithMemory(node, beta-1, beta, depth, depth) 
		 85          if g< beta: 
		 86              upperBound=g 
		 87          else: 
		 88              lowerBound=g 
		 89      return g 
	*/
	/*public static int mtdf(int hash, int first_guess, int depth) {
		int g = first_guess;
		int beta = 0;
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;
		while(lowerBound < upperBound) {
			if(g == lowerBound) {
				beta = g + 1;
			}
			else {
				beta = g;
			}
			
			g = alphaBetaWithMemory(hash, beta - 1, beta, depth, depth);
			if(g < beta) {
				upperBound = g;
			}
			else {
				lowerBound = g;
			}
		}
		return g;
	}
	*/
	/*
	def iterativeDeepening(node): 
		144      #print "in iterative deepening of node", node 
		145      firstGuess=0 
		146      for d in range(1,12): 
		147          firstGuess=mtdf(node, firstGuess,d) 
		148          #print "first guess after depth %d is %d"%(d,firstGuess) 
		149          #print "I would check the time here" 
		150      return firstGuess 
	
	public static int iterativeDeepening(int hash) {
		int first_guess = 0;
		
		for(int d = 1; d <= 12; d ++) {
			first_guess = mtdf(hash, first_guess, d);
			//if timeout reached break;
		}
		
		return first_guess;
	}
	*/
}

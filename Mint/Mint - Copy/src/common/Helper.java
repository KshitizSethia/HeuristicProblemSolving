package common;

public class Helper {

	private Helper() {
		// prevent instantiation
	}

	public static final int NUM_DENOMINATIONS = 7;
	public static final int PENCES_IN_POUND = 240;
	public static final int INF = 10000;// max can be if all denominations are
										// 1
	public static final int MULT_OF = 5;

	// FIXME: always Ctrl+Shift+f this file before compilation
	// using conditional compilation to build two jars, one for exact and one
	// for exchange number
	public static final boolean USE_EXACT = true;

	// use conditional compilation to remove parts which would slow down code
	// during competition
	public static final boolean DEBUG_MODE = true;
}

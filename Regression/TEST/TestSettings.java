package TEST;


/*
	Class contains parametrs, needed to run tests.
*/
public class TestSettings{
	// dla wielomianow - 1e-4, dla independent 1e-6
	public static double PRECISION = 1e-6;			// precision of comparing values, using Assert methods
	public static int S = 1;						// the quantity of testing functions
	public static int N = 100;						// the maximum mat
	public static int M = 10;						// the number of testing functions
	public static double MAX = 1;					// the maximum coefficient values
}
package Tests;

/*
	Class contains parametrs, needed to run tests.
*/
public class TestSettings{
	public static double PRECISION = 1e-5;			// precision of comparing values, using Assert methods


	public static double START = -0.95;				// the smalest allowed value of a
	public static double STOP = 1.;					// the bigger allowed value of a

	public static double STEP = 0.25;				// the step of a and b values iteration
	public static double RANGE = 0.25;				// the diffrence between b and a

	public static int SIZE = 20;
}
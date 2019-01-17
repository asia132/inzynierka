import least_squares_fitting.*;
import TEST.*;

public class Main{

	public static void main(String[] args){
		RegressionTest.runLinear();
		RegressionTest.runMultiParameter();
		RegressionTest.runRegularized();
		RegressionTest.runRobustLinear();
	}
	
}
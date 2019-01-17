import least_squares_fitting.*;
import TEST.*;

public class RegressionTest{

	public static void runLinear(){
		LinearRegressionTEST linearTest = new LinearRegressionTEST();
		linearTest.testReg();
		linearTest.testWeigthReg();
		linearTest.testEst();
	}
	public static void runMultiParameter(){
		try{
			MultiParameterRegressionTEST multiParameterTest = new MultiParameterRegressionTEST();
			System.out.println("Without Weigth:");
			multiParameterTest.testReg();
			System.out.println("\nWith Weigth:");
			multiParameterTest.testRegWeigth();
			System.out.println("\nEST values:");
			multiParameterTest.testEst();
			System.out.println("\nEST F values:");
			multiParameterTest.testFunctions();
		}catch(GSLErrors gslError){
			System.out.println(gslError.getErrorMessage());
		}
	}
	public static void runRegularized(){
		try{
			RegularizedLinearRegressionTEST regularizedTest = new RegularizedLinearRegressionTEST();
			System.out.println("\n\nCoefficient reg:");
			regularizedTest.testUnreg();
			System.out.println("\n\nCoefficient unreg:");
			regularizedTest.testReg();
			System.out.println("\n\nEST values:");
			regularizedTest.testEst();
			System.out.println("\n\nEST F values:");
			regularizedTest.testFunctions();
		}catch(GSLErrors gslError){
			System.out.println(gslError.getErrorMessage());
		}
	}

	public static void runRobustLinear(){
		try{
			RobustLinearRegressionTEST robustTest = new RobustLinearRegressionTEST();
			System.out.println("\nCoefficient:");
			robustTest.testReg();
			System.out.println("\nEST values:");
			robustTest.testEst();
			System.out.println("\nEST F values:");
			robustTest.testFunctions();
		}catch(GSLErrors gslError){
			System.out.println(gslError.getErrorMessage());
		}
	}
}
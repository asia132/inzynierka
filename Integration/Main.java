import Tests.*;
import Integration.*;
import java.util.function.*;
import junit.framework.Assert;
import java.util.Random;


public class Main{

	public static void main(String[] args) {

		UnitTest test = new UnitTest();

		test.testQNG();				
		test.testQAG();				
		test.testQAGS();			
		test.testCQUAD();			

		test.testSineQAWO();		
		test.testCosineQAWO();		
		
		test.testQAGP();			
		test.testQAWC();			

		test.testQAGI();			
		test.testQAGIU();			
		test.testQAGIL();			
		
		test.testQAWS();			

		test.testCosineQAWF();	
		test.testSineQAWF();	

	}

}

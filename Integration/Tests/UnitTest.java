package Tests;

import Integration.*;
import java.util.function.*;
import junit.framework.Assert;
import java.util.Random;


public class UnitTest{
	private FunctionArray testFunctions;

	public UnitTest(){
		testFunctions = new FunctionArray();
	}
// QNG | QAG | QAGS | CQUAD
	public void testQNG(){		
		// wypełnienie tablicy funkcji
		testFunctions.fillQNG();
		// iteracja po tablicy funkcji
		for (int k = 0; k < testFunctions.N; ++k){
			// dostosowanie przedziału do dziedziny funkcji
			double start = k > 6 ? 0.05 : TestSettings.START;
			// iteracja zmieniająca początek przedziału
			for (double a = start; a < TestSettings.STOP - TestSettings.RANGE; a += TestSettings.STEP){	
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.RANGE; b < TestSettings.STOP; b += TestSettings.STEP){
					try{
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationQNG();
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
							System.out.println("QNG:: " + testFunctions.getFunName(k) + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
						}catch(AssertionError error){
							System.out.println("QNG:: The integration result are not the same (" + testFunctions.getFunName(k) + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){
						System.out.println(exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println("QNG:: " + exeption.getErrorMessage() + " (" + testFunctions.getFunName(k) + ", " + a + ", " + b + "). ");
					}
				}
			}
		}
	}

	public void testQAG(){		
		// wypełnienie tablicy funkcji
		testFunctions.fillQNG();
		// zdefiniowanie tablicy kluczy
		int [] keys = {15, 21, 31, 41, 51, 61};
		for (int k = 0; k < FunctionArray.N; ++k){
			// dostosowanie przedziału do dziedziny funkcji
			double start = k > 6 ? 0.05 : TestSettings.START;
			// iteracja zmieniająca początek przedziału
			for (double a = start; a < TestSettings.STOP; a += TestSettings.STEP){
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.RANGE; b < TestSettings.STOP + TestSettings.RANGE; b += TestSettings.STEP){
					// iteracja po kluczach, przekazywanych do calcIntegrationQAG
					for (int key : keys){
						try{
							// inicjalizacja testowanego obiektu
							NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
							// obliczenie oczekiwanego wyniku
							double excepted = testFunctions.getIntegalsAB(k, a, b);
							// wywołanie testowanej metody
							double actual = integration.calcIntegrationQAG(key);
							try{
								// porównanie wyników
								Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
								System.out.println("QAG:: " + testFunctions.getFunName(k) + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
							}catch(AssertionError error){;
								System.out.println("QAG:: The integration result are not the same (" + k + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
							}
						// złapanie rzucanych wyjątków
						}catch(WrongValue exeption){
							System.out.println(exeption.getErrorMessage());
						}catch(GSLErrors exeption){
							System.out.println("QAG:: " + exeption.getErrorMessage() + "(" + k + ", " + a + ", " + b + "). ");
						}
					}
				}
			}
		}
	}

	public void testQAGS(){		
		// wypełnienie tablicy funkcji
		testFunctions.fillQNG();
		for (int k = 0; k < FunctionArray.N; ++k){			// function iteration
			// dostosowanie przedziału do dziedziny funkcji
			double start = k > 6 ? 0.05 : TestSettings.START;
			// iteracja zmieniająca początek przedziału
			for (double a = start; a < TestSettings.STOP; a += TestSettings.STEP){
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.RANGE; b < TestSettings.STOP + TestSettings.RANGE; b += TestSettings.STEP){
					try{
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationQAGS();
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
							System.out.println("QAGS:: " + testFunctions.getFunName(k) + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
						}catch(AssertionError error){
							System.out.println("QAGS:: The integration result are not the same (" + k + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){
						System.out.println(exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println("QAGS:: " + exeption.getErrorMessage() + "(" + k + ", " + a + ", " + b + "). ");
					}
				}
			}
		}
	}

	public void testCQUAD(){		
		// wypełnienie tablicy funkcji
		testFunctions.fillQNG();
		// iteracja po tablicy funkcji
		for (int k = 0; k < testFunctions.N; ++k){			// function iteration
			// dostosowanie przedziału do dziedziny funkcji
			double start = k > 6 ? 0.05 : TestSettings.START;
			// iteracja zmieniająca początek przedziału
			for (double a = start; a < TestSettings.STOP - TestSettings.RANGE; a += TestSettings.STEP){
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.RANGE; b < TestSettings.STOP; b += TestSettings.STEP){
					try{
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationCQUAD();
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
						}catch(AssertionError error){
							System.out.println("CQUAD:: The integration result are not the same (" + testFunctions.getFunName(k) + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){
						System.out.println(exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println("CQUAD:: " + exeption.getErrorMessage() + " (" + testFunctions.getFunName(k) + ", " + a + ", " + b + "). ");
					}
				}
			}
		}
	}
// QAGP | QAWC
	public void testQAGP(){		
		// wypełnienie tablicy funkcji
		testFunctions.fillQNG();
		// iteracja po tablicy funkcji
		for (int k = 0; k < 3; ++k){
			// iteracja zmieniająca początek przedziału
			for (double a = TestSettings.START; a < TestSettings.STOP; a += TestSettings.STEP){
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.STEP; b < TestSettings.STOP + TestSettings.RANGE; b += TestSettings.STEP){
					try{
						// zapisanie przedziału całkowania do tablicy
						double [] points = {a, b};
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationQAGP(points);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
						}catch(AssertionError error){
							System.out.println("QAWP:: The integration result are not the same." + testFunctions.getFunName(k) + ": [" + a + ", " + b + "], excepted: " + excepted + ", actual: " + actual + ").");
						}
					// złapanie rzucanych wyjątków
							}catch(WrongValue exeption){
						System.out.println("QAGP:: " + exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println("QAWP:: error: " + exeption.getErrorMessage() + "(fun: " + testFunctions.getFunName(k) + ", a: " + a + ", b: " + b + ", singularities:" + testFunctions.getPoints(k)[0] + ", " + testFunctions.getPoints(k)[1] + "). ");
					}
				}
			}
		}
	}

	public void testQAWC(){
		Random r = new Random();
		// iteracja po tablicy funkcji
		for (int k = 0; k < 3; ++k){
			// iteracja zmieniająca początek przedziału
			for (double a = TestSettings.START; a < TestSettings.STOP; a += TestSettings.STEP){
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.RANGE; b < TestSettings.STOP + TestSettings.RANGE; b += TestSettings.STEP){
					// wylosowanie punktu osobliwego
					double point = r.nextDouble() * (b - a) + a;
					// wypełnienie tablicy funkcji
					testFunctions.fillQAWC(point);
					try{
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationQAWC(point);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
						}catch(AssertionError error){
							System.out.println("QAWC:: The integration result are not the same." + testFunctions.getFunName(k) + ": [" + a + ", " + b + "], excepted: " + excepted + ", actual: " + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){;
						System.out.println("QAWC:: " + exeption.getErrorMessage() + "(" + testFunctions.getFunName(k) + ", " + a + ", " + b + ", " + point + "). ");
					}catch(GSLErrors exeption){;
						System.out.println("QAWC:: error: " + exeption.getErrorMessage() + "(fun: " + testFunctions.getFunName(k) + ", a: " + a + ", b: " + b + ", singularity:" + point + "). ");
					}
				}
			}
		}
	}
// QAGI | QAGIL | QAGIU	
	public void testQAGI(){
		// wypełnienie tablicy funkcji
		testFunctions.fillQAGI();
		// iteracja po tablicy funkcji
		for (int k = 0; k < FunctionArray.N; ++k){
			try{
				NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), 0, 1);
				// wywołanie testowanej metody
				double actual = integration.calcIntegrationQAGI();
				// pobranie oczekiwanego wyniku
				double excepted = testFunctions.getResult(k);
				try{
					// porównanie wyników
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
					System.out.println("QAGI:: " + testFunctions.getFunName(k) + " : " + excepted + ", " + actual + ".");
				}catch(AssertionError error){;
					System.out.println("QAGI:: The integration result are not the same (" + testFunctions.getFunName(k) + ") : (" + excepted + " : " + actual + ").");
				}
			// złapanie rzucanych wyjątków
			}catch(WrongValue exeption){
				System.out.println(exeption.getErrorMessage());
			}catch(GSLErrors exeption){
				System.out.println(exeption.getErrorMessage());
			}
		}
	}

	public void testQAGIL(){
		// wypełnienie tablicy funkcji
		testFunctions.fillQAGIL();
		// iteracja po tablicy funkcji
		for (int k = 0; k < 5; ++k){
			// iteracja zmieniająca koniec przedziału
			for (double b = TestSettings.START + TestSettings.RANGE; b < TestSettings.STOP; b += TestSettings.STEP){
				try{
					NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), TestSettings.START, b);
					// wywołanie testowanej metody
					double actual = integration.calcIntegrationQAGIL();
					// obliczenie oczekiwanego wyniku
					double excepted = testFunctions.getIntegalsInfB(k, integration.getB());
					try{
						// porównanie wyników
						Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
					}catch(AssertionError error){
						System.out.println("QAGIL:: The integration result are not the same (" 
							+ testFunctions.getFunName(k) + ", " + integration.getB() + ") : (e: " + excepted + " : a:" + actual + ").");
					}
				// złapanie rzucanych wyjątków
				}catch(WrongValue exeption){
					System.out.println("QAGIL:: " + testFunctions.getFunName(k) + ": " + exeption.getErrorMessage());
				}catch(GSLErrors exeption){;
					System.out.println("QAGIL:: " + testFunctions.getFunName(k) + ": " + exeption.getErrorMessage());
				}
			}
		}
	}

	public void testQAGIU(){
		// wypełnienie tablicy funkcji
		testFunctions.fillQAGIU();
		// iteracja po tablicy funkcji
		for (int k = 0; k < 7; ++k){
			// iteracja zmieniająca początek przedziału
			for (double a = TestSettings.START + TestSettings.RANGE; a < TestSettings.STOP; a += TestSettings.STEP){
				try{
					NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, TestSettings.STOP);
					// wywołanie testowanej metody
					double actual = integration.calcIntegrationQAGIU();
						// obliczenie oczekiwanego wyniku
					double excepted = testFunctions.getIntegalsInfA(k, a);
					try{
						// porównanie wyników
						Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
					}catch(AssertionError error){
						System.out.println("QAGIU:: The integration result are not the same (" + k + ":\t" + testFunctions.getFunName(k) + ",\t" + a + ") : (e:\t" + excepted + "\t: b:\t" + actual + ").");
					}
				// złapanie rzucanych wyjątków
				}catch(WrongValue exeption){
					System.out.println("QAGIU:: " + testFunctions.getFunName(k) + ": " + exeption.getErrorMessage());
				}catch(GSLErrors exeption){;
					System.out.println("QAGIU:: " + testFunctions.getFunName(k) + ": " + exeption.getErrorMessage());
				}
			}
		}
	}
// QAWO
	public void testSineQAWO(){
		Random randGenD = new Random();
		// iteracja po tablicy funkcji
		for (int k = 0; k < FunctionArray.N; ++k){
			// iteracja zmieniająca początek przedziału
			for (double a = TestSettings.START; a < TestSettings.STOP; a += TestSettings.STEP){
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.RANGE; b < TestSettings.STOP + TestSettings.RANGE; b += TestSettings.STEP){
					try{
						// wylosowanie współczynnika omega
						double omega = randGenD.nextDouble() + 40;
						// wypełnienie tablicy funkcji
						testFunctions.fillSineQAWO(omega);
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationSineQAWO(omega);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
							System.out.println("SineQAWO:: " + testFunctions.getFunName(k) + ", " + a + ", " + b + " : " + excepted + " : " + actual + ".");
						}catch(AssertionError error){
							System.out.println("SineQAWO:: The integration result are not the same (" + testFunctions.getFunName(k) + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){
						System.out.println(exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println(exeption.getErrorMessage() + "(" + k + ", " + a + ", " + b + "). ");
					}
				}
			}
		}
	}

	public void testCosineQAWO(){
		Random randGenD = new Random();
		// iteracja po tablicy funkcji
		for (int k = 0; k < FunctionArray.N; ++k){
			// iteracja zmieniająca początek przedziału
			for (double a = TestSettings.START; a < TestSettings.STOP; a += TestSettings.STEP){
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.RANGE; b < TestSettings.STOP + TestSettings.RANGE; b += TestSettings.STEP){
					try{
						// wylosowanie współczynnika omega
						double omega = randGenD.nextDouble() + 40;
						// wypełnienie tablicy funkcji
						testFunctions.fillCosineQAWO(omega);
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationCosineQAWO(omega);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
						}catch(AssertionError error){
							System.out.println("CosineQAWO:: The integration result are not the same (" + testFunctions.getFunName(k) + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){
						System.out.println(exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println(exeption.getErrorMessage() + "(" + k + ", " + a + ", " + b + "). ");
					}
				}
			}
		}
	}
// QAWF
	public void testSineQAWF(){
		Random randGenD = new Random();
		// iteracja po tablicy funkcji
		for (int k = 0; k < testFunctions.N; ++k){
			// iteracja zmieniająca początek przedziału
			for (double a = TestSettings.START; a < TestSettings.STOP; a += TestSettings.STEP){
				try{
					// wylosowanie współczynnika omega
					double omega = randGenD.nextDouble() + 40;
					// wypełnienie tablicy funkcji
					testFunctions.fillSineQAWF(omega);
					// inicjalizacja testowanego obiektu
					NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, a + TestSettings.STEP);
					// wywołanie testowanej metody
					double actual = integration.calcIntegrationSineQAWF(omega);
					// obliczenie oczekiwanego wyniku
					double excepted = testFunctions.getIntegalsInfA(k, a);
					try{
						// porównanie wyników
						Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
					}catch(AssertionError error){;
						System.out.println("SineQAWF:: The integration result are not the same (\t" + testFunctions.getFunName(k) + ",\t" + a + ") : (" + excepted + ",\t" + actual + ":\t" + (excepted -  actual) + ").");
					}
				// złapanie rzucanych wyjątków
						}catch(WrongValue exeption){
					System.out.println(exeption.getErrorMessage());
				}catch(GSLErrors exeption){
					System.out.println("SineQAWF:: " + exeption.getErrorMessage() + "(" + testFunctions.getFunName(k) + ", " + a + "). ");
				}
			}
		}
	}
	public void testCosineQAWF(){
		Random randGenD = new Random();
		// iteracja po tablicy funkcji
		for (int k = 0; k < 6; ++k){
			// iteracja zmieniająca początek przedziału
			for (double a = TestSettings.START; a < TestSettings.STOP; a += TestSettings.STEP){
				try{
					// wylosowanie współczynnika omega
					double omega = randGenD.nextDouble() + 40;
					// wypełnienie tablicy funkcji
					testFunctions.fillCosineQAWF(omega);
					// inicjalizacja testowanego obiektu
					NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, a + TestSettings.STEP);
					// wywołanie testowanej metody
					double actual = integration.calcIntegrationCosineQAWF(omega);
					// obliczenie oczekiwanego wyniku
					double excepted = testFunctions.getIntegalsInfA(k, a);
					try{
						// porównanie wyników
						Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
					}catch(AssertionError error){
						System.out.println("CosineQAWF:: The integration result are not the same (" + testFunctions.getFunName(k) + ", a: " + a + ") : (" + excepted + ", " + actual + ": " + (excepted -  actual) + ").");
					}
				// złapanie rzucanych wyjątków
						}catch(WrongValue exeption){
					System.out.println(exeption.getErrorMessage());
				}catch(GSLErrors exeption){
					System.out.println("CosineQAWF:: " + exeption.getErrorMessage() + "(" + testFunctions.getFunName(k) + ", " + a + "). ");
				}
			}
		}
	}
// QAWS functions
	public void testQAWS(){
		// iteracja po tablicy funkcji
		for (int k = 0; k < 5; ++k){
			// iteracja zmieniająca początek przedziału
			for (double a = TestSettings.STEP; a < TestSettings.STOP - TestSettings.RANGE; a += TestSettings.STEP){
				// iteracja zmieniająca koniec przedziału
				for (double b = a + TestSettings.RANGE; b < TestSettings.STOP; b += TestSettings.STEP){
					try{
						// ustalenie postaci funkcji wagowej
						double alpha = 1;
						double beta = 1;
						// wypełnienie tablicy funkcji
						testFunctions.fillQAWS11(a, b);
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationQAWS(alpha, beta);
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
						}catch(AssertionError error){
							System.out.println("QAWS11:: The integration result are not the same (" + testFunctions.getFunName(k) + ", " + a + ", " + b + ") : (" + excepted + " : " + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){
						System.out.println(exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println("QAWS11:: " + exeption.getErrorMessage() + " (" + testFunctions.getFunName(k) + ", " + a + ", " + b + "). ");
					}
					try{
						// ustalenie postaci funkcji wagowej
						double alpha = 1;
						double beta = 0;
						// wypełnienie tablicy funkcji
						testFunctions.fillQAWS10(a, b);
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationQAWS(alpha, beta);
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
						}catch(AssertionError error){
							System.out.println("QAWS10:: The integration result are not the same (\t" + testFunctions.getFunName(k) + ",\t" + a + ",\t" + b + ") : (\t" + excepted + " :\t" + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){
						System.out.println(exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println("QAWS10:: " + exeption.getErrorMessage() + " (" + testFunctions.getFunName(k) + ", " + a + ", " + b + "). ");
					}
					try{
						// ustalenie postaci funkcji wagowej
						double alpha = 0;
						double beta = 1;
						// wypełnienie tablicy funkcji
						testFunctions.fillQAWS01(a, b);
						// inicjalizacja testowanego obiektu
						NumericalIntegration integration = new NumericalIntegration(testFunctions.getFunObj(k), a, b);
						// obliczenie oczekiwanego wyniku
						double excepted = testFunctions.getIntegalsAB(k, a, b);
						// wywołanie testowanej metody
						double actual = integration.calcIntegrationQAWS(alpha, beta);
						try{
							// porównanie wyników
							Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
						}catch(AssertionError error){
							System.out.println("QAWS01:: The integration result are not the same (" + testFunctions.getFunName(k) + ",\t" + a + ",\t" + b + ") : (\t" + excepted + " :\t" + actual + ").");
						}
					// złapanie rzucanych wyjątków
					}catch(WrongValue exeption){
						System.out.println(exeption.getErrorMessage());
					}catch(GSLErrors exeption){
						System.out.println("QAWS01:: " + exeption.getErrorMessage() + " (" + testFunctions.getFunName(k) + ", " + a + ", " + b + "). ");
					}
				}
			}
		}
	}
}
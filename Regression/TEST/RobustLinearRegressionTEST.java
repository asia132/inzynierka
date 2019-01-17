package TEST;

import least_squares_fitting.*;
import java.util.Random;
import junit.framework.Assert;


class RobRegDatas{
	double [][] x;
	double [] y;
	double [] c;
	int n, m;

	RobRegDatas(int n, int m){
		this.m = m;
		this.n = n;
		this.x = new double [n][m];
		this.y = new double [n];
		this.c = new double [m];
	}
}
// wspolczynniki liczone przez gsl nie musza byc koniecznie rowne wspolczynnikom rzeczywistym.
// ale wynik funkcji jest ten sam (see testFunctions())
public class RobustLinearRegressionTEST{
// ------- Components ---------
	RobRegDatas [] regDatas;
	RobustLinearRegression [] reg;
// ------- Constructors -------
	public RobustLinearRegressionTEST(){
		// nadanie tablicom rozmiarów
		reg = new RobustLinearRegression[TestSettings.S];
		regDatas = new RobRegDatas[TestSettings.S];
		Random r = new Random();
		for (int k = 0; k < TestSettings.S; ++k) {
			// wylosowanie wielkości zbioru danych do regresji
			int m = (int)(r.nextDouble() * (TestSettings.M - 1) + 1);
			int n = (int)(r.nextDouble() * (TestSettings.N - m) + m);

			regDatas[k] = new RobRegDatas(n, m);		
			// wypełnienie tablicy x
			for (int i = 0; i < regDatas[k].n; ++i){
					regDatas[k].x[i][0] = 1;	
				for (int j = 1; j < regDatas[k].m; ++j)
					regDatas[k].x[i][j] = i * 1. + j;
			}		
			// wylosowanie współczynników funkcji	
			for (int j = 0; j < regDatas[k].m; ++j)
				regDatas[k].c[j] = r.nextDouble() * TestSettings.MAX;
			// wypełnienie tablicy y przy pomocy f
			for (int i = 0; i < regDatas[k].n; ++i)
				regDatas[k].y[i] = f(regDatas[k].x[i], k);
			// dodanie elementu do tablicy typu RobustLinearRegression []
			reg[k] = new RobustLinearRegression (regDatas[k].x, regDatas[k].y);
			
		}		
	}
// ------- Original Function --
	public double f(double [] x, int k){
		double result = 0;
		for (int i = 0; i < x.length; ++i) {
			result += x[i] * regDatas[k].c[i];
		}
		return result;
	}
	public double f_gsl(double [] x, int k){
		double result = 0;
		for (int i = 0; i < x.length; ++i) {
			result += x[i] * reg[k].getC()[i];
		}
		return result;
	}
// ------- Regresion Test -----
	public void testReg() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i){
			// wywołanie testowanej metody
			reg[i].robustLinear();
			for (int j = 0; j < reg[i].getC().length; ++j){
				// pobranie wartości oczekiwanych z tablicy regDatas
				double excepted = regDatas[i].c[j];
				// pobranie wartości obliczonych przez testowaną metodę
				double actual = reg[i].getC()[j];
				// porównianie wartości współczynników
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){
					System.out.println("RobustLinearRegression:: The C result are not the same for x^" + j + ". Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
		System.out.println("MultiParameter Regression test - done.");
	}
// ------- Estimation Test ----
	public void testEst() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i) {
		// wywołanie metody obliczającej współczynniki
			reg[i].robustLinear();
			for (int j = 0; j < regDatas[i].n; ++j) {
				// obliczenie wyniku przez oryginalną metodę
				double excepted = f(regDatas[i].x[j], i);
				// obliczenie wyniku funkcji przez metodę klasy RobustLinearRegression
				double actual = reg[i].robustLinearFunction(regDatas[i].x[j]);
				// porównanie wartości
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){;
					System.out.println("RobustLinearRegression:: The fitting result are not the same.");
					System.out.println("Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
	}
	public void testFunctions() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i) {
			// wywołanie metody obliczającej współczynniki
			reg[i].robustLinear();
			for (int j = 0; j < regDatas[i].n; ++j) {
				// obliczenie wyniku przy pomocy oryginalnych współczynników
				double excepted = f(regDatas[i].x[j], i);
				// obliczenie wyniku przy pomocy współczynników obliczonych przez robustLinear()
				double actual = f_gsl(regDatas[i].x[j], i);
				// porównanie wartości
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){;
					System.out.println("RobustLinearRegression:: The fitting functions does not give the same result.");
					System.out.println("Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
	}
}

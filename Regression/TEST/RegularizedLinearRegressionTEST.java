package TEST;

import least_squares_fitting.*;
import java.util.Random;
import junit.framework.Assert;


class RegularizedRegDatas{
	double [][] x;
	double [] y;
	double [] c;
	int n, m;

	RegularizedRegDatas(int n, int m){
		this.m = m;
		this.n = n;
		this.x = new double [n][m];
		this.y = new double [n];
		this.c = new double [m];
	}
}
// wspolczynniki liczone przez gsl nie musza byc koniecznie rowne wspolczynnikom rzeczywistym.
// ale wynik funkcji jest ten sam (see testFunctions())
public class RegularizedLinearRegressionTEST{
// ------- Components ---------
	RegularizedRegDatas [] regDatas;
	RegularizedLinearRegression [] reg;
// ------- Constructors -------
	public RegularizedLinearRegressionTEST(){
		// nadanie tablicom rozmiarów
		reg = new RegularizedLinearRegression[TestSettings.S];
		regDatas = new RegularizedRegDatas[TestSettings.S];
		Random r = new Random();
		for (int k = 0; k < TestSettings.S; ++k) {
			// wylosowanie wielkości zbioru danych do regresji
			int m = 5;//(int)(r.nextDouble() * (30 - 10) + 10);
			int n = (int)(r.nextDouble() * (TestSettings.N - m) + m);

			regDatas[k] = new RegularizedRegDatas(n, m);	
			// wypełnienie tablicy x
			for (int i = 0; i < regDatas[k].n; ++i){
					regDatas[k].x[i][0] = 1.;	
				for (int j = 1; j < regDatas[k].m; ++j)
					regDatas[k].x[i][j] = i * 1. + j;
			}		
			// wylosowanie współczynników funkcji	
			for (int j = 0; j < regDatas[k].m; ++j)
				regDatas[k].c[j] = r.nextDouble() * TestSettings.MAX;
			// wypełnienie tablicy y przy pomocy f
			for (int i = 0; i < regDatas[k].n; ++i)
				regDatas[k].y[i] = f(regDatas[k].x[i], k);
			// dodanie elementu do tablicy typu RegularizedLinearRegression []
			reg[k] = new RegularizedLinearRegression (regDatas[k].x, regDatas[k].y, r.nextDouble());
			
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
	public void testUnreg() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i){
			// wywołanie testowanej metody
			reg[i].regularizedLinear();
			for (int j = 0; j < reg[i].getC().length; ++j){
				// pobranie wartości oczekiwanych z tablicy regDatas
				double excepted = regDatas[i].c[j];
				// pobranie wartości obliczonych przez testowaną metodę
				double actual = reg[i].getC()[j];
				// porównianie wartości współczynników
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){
					System.out.println("RegularizedRegression:: The C result are not the same for x^" + j + ". Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
		System.out.println("Regularized Regression test - done.");
	}
	public void testReg() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i){
			// wywołanie testowanej metody
			reg[i].unregularizedLinear();
			for (int j = 0; j < reg[i].getC().length; ++j){
				// pobranie wartości oczekiwanych z tablicy regDatas
				double excepted = regDatas[i].c[j];
				// pobranie wartości obliczonych przez testowaną metodę
				double actual = reg[i].getC()[j];
				// porównianie wartości współczynników
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){
					System.out.println("RegularizedRegression:: The C result are not the same for x^" + j + ". Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
		System.out.println("Regularized Regression test - done.");
	}
// ------- Estimation Test ----
	public void testEst() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i) {
			// wywołanie metody obliczającej współczynniki
			reg[i].regularizedLinear();
			for (int j = 0; j < regDatas[i].n; ++j) {
				// obliczenie wyniku przez oryginalną metodę
				double excepted = f(regDatas[i].x[j], i);
				// obliczenie wyniku funkcji przez metodę klasy MultiParameterRegression
				double actual = reg[i].regularizedLinearFunction(regDatas[i].x[j]);
				// porównanie wartości
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
					System.out.print("FUN:: X: [");
					for (double var: regDatas[i].x[j])	System.out.print(" ," + var);
					System.out.println("] Excepted: " + excepted + ", actual: " + actual);
				}catch(AssertionError error){;
					System.out.println("RegularizedRegression:: The fitting result are not the same.");
					System.out.println("Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
	}
	public void testFunctions() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i) {
			// wywołanie metody obliczającej współczynniki
			reg[i].regularizedLinear();
			for (int j = 0; j < regDatas[i].n; ++j) {
				// obliczenie wyniku przy pomocy oryginalnych współczynników
				double excepted = f(regDatas[i].x[j], i);
				// obliczenie wyniku przy pomocy współczynników obliczonych przez regularizedLinear()
				double actual = f_gsl(regDatas[i].x[j], i);
				// porównanie wartości
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
					System.out.println("FUN:: X: " + j + " Excepted: " + excepted + ", actual: " + actual);
				}catch(AssertionError error){;
					System.out.println("RegularizedRegression:: The fitting functions does not give the same result.");
					System.out.println("Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
	}
}

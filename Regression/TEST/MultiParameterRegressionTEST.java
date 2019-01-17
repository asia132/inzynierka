package TEST;

import least_squares_fitting.*;
import java.util.Random;
import junit.framework.Assert;


class MRegDatas{
	double [][] x;
	double [] y;
	double [] w;
	double [] c;
	int n, m;

	MRegDatas(int n, int m){

		this.m = m;
		this.n = n;
		this.x = new double [n][m];
		this.y = new double [n];
		this.w = new double [n];
		this.c = new double [m];
	}
}
// wspolczynniki liczone przez gsl nie musza byc koniecznie rowne wspolczynnikom rzeczywistym.
// ale wynik funkcji jest ten sam (see testFunctions())
public class MultiParameterRegressionTEST{
// ------- Components ---------
	MRegDatas [] regDatas;
	MultiParameterRegression [] regM;
// ------- Constructors -------
	public MultiParameterRegressionTEST(){
		// nadanie tablicom rozmiarów
		regM = new MultiParameterRegression[TestSettings.S];
		regDatas = new MRegDatas[TestSettings.S];
		Random r = new Random();
		for (int k = 0; k < TestSettings.S; ++k) {
			// wylosowanie wielkości zbioru danych do regresji
			int m = (int)(r.nextDouble() * (TestSettings.M - 1) + 1);
			int n = (int)(r.nextDouble() * (TestSettings.N - m) + m);

			regDatas[k] = new MRegDatas(n, m);		
			// wypełnienie tablicy x
			for (int i = 0; i < regDatas[k].n; ++i){	
				for (int j = 0; j < regDatas[k].m; ++j)
					regDatas[k].x[i][j] = Math.sin(j * i);
					// regDatas[k].x[i][j] = j;
					// regDatas[k].x[i][j] = Math.pow(i,  j);
			}	
			// wylosowanie współczynników funkcji	
			for (int j = 0; j < regDatas[k].m; ++j)
				regDatas[k].c[j] = r.nextDouble() * TestSettings.MAX;
			// wypełnienie tablicy y przy pomocy f
			for (int i = 0; i < regDatas[k].n; ++i)
				regDatas[k].y[i] = f(regDatas[k].x[i], k);
			// obliczenie wag punktów
			double aver = average(regDatas[k].y);
			for (int i = 0; i < regDatas[k].n; ++i) {
				regDatas[k].w[i] = regDatas[k].y[i] == aver ? 1 : 1 / (regDatas[k].y[i] - aver);
			}
			// dodanie elementu do tablicy typu MultiParameterRegression []
			regM[k] = new MultiParameterRegression (regDatas[k].x, regDatas[k].y, regDatas[k].w);
			
		}		
	}
// ------- Original Function --
	public double f(double [] x, int k){
		double result = 0;
		for (int i = 1; i < x.length; ++i) {
			result += x[i] * regDatas[k].c[i];
		}
		return result;
	}
	public double f_gsl(double [] x, int k){
		double result = 0;
		for (int i = 1; i < x.length; ++i) {
			result += x[i] * regM[k].getC()[i];
		}
		return result;
	}
// ------- Calc array average -
	private double average(double [] array){
		double average = 0;
		for (double i : array) average += i;
		average /= array.length;
		return average;
	}
// ------- Regresion Test -----
	public void testReg() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i){	
			// wywołanie testowanej metody
			regM[i].multiFitLinear();
			for (int j = 0; j < regM[i].getC().length; ++j){
				// pobranie wartości oczekiwanych z tablicy regDatas
				double excepted = regDatas[i].c[j];
				// pobranie wartości obliczonych przez testowaną metodę
				double actual = regM[i].getC()[j];
				// porównianie wartości współczynników
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){
					System.out.println("MultiParameterRegression:: The C result are not the same for x^" + j + ". Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
		System.out.println("MultiParameter Regression test - done.");
	}
	public void testRegWeigth() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i){	
			// wywołanie testowanej metody
			regM[i].multiFitWeigthLinear();
			for (int j = 0; j < regM[i].getC().length; ++j){
				// pobranie wartości oczekiwanych z tablicy regDatas
				double excepted = regDatas[i].c[j];
				// pobranie wartości obliczonych przez testowaną metodę
				double actual = regM[i].getC()[j];
				// porównianie wartości współczynników
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){
					System.out.println("MultiParameterRegression with Weigth:: The C result are not the same for x^" + j + ". Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
		System.out.println("MultiParameter Regression with Weigth test - done.");
	}
// ------- Estimation Test ----
	public void testEst() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i) {
			// wywołanie metody obliczającej współczynniki
			regM[i].multiFitLinear();
			for (int j = 0; j < regDatas[i].n; ++j) {
				// obliczenie wyniku przez oryginalną metodę
				double actual = f(regDatas[i].x[j], i);
				// obliczenie wyniku funkcji przez metodę klasy MultiParameterRegression
				double excepted = regM[i].multiFitFunction(regDatas[i].x[j]);
				// porównanie wartości
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){;
					System.out.println("MultiParameterRegression:: The fitting result are not the same.");
					System.out.println("Excepted: " + excepted + ", actual: " + actual + ". Error: " + (excepted - actual));
				}
			}
		}
	}
	public void testFunctions() throws GSLErrors{
		for (int i = 0; i < regDatas.length; ++i) {
			// wywołanie metody obliczającej współczynniki
			regM[i].multiFitLinear();
			for (int j = 0; j < regDatas[i].n; ++j) {
				// obliczenie wyniku przy pomocy oryginalnych współczynników
				double actual = f(regDatas[i].x[j], i);
				// obliczenie wyniku przy pomocy współczynników obliczonych przez regularizedLinear()
				double excepted = f_gsl(regDatas[i].x[j], i);
				// porównanie wartości
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){;
					System.out.println("MultiParameterRegression:: The fitting functions does not give the same result.");
					System.out.println("Excepted: " + excepted + ", actual: " + actual + ". Error: " + (excepted - actual));
				}
			}
		}
	}
}

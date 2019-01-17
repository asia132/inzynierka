package TEST;

import least_squares_fitting.*;
import java.util.Random;
import junit.framework.Assert;

public class LinearRegressionTEST{
// ------- Components ---------
	RegDatas [] regDatas;
	LinearRegression [] l;
// ------- Constructors -------
	public LinearRegressionTEST(){
		// nadanie tablicom rozmiarów
		regDatas = new RegDatas[TestSettings.S];
		l = new LinearRegression[TestSettings.S];
		Random r = new Random();
		for (int i = 0; i < regDatas.length; ++i) {
			l[i] = new LinearRegression();
			// wylosowanie wielkości zbioru danych do regresji
			regDatas[i] = new RegDatas((int)(r.nextDouble() * TestSettings.N + 1));	
			// wylosowanie współczynników funkcji	
			regDatas[i].c0 = r.nextDouble() * TestSettings.MAX;
			regDatas[i].c1 = r.nextDouble() * TestSettings.MAX;
			// obliczenie wartości x i y punktów
			for (int j = 0; j < regDatas[i].s; ++j) {
				regDatas[i].x[j] = j * 1.;
				regDatas[i].y[j] = f(j * 1., i);
			}
			// obliczenie wag punktów
			double aver = average(regDatas[i].y);
			for (int j = 0; j < regDatas[i].s; ++j) {
				regDatas[i].w[j] = regDatas[i].y[j] == aver ? 1 : 1 / (regDatas[i].y[j] - aver);
				// dodanie punktu do tablicy LinearRegression
				l[i].addPoint(regDatas[i].x[j], regDatas[i].y[j], regDatas[i].w[j]);
			}
		}		
	}
// ------- Original Function --
	public double f(double x, int i){
		return regDatas[i].c0 + x * regDatas[i].c1;
	}
// ------- Calc array average -
	private double average(double [] array){
		double average = 0;
		for (double i : array) average += i;
		average /= array.length;
		return average;
	}
// ------- Regresion Test -----
	public void testReg(){
		for (int i = 0; i < regDatas.length; ++i){		
		// wywołanie testowanej metody
			l[i].fitLinear();
		// pobranie wartości oczekiwanych z tablicy regDatas
			double exceptedC0 = regDatas[i].c0;
			double exceptedC1 = regDatas[i].c1;
		// pobranie wartości obliczonych przez testowaną metodę
			double actualC0 = l[i].getC0();
			double actualC1 = l[i].getC1();
		// porównianie wartości współczynnika C0
			try{
				Assert.assertEquals(exceptedC0, actualC0, TestSettings.PRECISION);
			}catch(AssertionError error){
				System.out.println("LinearRegression:: The C0 result are not the same. Excepted: " + exceptedC0 + ", actual: " + actualC0);
			}
		// porównianie wartości współczynnika C1
			try{
				Assert.assertEquals(exceptedC1, actualC1, TestSettings.PRECISION);
			}catch(AssertionError error){
				System.out.println("LinearRegression:: The C1 result are not the same. Excepted: " + exceptedC1 + ", actual: " + actualC1);
			}
		}
		System.out.println("Linear Regression test - done.");
	}
	public void testWeigthReg(){
		for (int i = 0; i < regDatas.length; ++i){		
		// wywołanie testowanej metody
			l[i].fitWeigthLinear();
		// pobranie wartości oczekiwanych z tablicy regDatas
			double exceptedC0 = regDatas[i].c0;
			double exceptedC1 = regDatas[i].c1;
		// pobranie wartości obliczonych przez testowaną metodę
			double actualC0 = l[i].getC0();
			double actualC1 = l[i].getC1();
		// porównianie wartości współczynnika C0
			try{
				Assert.assertEquals(exceptedC0, actualC0, TestSettings.PRECISION);
			}catch(AssertionError error){
				System.out.println("LinearWeigthRegression:: The C0 result are not the same. Excepted: " + exceptedC0 + ", actual: " + actualC0);
			}
		// porównianie wartości współczynnika C1
			try{
				Assert.assertEquals(exceptedC1, actualC1, TestSettings.PRECISION);
			}catch(AssertionError error){
				System.out.println("LinearWeigthRegression:: The C1 result are not the same. Excepted: " + exceptedC1 + ", actual: " + actualC1);
			}
		}
		System.out.println("Linear Regression with Weigth test - done.");
	}
// ------- Estimation Test ----
	public void testEst(){
		for (int i = 0; i < regDatas.length; ++i) {		
		// wywołanie metody obliczającej współczynniki
			l[i].fitWeigthLinear();
			for (int j = 0; j < regDatas[i].s; ++j) {
			// obliczenie wyniku funkcji przez metodę klasy LinearRegression
				double actual = l[i].fitFunction(l[i].getPointX(j));
			// obliczenie wyniku przez oryginalną metodę
				double excepted = f(regDatas[i].x[j], i);
			// porównanie wartości
				try{
					Assert.assertEquals(excepted, actual, TestSettings.PRECISION);
				}catch(AssertionError error){
					System.out.println("LinearRegression:: The fitting result are not the same.");
					System.out.println("F(" + l[i].getPointX(j) + ") = " + l[i].getC0() + "+ " + l[i].getPointX(j) + 
						" * " + l[i].getC1() + ". Excepted: " + excepted + ", actual: " + actual);
				}
			}
		}
		System.out.println("Linear Regression Fitting test - done.");
	}
}

class RegDatas{
	double [] x;
	double [] y;
	double [] w;
	double c0, c1;
	int s;

	RegDatas(int s){

		this.s = s;
		this.x = new double [s];
		this.y = new double [s];
		this.w = new double [s];
	}
}
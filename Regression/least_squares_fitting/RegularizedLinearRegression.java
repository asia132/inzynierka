package least_squares_fitting;

import java.util.ArrayList;
import java.util.List;
 
public class RegularizedLinearRegression{
// -------- Components ---
	double rnorm, snorm;
	double [] c;
	double [][] x;
	double [] y;
	double lambda;
// -------- Constructors -
	public RegularizedLinearRegression(int s){
		this.x = new double[s][3];
		this.y = new double[s];
		this.c = new double[s];
		this.lambda = 0;
	}
	public RegularizedLinearRegression(double [][] x, double [] y, double lambda){
		this.x = x;
		this.y = y;
		this.c = new double[y.length];
		this.lambda = lambda;
	}
// -------- Gets ---------
	public void setX(double [][] x){
		this.x = x;
	}
	public void getY(double [] y){
		this.y = y;
	}
	public void getLambda(double lambda){
		this.lambda = lambda;
	}
// -------- Gets ---------
	public double [][] getX(){
		return this.x;
	}
	public double [] getY(){
		return this.y;
	}
	public double [] getC(){
		return this.c;
	}
	public double getLambda(){
		return this.lambda;
	}
	public double getRnorm(){
		return this.rnorm;
	}
	public double getSnorm(){
		return this.snorm;
	}
// -------- Lsf ----------
	public void regularizedLinear() throws GSLErrors{
		int n = x[0].length;
		double [] xx = new double[x.length * n];
		for (int i = 0, k = 0; i < x.length; i++){
			for (int j = 0; j < n; j++, k++) {
				xx[k] 	  = x[i][j];
			}
		}
		gslRegularizedLinear(xx, n);
	}
	public void unregularizedLinear() throws GSLErrors{
		int n = x[0].length;
		double [] xx = new double[x.length * n];
		for (int i = 0, k = 0; i < x.length; i++){
			for (int j = 0; j < n; j++, k++) {
				xx[k] 	  = x[i][j];
			}
		}
		gslRegularizedLinear(xx, n, lambda);
	}
	public double regularizedLinearFunction(double [] x){
		int n = this.c.length;
		double result = 0.;
		for (int i = 0; i < n; i++){
			result += this.c[i] * x[i];
		}
		return result;
	}
// -------- Native -------
	private native void gslRegularizedLinear(double [] x, int n, double lamda) throws GSLErrors;
	private native void gslRegularizedLinear(double [] x, int n) throws GSLErrors;
// The calling of loadLibrary, needed to native methods working
	static{
		System.loadLibrary("RegularizedLinearRegression");
	}
}
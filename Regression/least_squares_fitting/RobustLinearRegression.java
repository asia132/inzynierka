package least_squares_fitting;

import java.util.ArrayList;
import java.util.List;

public class RobustLinearRegression{
// -------- Components ---
	double y_err;
	double [][] cov;
	double [] c;
	double [][] x;
	double [] y;
// -------- Constructors -
	public RobustLinearRegression(int s){
		this.x = new double[s][3];
		this.y = new double[s];
		this.c = new double[s];
		this.cov = new double [3][3];
	}
	public RobustLinearRegression(double [][] x, double [] y){
		this.x = x;
		this.y = y;
		this.c = new double[y.length];
		this.cov = new double [x[0].length][x[0].length];
	}
// -------- Sets ---------
	public void getX(double [][] x){
		this.x = x;
	}
	public void getY(double [] y){
		this.y = y;
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
	public double [][] getCovArray(){
		return this.cov;
	}
	public double getYErr(){
		return this.y_err;
	}
// -------- Lsf ----------
	public void robustLinear() throws GSLErrors{
		int n = x[0].length;
		double [] xx = new double[x.length * n];
		for (int i = 0, k = 0; i < x.length; i++){
			for (int j = 0; j < n; j++, k++) {
				xx[k] 	  = x[i][j];
			}
		}
		double [] cov_temp = gslRobustLinear(xx, n, 0);
		for (int i = 0, k = 0; i < n; i++){
			for (int j = 0; j < n; j++, k++) {
				cov[i][j] = cov_temp[k];
			}
		}
	}
	public double robustLinearFunction(double [] x) throws GSLErrors{
		int n = this.cov[0].length;
		double [] ccov = new double[this.cov.length * n];
		for (int i = 0, k = 0; i < cov.length; i++){
			for (int j = 0; j < n; j++, k++) {
				ccov[k] 	  = this.cov[i][j];
			}
		}
		return gslRobustLinearEst(x, ccov);
	}
// -------- Native -------
	private native double [] gslRobustLinear(double [] x, int n, int robType) throws GSLErrors;
	private native double gslRobustLinearEst(double [] x, double [] cov) throws GSLErrors;
// The calling of loadLibrary, needed to native methods working
	static{
		System.loadLibrary("RobustLinearRegression");
	}
}
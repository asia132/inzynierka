package least_squares_fitting;

import java.util.ArrayList;
import java.util.List;

public class MultiParameterRegression{
// -------- Components ---
	double chisq;
	double y_err;
	double [][] cov;
	double [] c;
	double [][] x;
	double [] y;
	double [] w;
// -------- Constructors -
	public MultiParameterRegression(int n, int m){
		this.x = new double[n][m];
		this.y = new double[n];
		this.w = new double[n];
		this.c = new double[n];
		this.cov = new double [m][m];
	}
	public MultiParameterRegression(double [][] x, double [] y, double [] w){
		this.x = x;
		this.y = y;
		this.w = w;
		this.c = new double[x[0].length];
		this.cov = new double [x[0].length][x[0].length];
	}
// -------- Sets ---------
	public void setX(double [][] x){
		this.x = x;
	}
	public void setY(double [] y){
		this.y = y;
	}
	public void setW(double [] w){
		this.w = w;
	}
// -------- Gets ---------
	public double [][] getX(){
		return this.x;
	}
	public double [] getY(){
		return this.y;
	}
	public double [] getW(){
		return this.w;
	}
	public double [] getC(){
		return this.c;
	}
	public double [][] getCovArray(){
		return this.cov;
	}
	public double getChisq(){
		return this.chisq;
	}
	public double getYErr(){
		return this.y_err;
	}
// -------- mpf ----------
	public void multiFitLinear() throws GSLErrors{
		int n = x[0].length;
		double [] xx = new double[x.length * n];
		for (int i = 0, k = 0; i < x.length; i++){
			for (int j = 0; j < n; j++, k++) {
				xx[k] 	  = x[i][j];
			}
		}
		double [] cov_temp = gslMultiFitLinear(xx, n);
		for (int i = 0, k = 0; i < n; i++){
			for (int j = 0; j < n; j++, k++) {
				cov[i][j] = cov_temp[k];
			}
		}
	}
	public void multiFitWeigthLinear() throws GSLErrors{

		int n = x[0].length;
		double [] xx = new double[x.length * n];
		for (int i = 0, k = 0; i < x.length; i++){
			for (int j = 0; j < n; j++, k++) {
				xx[k] 	  = x[i][j];
			}
		}
		double [] cov_temp = gslMultiFitLinearW(xx, n);
		for (int i = 0, k = 0; i < n; i++){
			for (int j = 0; j < n; j++, k++) {
				cov[i][j] = cov_temp[k];
			}
		}
	}
	public double multiFitFunction(double [] x) throws GSLErrors{
		int n = this.cov[0].length;
		double [] ccov = new double[this.cov.length * n];
		for (int i = 0, k = 0; i < cov.length; i++){
			for (int j = 0; j < n; j++, k++) {
				ccov[k] 	  = this.cov[i][j];
			}
		}
		return gslMultiFitLinearEst(x, ccov);
	}
// -------- Native -------
	private native double [] gslMultiFitLinearW(double [] y, int n) throws GSLErrors;
	private native double [] gslMultiFitLinear(double [] y, int n) throws GSLErrors;
	private native double gslMultiFitLinearEst(double [] x, double [] cov) throws GSLErrors;
// The calling of loadLibrary, needed to native methods working
	static{
		System.loadLibrary("MultiParameterRegression");
	}
}
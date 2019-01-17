package least_squares_fitting;

import java.util.ArrayList;
import java.util.List;

public class LinearRegression{
// -------- Components ---
	private List<Point> pointList; // w - odwrotnosc wariacji

	private double c0, c1, chisq, yf_err;
	private double [][] cov;
// -------- Constructors -
	public LinearRegression(){
		this.pointList = new ArrayList<Point>();
		this.cov = new double [2][2];
		this.c0 = 0;
		this.c1 = 0;
	}
// -------- Point ----
	public void addPoint(double x, double y, double w){
		this.pointList.add(new Point(x, y, w));
	}
	public void addPoint(Point p){
		this.pointList.add(p);
	}
	public void setPoint(int i, double x, double y, double w){
		this.pointList.set(i, new Point(x, y, w));
	}
	public void removePoint(int i){
		this.pointList.remove(i);
	}
	public double getPointX(int i){
		return this.pointList.get(i).x;
	}
	public double getPointY(int i){
		return this.pointList.get(i).y;
	}
	public double getPointW(int i){
		return this.pointList.get(i).w;
	}
// -------- Gets ---------
	public double getC0(){
		return this.c0;
	}
	public double getC1(){
		return this.c1;
	}
	public double [][] getCovArray(){
		return this.cov;
	}
	public double getChisq(){
		return this.chisq;
	}
	public double getYErr(){
		return this.yf_err;
	}
// -------- Lsf ----------
	public void fitLinear(){
		Point [] pointArray = this.pointList.toArray(new Point[0]);
		double [] y = new double [pointArray.length * 3];

		for (int i = 0; i < pointArray.length; i++) {
			y[i * 3] 	 = pointArray[i].x;
			y[i * 3 + 1] = pointArray[i].y;
			y[i * 3 + 2] = pointArray[i].w;
		}

		double [] cov_temp = gslFitLinear(y);

		cov[0][0] = cov_temp[0];
		cov[0][1] = cov_temp[1];
		cov[1][0] = cov_temp[2];
		cov[1][1] = cov_temp[3];
	}
	public void fitWeigthLinear(){
		Point [] pointArray = this.pointList.toArray(new Point[0]);
		double [] y = new double [pointArray.length * 3];

		for (int i = 0; i < pointArray.length; i++) {
			y[i * 3] 	 = pointArray[i].x;
			y[i * 3 + 1] = pointArray[i].y;
			y[i * 3 + 2] = pointArray[i].w;
		}

		double [] cov_temp = gslFitWeigthLinear(y);

		cov[0][0] = cov_temp[0];
		cov[0][1] = cov_temp[1];
		cov[1][0] = cov_temp[2];
		cov[1][1] = cov_temp[3];
	}
	public double fitFunction(double x){
		return gslFitLinearEst(x, cov[0][0], cov[0][1], cov[1][1]);
	}
// -------- Native -------
	private native double [] gslFitLinear(double [] y);
	private native double [] gslFitWeigthLinear(double [] y);
	private native double gslFitLinearEst(double x, double cov00, double cov01, double cov11);
// The calling of loadLibrary, needed to native methods working
	static{
		System.loadLibrary("LinearRegression");
	}
}
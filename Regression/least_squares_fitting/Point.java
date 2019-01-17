package least_squares_fitting;

public class Point{
	public double x;
	public double y;
	public double w;
	public Point(double x, double y, double w){
		this.x = x;
		this.y = y;
		this.w = w;
	}
	public Point(){
		this.x = 0;
		this.y = 0;
		this.w = 0;
	}
	public String toString(){
		return x + ", " + y + ", " + w;
	}
}
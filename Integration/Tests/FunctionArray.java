package Tests;

import java.util.function.*;
import java.lang.Math.*;

public class FunctionArray{
	private Function [] functions;
	public static int N = 10;
	// public static int n = 7;

	public FunctionArray(){ 
		functions = new Function [N];
		for (int i = 0; i < N; ++i) {
			functions[i] = new Function();
		}
	}
// QNG 
	public void fillQNG(){
		functions[0].setFunctionDatas(x -> Math.sin(x), 
			y -> -Math.cos(y),
			"sin");
		functions[1].setFunctionDatas(x -> Math.cos(x), 
			x -> Math.sin(x), 
			"cos");
		functions[2].setFunctionDatas(x -> 1 / (Math.cos(x)*Math.cos(x)), 
			x -> Math.tan(x),
			"1 / (cos(x)^2)");
		functions[3].setFunctionDatas(x -> 1 / (x * x + 1),
			x -> Math.atan(x), 
			"1 / (x^2 + 1)");
		functions[4].setFunctionDatas(x -> x * x,
			x -> x * x * x / 3,
			"x^2");
		functions[5].setFunctionDatas(x -> 4 * x * x * x + 3 * x * x + 3,
			x -> x * x * x * x + x * x * x + 3 * x,
			"4x^3 + 3x^2 + 3");
		functions[6].setFunctionDatas(x -> Math.pow(5, x),
			x -> Math.pow(5, x) / Math.log(5), 
			"5^x");
		functions[7].setFunctionDatas(x -> Math.sin(x) * Math.sin(x),
			x -> 0.5 * (x - Math.sin(x) * Math.cos(x)),
			"sin^2");
		functions[8].setFunctionDatas(x -> Math.log(x),	
			x -> x * (Math.log(x) - 1),
			"ln");
		functions[9].setFunctionDatas(x -> Math.sqrt(x),
			x -> Math.pow(x, 1.5) * 2 / 3,
			"sqrt");
	}
// QAWO
	public void fillSineQAWO(double a){
		functions[0].setFunctionDatas(x -> Math.sin(x), 
			x -> (Math.cos(x) * Math.sin(a * x) - a * Math.sin(x) * Math.cos(a * x)) / (a * a - 1),
			"sin");
		functions[1].setFunctionDatas(x -> Math.cos(x), 
			x -> (Math.sin(x) * Math.sin(a * x) + a * Math.cos(x) * Math.cos(a * x)) / (1 - a * a), 
			"cos");
		functions[2].setFunctionDatas(x -> x * x, 
			x -> ((2 - a * a * x * x) * Math.cos(a * x) + 2 * a * x * Math.sin(a * x)) / (a * a * a),
			"x^2");
		functions[3].setFunctionDatas(x -> 4 * x * x * x + 3 * x * x + 3,
			x -> (6 * (a * a * x * (2 * x + 1) - 4) * Math.sin(a * x) - a * (a * a * ( 4 * Math.pow(x, 3) + 3 * x * x + 3) - 6 * (4 * x + 1)) * Math.cos(a * x)) / Math.pow(a, 4), 
			"4x^3 + 3x^2 + 3");
		functions[4].setFunctionDatas(x -> Math.pow(5, x),
			x -> Math.pow(5, x) * (Math.log(5) * Math.sin(a * x) - a * Math.cos(a * x)) / ( a * a + Math.log(5) * Math.log(5)),
			"5^x");
		functions[5].setFunctionDatas(x -> Math.sin(x) * Math.sin(x),
			x -> 0.25 * (Math.cos(a * x - 2 * x) / (a - 2) - 2 * Math.cos(a * x) / a + Math.cos(a * x + 2 * x) / (a + 2)),
			"sin^2");
		functions[6].setFunctionDatas(x -> 3 * Math.cos(x) * x + x * x,
			x -> 2 * x * Math.sin(a * x) / a / a
					- (a * a * x * x - 2) * Math.cos(a * x) / a / a / a
					+ 1.5 * (Math.sin(a * x - x) / (a - 1) / (a - 1) - x * Math.cos(a * x - x) / (a - 1))
					+ 1.5 * (Math.sin(a * x + x) / (a + 1) / (a + 1) - x * Math.cos(a * x + x) / (a + 1)), 
			"xcos(x) + x^2");
		functions[7].setFunctionDatas(x -> Math.pow(4, 2 * x),
			x -> Math.pow(16, x) * (Math.log(16) * Math.sin(a * x) - a * Math.cos(a * x)) / (a * a + Math.log(16) * Math.log(16)),
			"4^2x");
		functions[8].setFunctionDatas(x -> 9 * x + Math.pow(Math.cos(x), 4),	
			x -> 9 * Math.sin(a * x) / a / a - Math.cos(a * x - 4 * x) / 16 / (a - 4) - Math.cos(a * x - 2 * x) / 4 / (a - 2)
					- Math.cos(a * x) * 9 * x / a - Math.cos(a * x) * 3 / 8 / a	- Math.cos(a * x + 2 * x) / 4 / (a + 2)	- Math.cos(a * x + 4 * x) / 16 / (a + 4),
			"9x + cos(x)^4");
		functions[9].setFunctionDatas(x -> Math.sin(x) + Math.cos(x),
			x -> (Math.cos(x) * (Math.sin(a * x) - a * Math.cos(a * x)) - Math.sin(x) * (Math.sin(a * x) + a * Math.cos(a * x))) / (a * a - 1),
			"sinx + cosx");
	}

	public void fillCosineQAWO(double a){
		functions[0].setFunctionDatas(x -> Math.sin(x), 
			x -> (a * Math.sin(x) * Math.sin(a * x) + Math.cos(x) * Math.cos(a * x)) / (a * a - 1),
			"sin");
		functions[1].setFunctionDatas(x -> Math.cos(x), 
			x -> (a * Math.cos(x) * Math.sin(a * x) - Math.sin(x) * Math.cos(a * x)) / (a * a - 1), 
			"cos");
		functions[2].setFunctionDatas(x -> x * x, 
			x -> ((a * a * x * x - 2) * Math.sin(a * x) + 2 * a * x * Math.cos(a * x)) / (a * a * a),
			"x^2");
		functions[3].setFunctionDatas(x -> 4 * x * x * x + 3 * x * x + 3,
			x -> (a * (a * a * ( 4 * Math.pow(x, 3) + 3 * x * x + 3) - 6 * (4 * x + 1)) * Math.sin(a * x)
				+ 6 * (a * a * x * (2 * x + 1) - 4) * Math.cos(a * x))/ Math.pow(a, 4),
			"4x^3 + 3x^2 + 3");
		functions[4].setFunctionDatas(x -> Math.pow(5, x),
			x -> Math.pow(5, x) * ( Math.log(5) * Math.cos(a * x) + a * Math.sin(a * x) ) / ( a * a + Math.log(5) * Math.log(5)),
			"5^x");
		functions[5].setFunctionDatas(x -> Math.sin(x) * Math.sin(x),
			x -> 0.25 * (- Math.sin(a * x - 2 * x) / (a - 2) + 2 * Math.sin(a * x) / a - Math.sin(a * x + 2 * x) / (a + 2)),
			"sin^2");
		functions[6].setFunctionDatas(x -> 3 * Math.cos(x) * x + x * x,
			x -> 2 * x * Math.cos(a * x) / a / a + (a * a * x * x - 2) * Math.sin(a * x) / a / a / a
				+ 1.5 * (x * Math.sin(a * x - x) / (a - 1) + Math.cos(a * x - x) / (a - 1) / (a - 1))
				+ 1.5 * (x * Math.sin(a * x + x) / (a + 1) + Math.cos(a * x + x) / (a + 1) / (a + 1)), 
			"xcos(x) + x^2");
		functions[7].setFunctionDatas(x -> Math.pow(4, 2 * x),
			x -> Math.pow(16, x) * (Math.log(16) * Math.cos(a * x) + a * Math.sin(a * x)) / (a * a + Math.log(16) * Math.log(16)),
			"4^2x");
		functions[8].setFunctionDatas(x -> 9 * x + Math.pow(Math.cos(x), 4),	
			x -> 9 * Math.cos(a * x) / a / a + Math.sin(a * x - 4 * x) / 16 / (a - 4)
				+ Math.sin(a * x - 2 * x) / 4 / (a - 2) + Math.sin(a * x) * 9 * x / a
				+ Math.sin(a * x) * 3 / 8 / a + Math.sin(a * x + 2 * x) / 4 / (a + 2)
				+ Math.sin(a * x + 4 * x) / 16 / (a + 4),
			"9x + cos(x)^4");
		functions[9].setFunctionDatas(x -> Math.sin(x) + Math.cos(x),
			x -> (Math.sin(x) * (a * Math.sin(a * x) - Math.cos(a * x)) + Math.cos(x) * (a * Math.sin(a * x) + Math.cos(a * x))) / (a * a - 1),
			"sinx + cosx");
	}
// QAGI
	public void fillQAGI(){
		functions[0].setFunctionDatas(x -> Math.sin(x), 
			0,
			"sin(x)");
		functions[1].setFunctionDatas(x -> 1/x, 
			0, 
			"1/x");
		functions[2].setFunctionDatas(x -> 1 / (x * x + 1), 
			Math.PI,
			"1 / (x^2 + 1)");
		functions[3].setFunctionDatas(x -> -2 / (x*x + 1),
			-Math.PI * 2,
			"-2 / (x^2 + 1)");
		functions[4].setFunctionDatas(x -> Math.exp(2 * x - x * x),
			Math.E * Math.sqrt(Math.PI), 
			"exp(2x - x^2)");
		functions[5].setFunctionDatas(x -> Math.exp(x*x - (1 - x)*(1 - x) - (-1 - x)*(-1 - x)),
			Math.sqrt(Math.PI) / Math.E / Math.E,
			"exp(x^2 - (1 - x)^2 - (-1 - x)^2)");
		functions[6].setFunctionDatas(x -> Math.exp(-x*x/4),
			2 * Math.sqrt(Math.PI), 
			"exp(x^2/4)");
		functions[7].setFunctionDatas(x -> Math.exp(x*x - (4 - x)*(4 - x) - (2 - x)*(2 - x)),
			Math.exp(16) * Math.sqrt(Math.PI),
			"exp(x^2 - (4 - x)^2 - (2 - x)^2)");
		functions[8].setFunctionDatas(x -> Math.exp(x*x - (3 - x)*(3 - x) - (2 - x)*(2 - x)),	
			Math.exp(12) * Math.sqrt(Math.PI),
			"exp(x^2 - (3 - x)^2 - (2 - x)^2)");
		functions[9].setFunctionDatas(x -> Math.exp(6 * x - 3 * x * x),
			Math.sqrt(Math.PI/3) * Math.exp(3),
			"exp(6x - 3x^2)");
	}

	public void fillQAGIU(){
		functions[0].setFunctionDatas(x -> x / (Math.pow(x, 4) + 1),	
			x -> 0.5 * Math.atan(x*x),
			Math.PI / 4,
			"x / (x^4 +1)");	
		functions[1].setFunctionDatas(x -> 1 / (x * x + 1), 
			x -> Math.atan(x),
			Math.PI / 2,
			"1 / (x^2 + 1)");
		functions[2].setFunctionDatas(x -> -2 / (x * x + 1), 
			x -> - 2 * Math.atan(x),
			-Math.PI,
			"-2 / (x^2 + 1)");
		functions[3].setFunctionDatas(x -> 5 /(x*x + 6*x + 9), 
			x -> -5 / (x + 3),
			0,
			"5 /(x*x + 6*x + 9)");
		functions[4].setFunctionDatas(x -> Math.atan(x)*Math.atan(x)/(1 + x*x), 
			x->Math.atan(x) *Math.atan(x) *Math.atan(x)/3,
			Math.PI*Math.PI*Math.PI/24,
			"atan(x)^2 / (1 + x^2)");
		functions[5].setFunctionDatas(x -> 2*x / (x*x*x + 1), 
			x->2 * (Math.log(x * x - x + 1) / 6 + Math.atan((2 * x - 1) / Math.sqrt(3)) / Math.sqrt(3) - Math.log(x + 1) / 3), 
			2*(Math.atan(Math.sqrt(3)/3)/Math.sqrt(3) - Math.atan(1/Math.sqrt(3))/Math.sqrt(3) + Math.PI/(2*Math.sqrt(3))),
			"2x / (x^2 + 1)");
		functions[6].setFunctionDatas(x -> 1 / (9 + x*x),
			x -> Math.atan(x/3)/3,
			Math.PI/6,
			"1 / (9 + x^2)");
	}

	public void fillQAGIL(){
		functions[0].setFunctionDatas(x -> x / (Math.pow(x, 4) + 1),	
			x -> 0.5* Math.atan(x*x),
			Math.PI / 4,
			"x / (x^4 + 1)");	
		functions[1].setFunctionDatas(x -> 1 / (x * x + 1), 
			x -> Math.atan(x),
			-Math.PI / 2,
			"1 / (x^2 + 1)");
		functions[2].setFunctionDatas(x -> -2 / (x * x + 1), 
			x -> -2 * Math.atan(x),
			Math.PI,
			"-2 / (x^2 + 1)");
		functions[3].setFunctionDatas(x -> Math.atan(x)*Math.atan(x)/(1 + x*x), 
			x->Math.atan(x) *Math.atan(x) *Math.atan(x)/3,
			-Math.PI*Math.PI*Math.PI / 24,
			"atan(x)^2 / (1 + x^2)");
		functions[4].setFunctionDatas(x -> 1 / (9 + x*x),
			x -> Math.atan(x/3)/3,
			-Math.PI/6,
			"1 / (9 + x^2)");
	}
// QAWF
	public void fillSineQAWF(double a){
		functions[0].setFunctionDatas(x -> Math.sin(x), 
			x -> (Math.cos(x) * Math.sin(a * x) - a * Math.sin(x) * Math.cos(a * x)) / (a * a - 1),
			0,
			"sin");
		functions[1].setFunctionDatas(x -> Math.cos(x), 
			x -> (Math.sin(x) * Math.sin(a * x) + a * Math.cos(x) * Math.cos(a * x)) / (1 - a * a),
			0, 
			"cos");
		functions[2].setFunctionDatas(x -> x * x, 
			x -> ((2 - a * a * x * x) * Math.cos(a * x) + 2 * a * x * Math.sin(a * x)) / (a * a * a),
			0,
			"x^2");
		functions[3].setFunctionDatas(x -> Math.sin(x) * Math.sin(x),
			x -> 0.25 * (Math.cos(a * x - 2 * x) / (a - 2) - 2 * Math.cos(a * x) / a + Math.cos(a * x + 2 * x) / (a + 2)),
			0,
			"sin^2");
		functions[4].setFunctionDatas(x -> 9 * x + Math.pow(Math.cos(x), 4),	
			x -> 9 * Math.sin(a * x) / a / a - Math.cos(a * x - 4 * x) / 16 / (a - 4) - Math.cos(a * x - 2 * x) / 4 / (a - 2)
					- Math.cos(a * x) * 9 * x / a - Math.cos(a * x) * 3 / 8 / a	- Math.cos(a * x + 2 * x) / 4 / (a + 2)	- Math.cos(a * x + 4 * x) / 16 / (a + 4),
			0,
			"9x + cos(x)^4"); //?????
		functions[5].setFunctionDatas(x -> Math.sin(x) + Math.cos(x),
			x -> (Math.cos(x) * (Math.sin(a * x) - a * Math.cos(a * x)) - Math.sin(x) * (Math.sin(a * x) + a * Math.cos(a * x))) / (a * a - 1),
			0,
			"sinx + cosx");
		functions[6].setFunctionDatas(x -> 0.25 * x,
			x -> (Math.sin(a*x) - a * x * Math.cos(a * x)) / 4 / a / a,
			0, 
			"x / 4");
		functions[7].setFunctionDatas(x -> 5 * x + 1,
			x -> ((5*(Math.sin(a*x)-a*x*Math.cos(a*x)))/a-Math.cos(a*x))/a,
			0,
			"5x + 1");
		functions[8].setFunctionDatas(x -> Math.pow(8, -x),
			x -> -(a * Math.cos(a * x) + Math.log(8) * Math.sin(a * x)) / (a * a + Math.log(8) * Math.log(8)) / Math.pow(8, x), 
			0,
			"1 / 5^x");
		functions[9].setFunctionDatas(x -> x * Math.pow(5, -x),
			x -> (-(a*a*(x*Math.log(5) - 1) + Math.log(5)*Math.log(5)*(x*Math.log(5) + 1))*Math.sin(a*x) 
				- a * (a*a*x + x*Math.log(5)*Math.log(5) + Math.log(25))*Math.cos(a*x))
				/ (a * a + Math.log(5) * Math.log(5))/ (a * a + Math.log(5) * Math.log(5)) / Math.pow(5, x),
			0,
			"x / 5^x");
	}

	public void fillCosineQAWF(double a){
		functions[0].setFunctionDatas(x -> x * x, 
			x -> ((a * a * x * x - 2) * Math.sin(a * x) + 2 * a * x * Math.cos(a * x)) / (a * a * a),
			0,
			"x^2");
		functions[1].setFunctionDatas(x -> Math.sin(x) * Math.sin(x),
			x -> 0.25 * (- Math.sin(a * x - 2 * x) / (a - 2) + 2 * Math.sin(a * x) / a - Math.sin(a * x + 2 * x) / (a + 2)),
			0,
			"sin^2");
		functions[2].setFunctionDatas(x -> 9 * x + Math.pow(Math.cos(x), 4),	
			x -> 9 * Math.cos(a * x) / a / a + Math.sin(a * x - 4 * x) / 16 / (a - 4)
				+ Math.sin(a * x - 2 * x) / 4 / (a - 2) + Math.sin(a * x) * 9 * x / a
				+ Math.sin(a * x) * 3 / 8 / a + Math.sin(a * x + 2 * x) / 4 / (a + 2)
				+ Math.sin(a * x + 4 * x) / 16 / (a + 4),
			0,
			"9x + cos(x)^4");
		functions[3].setFunctionDatas(x -> Math.sin(x) + Math.cos(x),
			x -> (Math.sin(x) * (a * Math.sin(a * x) - Math.cos(a * x)) + Math.cos(x) * (a * Math.sin(a * x) + Math.cos(a * x))) / (a * a - 1),
			0,
			"sinx + cosx");
		functions[4].setFunctionDatas(x -> Math.pow(8, -x),
			x -> (a * Math.sin(a * x) - Math.log(8) * Math.cos(a * x)) / (a * a + Math.log(8) * Math.log(8)) / Math.pow(8, x), 
			0,
			"1 / 5^x");
		functions[5].setFunctionDatas(x -> x * Math.pow(5, -x),
			x -> (-(a*a*(x*Math.log(5) - 1) + Math.log(5)*Math.log(5)*(x*Math.log(5) + 1))*Math.cos(a*x) 
				+ a * (a*a*x + x*Math.log(5)*Math.log(5) + Math.log(25))*Math.sin(a*x))
				/ (a * a + Math.log(5) * Math.log(5))/ (a * a + Math.log(5) * Math.log(5)) / Math.pow(5, x),
			0,
			"x / 5^x");
	}
// QAWC
	public void fillQAWC(double c){
		functions[0].setFunctionDatas(x -> x * x,
			x -> c*c * Math.log(Math.abs(x - c)) + (2 * c * x + x * x) / 2,
			"x^2");
		functions[1].setFunctionDatas(x -> 4 * x * x * x + 3 * x * x + 3,
			x -> (3 + 3 * c*c + 4 * c*c*c) * Math.log(Math.abs(x - c)) + ((24 * c*c + 18 * c) * x + (12 * c + 9) * x*x + 8 * x*x*x) / 6,
			"4x^3 + 3x^2 + 3");
		functions[2].setFunctionDatas(x -> x,	
			x -> c * Math.log(Math.abs(x - c)) + x,
			"ln");
	}
// QAWS
	public void fillQAWS11(double a, double b){
		functions[0].setFunctionDatas(x -> Math.sin(x), 
			x -> b*(Math.sin(x)-x*Math.cos(x))+a*(Math.sin(x)-x*Math.cos(x))-2*x*Math.sin(x)-(2-x*x)*Math.cos(x)+a*b*Math.cos(x),
			"sin");
		functions[1].setFunctionDatas(x -> Math.cos(x), 
			x -> b*(Math.cos(x)+x*Math.sin(x))+a*(Math.cos(x)+x*Math.sin(x))-(x*x-2)*Math.sin(x)-a*b*Math.sin(x)-2*x*Math.cos(x), 
			"cos");
		functions[2].setFunctionDatas(x -> x * x, 
			x -> -(20*a*b*x*x*x+(-15*b-15*a)*x*x*x*x+12*Math.pow(x, 5))/60,
			"x^2");
		functions[3].setFunctionDatas(x -> 4 * x * x * x + 3 * x * x + 3,
			x -> (180*a*b*x + (-90*b-90*a)*Math.pow(x,2)+(60*a*b+60)*Math.pow(x,3) + ((60*a-45)*b-45*a)*Math.pow(x,4)+(-48*b-48*a+36)*Math.pow(x,5)+40*Math.pow(x,6))/-60, 
			"4x^3 + 3x^2 + 3");
		functions[4].setFunctionDatas(x -> Math.pow(5, x),
			x -> -((2-2*Math.log(5)*x+Math.log(5)*Math.log(5)*x*x)*Math.pow(Math.E, (Math.log(5)*x)))/Math.pow(Math.log(5), 3)+(b*(Math.log(5)*x-1)*Math.pow(Math.E, Math.log(5)*x))/Math.log(5)/Math.log(5)+
				(a*(Math.log(5)*x-1)*Math.pow(Math.E,Math.log(5)*x))/Math.pow(Math.log(5), 2)-(a*b*Math.pow(5, x))/Math.log(5),
			"5^x");
	}
	public void fillQAWS10(double a, double b){
		functions[0].setFunctionDatas(x -> Math.sin(x), 
			x -> Math.sin(x)-x*Math.cos(x)+a*Math.cos(x),
			"sin");
		functions[1].setFunctionDatas(x -> Math.cos(x), 
			x -> x*Math.sin(x)-a*Math.sin(x)+Math.cos(x), 
			"cos");
		functions[2].setFunctionDatas(x -> x * x, 
			x -> (3*Math.pow(x, 4)-4*a*Math.pow(x, 3))/12,
			"x^2");
		functions[3].setFunctionDatas(x -> 4 * x * x * x + 3 * x * x + 3,
			x -> (-60*a*x + 30*Math.pow(x,2) - 20*a*Math.pow(x,3) + (15-20*a)*Math.pow(x,4) + 16*Math.pow(x,5))/20, 
			"4x^3 + 3x^2 + 3");
		functions[4].setFunctionDatas(x -> Math.pow(5, x),
			x -> ((Math.log(5)*x-1)*Math.pow(Math.E, Math.log(5)*x))/Math.pow(Math.log(5), 2)-(a*Math.pow(5, x))/Math.log(5),
			"5^x");
	}
	public void fillQAWS01(double A, double b){
		functions[0].setFunctionDatas(x -> Math.sin(x), 
			x -> -Math.sin(x)+x*Math.cos(x)-b*Math.cos(x),
			"sin");
		functions[1].setFunctionDatas(x -> Math.cos(x), 
			x -> -x*Math.sin(x)+b*Math.sin(x)-Math.cos(x), 
			"cos");
		functions[2].setFunctionDatas(x -> x * x, 
			x -> -(3*Math.pow(x, 4)-4*b*Math.pow(x, 3))/12,
			"x^2");
		functions[3].setFunctionDatas(x -> 4 * x * x * x + 3 * x * x + 3,
			x -> -(-60*b*x+30*Math.pow(x,2)-20*b*Math.pow(x,3)+(15-20*b)*Math.pow(x,4)+16*Math.pow(x,5))/20, 
			"4x^3 + 3x^2 + 3");
		functions[4].setFunctionDatas(x -> Math.pow(5, x),
			x -> (b* Math.pow(5, x))/Math.log(5)-((Math.log(5)*x-1)* Math.pow(Math.E, Math.log(5)*x))/Math.pow(Math.log(5), 2),
			"5^x");
	}
// Get Functions
	public double getIntegalsAB(int i, double a, double b){
		return this.functions[i].integralObj.apply(b) - this.functions[i].integralObj.apply(a);
	}

	public double getIntegalsInfA(int i, double a){
		return this.functions[i].result - this.functions[i].integralObj.apply(a);
	}

	public double getIntegalsInfB(int i, double b){
		return this.functions[i].integralObj.apply(b) - this.functions[i].result;
	}

	public DoubleFunction getFunObj(int i){
		return this.functions[i].mathFunObj;
	}

	public String getFunName(int i){
		return this.functions[i].mathFunName;
	}

	public double [] getPoints(int i){
		return this.functions[i].points;
	}

	public double getPoint(int i){
		return this.functions[i].points[0];
	}

	public double getResult(int i){
		return this.functions[i].result;
	}

}


package Tests;

import java.util.function.*;
import java.lang.Math.*;

public class Function{
	public DoubleFunction <Double> mathFunObj;
	public DoubleFunction <Double> integralObj;
	public double [] points;
	public String mathFunName;
	public double result;

	public Function(){}

	public void setFunctionDatas(DoubleFunction<Double> funObj, DoubleFunction<Double> intObj, String name){ 
		this.mathFunObj 	= funObj;
		this.integralObj 	= intObj;
		this.mathFunName 	= name;
	}
	
	public void setFunctionDatas(DoubleFunction<Double> funObj, DoubleFunction<Double> intObj, double result, String name){ 
		this.mathFunObj 	= funObj;
		this.integralObj 	= intObj;
		this.result 		= result;
		this.mathFunName 	= name;
	}
	
	public void setFunctionDatas(DoubleFunction<Double> funObj, double result, String name){ 
		this.mathFunObj 	= funObj;
		this.result 		= result;
		this.mathFunName 	= name;
	}

	public void setFunctionDatas(DoubleFunction<Double> funObj, DoubleFunction<Double> intObj, double [] points, String name){ 	
		this.points = points;
		this.mathFunObj = funObj;
		this.integralObj = intObj;
		this.mathFunName = name;
	}

	public void setFunctionDatas(DoubleFunction<Double> funObj, double point, DoubleFunction<Double> intObj, String name){ 	
		this.points = new double [1];
		this.points[0] = point;
		this.mathFunObj = funObj;
		this.integralObj = intObj;
		this.mathFunName = name;
	}

}
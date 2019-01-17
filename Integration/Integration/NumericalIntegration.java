package Integration;

import java.util.Arrays;
import java.util.function.*;

/**
 * NumericalIntegration can be used to performing a various ways of numerical integration. 
 * All integration methods was implementing, using the gsl library, from c language.
 * Algorithms like QNG, QAG or QAGS was designed, based on Gauss-Kronrod rules. The other ones, like QAWO or QAWS uses a various weigth functions.
 * 
 * @author Joanna Mirochna
 * @version %G%
 */
public class NumericalIntegration {
// ---States---------------------------------
	/* The range of integration */
	private double a, b;
	/* the desired absolute and relative error limits */
	private double epsabs, epsrel;
	/* functional interface */
	private DoubleFunction <Double> funObj;
// ---Constructors---------------------------
	/**
	 * The class constructor, which allows to define the function, that will be integrated, the integration range, 
	 * the allowed absolute and relative errors limits and the maximum number of subintervals.
	 * 
	 * @param funObj 		the function, which will be integrated
	 * @param a 			the floor of the integration range
	 * @param b 			the ceil of the integration range
	 * @param epsabs 		the allowed absolute errors limits
	 * @param epsrel 		the allowed relative errors limits
	 * @throws WrongValue 	if the provided epsabs or epsrel value is lower than 0 or if the provided a is not smaller than b
	 * 
	 */
	public NumericalIntegration(DoubleFunction<Double> funObj, double a, double b, double epsabs, double epsrel) throws WrongValue{
		if (!(b > a)) 	throw new WrongValue("Constructor: The b value have to be largen than a!");
		if (epsabs < 0)	throw new WrongValue("Constructor: The epsabs walue must be grater than 0.");
		if (epsrel < 0)	throw new WrongValue("Constructor: The epsrel walue must be grater than 0.");
		this.funObj = funObj;
		this.a = a;
		this.b = b;
		this.epsabs = epsabs;
		this.epsrel = epsrel;
	}
	/**
	 * The class constructor, which allows to define the function, that will be integrated and the integration range.
	 * The allowed absolute and relative errors limits and the maximum number of subintervals are filled with default values.
	 * 
	 * @param funObj 		the function, which will be integrated
	 * @param a 			the floor of the integration range
	 * @param b 			the ceil of the integration range
	 * @throws WrongValue 	if the provided a is not smaller than b
	 * 
	 */
	public NumericalIntegration(DoubleFunction<Double> funObj, double a, double b) throws WrongValue{
		if (!(b > a)) throw new WrongValue("Constructor: The b value have to be largen than a!");
		this.funObj = funObj;
		this.epsabs = 1e-10;
		this.epsrel = 1e-7;
		this.a = a;
		this.b = b;
	}
// ---Gets-----------------------------------
	/**
	 * method, which runs the function, holded in funObj and returns it result
	 *
	 * @param x the value, for which the calculation will be provided
	 * @return the result of function
	 *
	 */
	public double integrableFunction(double x) {
		return funObj.apply(x);
	}
	/**
	 * The function returns the floor of the integration range
	 * 
	 * @return a the floor of the integration range
	 * 
	 */
	public double getA(){
		return this.a;
	}
	/**
	 * The function returns the ceil of the integration range
	 * 
	 * @return b the ceil of the integration range
	 * 
	 */
	public double getB(){
		return this.b;
	}
	/**
	 * The function returns the allowed absolute errors limits
	 * 
	 * @return epsabs 		the allowed absolute errors limits
	 * 
	 */
	public double getEpsabs(){
		return this.epsabs;
	}
	/**
	 * The function returns the allowed relative errors limits
	 * 
	 * @return epsrel 		the allowed relative errors limits
	 * 
	 */
	public double getEpsrel(){
		return this.epsrel;
	}
// ---Sets-----------------------------------
	/**
	 * The function allows to set the function, that will be integrated
	 * 
	 * @param funObj 	the function, which will be integrated
	 * 
	 */
	public void setFunction(DoubleFunction<Double> funObj){
		this.funObj = funObj;
	}
	/**
	 * The function allows to set the floor of the integration range
	 * 
	 * @param a 		the floor of the integration range
	 * 
	 */
	public void setA(double a){
		this.a = a;
	}
	/**
	 * The function allows to set the ceil of the integration range
	 * 
	 * @param b 			the ceil of the integration range
	 * 
	 */
	public void setB(double b){
		this.b = b;
	}
	/**
	 * The function allows to set the allowed absolute errors limits
	 * 
	 * @param epsabs 		the allowed absolute errors limits
	 * @throws WrongValue 	if the provided epsabs value is lower than 0.
	 * 
	 */
	public void setEpsabs(double epsabs) throws WrongValue{
		if (epsabs < 0)	throw new WrongValue("Constructor: The epsabs walue must be grater than 0.");
		this.epsabs = epsabs;
	}
	/**
	 * The function allows to set the allowed relative errors limits
	 * 
	 * @param epsrel 		the allowed relative errors limits
	 * @throws WrongValue 	if the provided epsrel value is lower than 0.
	 * 
	 */
	public void setEpsrel(double epsrel) throws WrongValue{
		if (epsrel < 0)	throw new WrongValue("Constructor: The epsrel walue must be grater than 0.");
		this.epsrel = epsrel;
	}
// ---Methods--------------------------------
	// -QNG
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QNG.
		 * The QNG algorithm was designed for fast integration of smooth functions and uses Gauss-Kronrod 10-point, 21-point, 43-point and 87-point integration rules.
		 * The rules are applied in sequence, until the integration result is not responds to epsabs and epsrel errors.
		 * It is works over range (a,b).
		 *
		 * @return the result of integration method integrableFunction
		 * 
		 */
		public double calcIntegrationQNG() throws GSLErrors{		
			return gslQNG();
		}
	// -QAG
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAG.
		 * calcIntegrationQAG allows to decide, which version of Gauss-Kronrod should be applied (15, 21, 31, 41, 51 or 61 point rule).
		 * Then it applies the choosen rule to function, until the integration result is not responds to epsabs and epsrel errors.
		 * It is works over range (a,b).
		 *
		 * @param key		The key value, defines a degree of Gauss-Kronrod rule point
		 * @return the result of integration
		 * @throws WrongValue if the provided key is not in {15, 21, 31, 41, 51, 61}
		 * 
		 */
		public double calcIntegrationQAG(int key) throws WrongValue, GSLErrors{
			int gsl_key = (int)(key*0.1);

			if (!(key == 15 || key == 21 || key == 31 || key == 41 || key == 51 || key == 61))	
				throw new WrongValue("Error QAG: Wrong value of key.\nThe correct values are:\n\t'15' for 15 point Gauss-Kronrod rule\n\t'21' for 21 point Gauss-Kronrod rule\n\t'31' for 31 point Gauss-Kronrod rule\n\t'41' for 41 point Gauss-Kronrod rule\n\t'51' for 51 point Gauss-Kronrod rule\n\t'61' for 61 point Gauss-Kronrod rule");		

			return gslQAG(gsl_key);
		}
	// -QAGS
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAGS.
		 * The method applies Gauss-Kronrod 21 point rule to function over range (a,b), until the integration result is not responds to epsabs and epsrel errors.
		 * 
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationQAGS() throws GSLErrors{
			return gslQAGS();
		}
	// -QAGP
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAGP.
		 * The algorithm applies QAGS for every singular point, defines in pts array. algorithm requires ascending order of the singular points, so the
		 * array is sorted in calcIntegrationQAGP before the QAGP procedure run.
		 *
		 * @param pts		Array of singularity points, for which the QAGS procedure will be applied
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationQAGP(double [] pts) throws GSLErrors{	
			Arrays.sort(pts);
			return gslQAGP(pts);
		}
	// -QAGI
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAGI.
		 * The algorithm calculate the integral over the infinite interval.
		 *
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationQAGI() throws GSLErrors{		
			return gslQAGI();
		}
	// -QAGIU
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAGIU.
		 * The algorithm calculate the integral over the semi-infinite interval (a, +∞).
		 *
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationQAGIU() throws GSLErrors{	
			return gslQAGIU();
		}
	// -QAGIL
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAGIL.
		 * The algorithm calculate the integral over the semi-infinite interval (-∞, b).
		 *
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationQAGIL() throws GSLErrors{
			return gslQAGIL();
		}
	// -QAWC
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWC.
		 * QAWC uses algorithm QAG for the (a, b) interval, except the singular point (c), for which a special 25-point modified Clenshaw-Curtis rule is applied.
		 * 
		 * @param c		The singular point
		 * @return the result of integration
		 * @throws WrongValue if the provided c value in smaller than a or bigger than b
		 * 
		 */
		public double calcIntegrationQAWC(double c) throws WrongValue, GSLErrors{
			if (c < this.a || c > this.b) 
				throw new WrongValue("Error QAWC: The c value has to be bigger than a and smaller than b.");	
			return gslQAWC(c);
		}
	// -QAWS functions
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWS.
		 * To calculate the integral of function, the algorithm uses following formula:
		 * <p>
		 * I = \int_a^b dx f(x) W(x)
		 * <p>
		 * Where W(x) is a singular weight function:
		 * <p>
		 * W(x) = (x-a)^alpha (b-x)^beta
		 * 
		 * @param alpha		The value of power of weight function part: (x - a)
		 * @param beta		The value of power of weight function part: (x - b)
		 * @return the result of integration
		 * @throws WrongValue if the provided alpha or beta values are not bigger than -1
		 * 
		 */
		public double calcIntegrationQAWS(double alpha, double beta) throws WrongValue, GSLErrors{

			if (alpha < -1)
				throw new WrongValue("Error QAWS: The alpha parameter cannot be smaller than -1.");	

			if (beta < -1)
				throw new WrongValue("Error QAWS: The beta parameter cannot be smaller than -1.");	

			return gslQAWS(alpha, beta, 0, 0);
		}
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWS.
		 * To calculate the integral of function, the algorithm uses following formula:
		 * <p>
		 * I = \int_a^b dx f(x) W(x)
		 * <p>
		 * Where W(x) is a singular weight function:
		 * <p>
		 * W(x) = (x-a)^alpha (b-x)^beta log(x-a)
		 * 
		 * @param alpha		The value of power of weight function part: (x - a)
		 * @param beta		The value of power of weight function part: (x - b)
		 * @return the result of integration
		 * @throws WrongValue if the provided alpha or beta values are not bigger than -1
		 * 
		 */
		public double calcIntegrationQAWSLogA(double alpha, double beta) throws WrongValue, GSLErrors{

			if (alpha < -1)
				throw new WrongValue("Error QAWS: The alpha parameter cannot be smaller than -1.");	

			if (beta < -1)
				throw new WrongValue("Error QAWS: The beta parameter cannot be smaller than -1.");	

			return gslQAWS(alpha, beta, 1, 0);
		}
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWS.
		 * To calculate the integral of function, the algorithm uses following formula:
		 * <p>
		 * I = \int_a^b dx f(x) W(x)
		 * <p>
		 * Where W(x) is a singular weight function:
		 * <p>
		 * W(x) = (x-a)^alpha (b-x)^beta log(b-x) 
		 * 
		 * @param alpha		The value of power of weight function part: (x - a)
		 * @param beta		The value of power of weight function part: (x - b)
		 * @return the result of integration
		 * @throws WrongValue if the provided alpha or beta values are not bigger than -1
		 * 
		 */
		public double calcIntegrationQAWSLogB(double alpha, double beta) throws WrongValue, GSLErrors{

			if (alpha < -1)
				throw new WrongValue("Error QAWS: The alpha parameter cannot be smaller than -1.");	

			if (beta < -1)
				throw new WrongValue("Error QAWS: The beta parameter cannot be smaller than -1.");	

			return gslQAWS(alpha, beta, 0, 1);
		}
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWS.
		 * To calculate the integral of function, the algorithm uses following formula:
		 * <p>
		 * I = \int_a^b dx f(x) W(x)
		 * <p>
		 * Where W(x) is a singular weight function:
		 * <p>
		 * W(x) = (x-a)^alpha (b-x)^beta log(x-a) log(b-x)
		 * 
		 * @param alpha		The value of power of weight function part: (x - a)
		 * @param beta		The value of power of weight function part: (x - b)
		 * @return the result of integration
		 * @throws WrongValue if the provided alpha or beta values are not bigger than -1
		 * 
		 */
		public double calcIntegrationQAWSLogALogB(double alpha, double beta) throws WrongValue, GSLErrors{

			if (alpha < -1)
				throw new WrongValue("Error QAWS: The alpha parameter cannot be smaller than -1.");	

			if (beta < -1)
				throw new WrongValue("Error QAWS: The beta parameter cannot be smaller than -1.");	

			return gslQAWS(alpha, beta, 1, 1);
		}
	// -QAWO Cosine
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWO.
		 * The algorithm was created for integrands with oscillatory factor, cos(omega*x):
		 * <p>
		 * W(x) = cos(omega x)
		 * <p>
		 * QAWO uses a and b values to compute the length of the intergated interval.
		 * 
		 * @param omega		The parameter needed to weigth function calculation
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationCosineQAWO(double omega) throws GSLErrors{
			return gslQAWO(omega, 0);
		}
	// -QAWO Sine
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWO.
		 * The algorithm was created for integrands with oscillatory factor, sin(omega*x):
		 * <p>
		 * W(x) = sin(omega x)
		 * <p>
		 * QAWO uses a and b values to compute the length of the intergated interval.
		 * 
		 * @param omega		The parameter needed to weigth function calculation
		 * @param n			The number of levels of coefficients that are computed
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationSineQAWO(double omega) throws GSLErrors{
			return gslQAWO(omega, 1);	
		}
	// -QAWF Cosine
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWF.
		 * The algorithm was created for Fourier integral:
		 * <p>
		 * f(x)*cos(omega x)
		 * <p>
		 * where cos(omega x) is the W(x) function
		 * <p>
		 * QAWF computes the integral over the semi-infinite interval (a, +∞).
		 *
		 * @param omega		The parameter needed to weigth function calculation
		 * @param n			The number of levels of coefficients that are computed
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationCosineQAWF(double omega) throws GSLErrors{
			return gslQAWF(omega, 0);
		}
	// -QAWF Sine
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm QAWF.
		 * The algorithm was created for Fourier integral:
		 * <p>
		 * f(x)*sin(omega x)
		 * <p>
		 * where sin(omega x) is the W(x) function
		 * <p>
		 * QAWF computes the integral over the semi-infinite interval (a, +∞).
		 *
		 * @param omega		The parameter needed to weigth function calculation
		 * @param n			The number of levels of coefficients that are computed
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationSineQAWF(double omega) throws GSLErrors{
			return gslQAWF(omega, 1);
		}
	// -CQUAD
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm CQUAD.
		 * The algorithm is based on Clenshaw-Curits quadrature rules of degree 4, 8, 16 and 32 over 5, 9, 17 and 33 nodes respectively.
		 * 
		 * @return the result of integration
		 * 
		 */
		public double calcIntegrationCQUAD() throws GSLErrors{		
			return gslCQUAD();
		}
	// -GLFIXED
		/**
		 * The method calculate the numerical integrate of mathematical function, using algorithm GLFIXED.
		 * The algorithm is based on Gauss-Lendree rule. The integration result is saved in an array.
		 *
		 * @return the array of result of integration
		 * 
		 */
		public double [][] calcIntegrationGLFIXED() throws GSLErrors{	
			double [] temp = gslGLFIXED();
			double [][] result = new double [temp.length / 2][2];

			int k = 0;

			for (int i = 0; i < result.length; i++) {
				result[i][0] = temp[k];
				result[i][1] = temp[k++ + result.length];
			}

			return result;
		}
// ---Natives--------------------------------
	private native double gslQNG() throws GSLErrors;

	private native double gslQAG(int key) throws GSLErrors;

	private native double gslQAGS() throws GSLErrors;
		
	private native double gslQAGP(double [] pts) throws GSLErrors;
	
	private native double gslQAGI() throws GSLErrors;
	
	private native double gslQAGIU() throws GSLErrors;
	
	private native double gslQAGIL() throws GSLErrors;
	
	private native double gslQAWC(double c) throws GSLErrors;
	
	private native double gslQAWS(double alpha, double beta, int mu, int nu) throws GSLErrors;
	
	private native double gslQAWO(double omega, int mode) throws GSLErrors;
	
	private native double gslQAWF(double omega, int mode) throws GSLErrors;
	
	private native double gslCQUAD() throws GSLErrors;
	
	private native double [] gslGLFIXED() throws GSLErrors;
// ---LoadLibrary----------------------------
	// The calling of loadLibrary, needed to native methods working
	static{
		System.loadLibrary("NumericalIntegration");
	}

}
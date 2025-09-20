// header file
import java.io.*;

public class SDGSS extends SteepestDescent {
	
	// the golden ratio
	private final double PHI = (1. + Math.sqrt(5)) / 2.;
	// maximum step size
	private double maxStep;
	// minimum step size
	private double minStep;
	// delta parameter
	private double delta;
	
	
	// default constructor
	public SDGSS () { this.maxStep = 1.0; this.minStep = 0.001; this.delta = 0.001; }
	
	// overloaded constructor
	public SDGSS ( double maxStep , double minStep , double delta ) { this.maxStep = maxStep; this.minStep = minStep; this.delta = delta; }
	
	
	// getters
	public double getMaxStep () { return this.maxStep; }
	
	public double getMinStep () { return this.minStep; }
	
	public double getDelta () { return this.delta; }
	
	
	// setters
	public void setMaxStep ( double a ) { this.maxStep = a; }
	
	public void setMinStep ( double a ) { this.minStep = a; }
	
	public void setDelta ( double a ) { this.delta = a; }
	
	
	@Override // find step size via golden section search
	public double lineSearch ( Polynomial P, double [] x ) {
		
		// get direction vector
		double [] d = this.direction(P, x);
		
		// set initial interval
		double a = this.minStep, b = this.maxStep, c = a + (b - a) / this.PHI;
		
		// return step size
		return GSS(a, b, c, x, d, P);
		
	} // end of function
	
	
	@Override // get algorithm parameters from user
	public boolean getParamsUser () throws NumberFormatException, IOException {
		
		System.out.println("Set parameters for SD with a golden section line search:");
		
		// get maximum step size
		double mx = Main.getDouble("Enter GSS maximum step size (0 to cancel): ", 0.00, Double.POSITIVE_INFINITY);
		if (mx == 0.00) { return false; }
		
		// get minimum step size
		double mn = Main.getDouble("Enter GSS minimum step size (0 to cancel): ", 0.00, mx);
		if (mn == 0.00) { return false; }
		
		// get delta parameter
		double d = Main.getDouble("Enter GSS delta (0 to cancel): ", 0.00, Double.POSITIVE_INFINITY);
		if (d == 0.00) { return false; }
		
		// user canceled process
		if (!super.getParamsUser()) { return false; }
		
		// new algorithm parameters set
		else { this.maxStep = mx; this.minStep = mn; this.delta= d; return true; }
		
	} // end of function
	
	
	@Override // print algorithm parameters
	public void print () {
		
		// print parameter
		System.out.println("\nSD with a golden section line search:"); super.print();
		System.out.println("GSS maximum step size: " + this.maxStep);
		System.out.println("GSS minimum step size: " + this.minStep);
		System.out.println("GSS delta: " + this.delta + "\n");
		
	} // end of function
	
	
	// run golden section search algorithm given initial interval
	private double GSS ( double a, double b, double c, double [] x, double [] dir, Polynomial P ) {
		
		// calculate sample bound with larger right interval
		double d = b - (b - c) / this.PHI, alpha = 1;
		
		// evaluate function to be minimized at each bound
		double fa = P.f(param(x, a, dir)), fb = P.f(param(x, b, dir)), fc = P.f(param(x, c, dir)), fd;
		
		// check whether interval size is greater than set tolerance
		if (b - a >= this.delta) {
			
			// check for special considerations
			if (fc > fa || fc > fb) {
				
				// function improves with largest step size
				if (fa >= fb) { return b; }
				
				// function worsens with largest step size
				else { return a; }	
			}
			
			// interval contains one extremum
			else {
				
				// left interval is larger
				if (b - c < c - a) {
					
					// calculate sample point according to interval
					d = a + (c - a) / this.PHI; fd = P.f(param(x, d, dir));
					
					// minimum is in this interval
					if (fd < fa && fd < fc) { alpha = this.GSS(a, c, d, x, dir, P); }
					
					// minimum is in other interval
					else { alpha = this.GSS(d, b, c, x, dir, P); }
				}
				
				// right interval is larger
				else {
					
					// calculate sample point according to interval
					d = b - (b - c) / this.PHI; fd = P.f(param(x, d, dir));
					
					// minimum is in this interval
					if (fd < fb && fd < fc) { alpha = this.GSS(c, b, d, x, dir, P); }
					
					// minimum is in other interval
					else { alpha = this.GSS(a, d, c, x, dir, P); }
				}
			}
		}
		
		// interval size is less than set tolerance
		else { return alpha = (a + b) / 2; }
		
		// return step size
		return alpha;
        
	} // end of function
	
	
	// parameterize the input of the function to be minimized with respect to alpha
	private double [] param ( double [] x, double y, double [] dir ) {
		
		// create array to represent input
		double [] a = new double[x.length];
		
		// set new array values for input
		for (int i = 0; i < x.length; i++) { a[i] = x[i] + y * dir[i]; }
		
		// return input to be minimized
		return a;
		
	} // end of function
	
	
} // end of class



// header file
import java.io.*;

public class SDArmijo extends SteepestDescent {
	
	// Armijo maximum step size
	private double maxStep;
	// Armijo beta parameter
	private double beta;
	// Armijo tau parameter
	private double tau;
	// Armijo maximum number of iterations
	private int K;
	
	
	// default constructor
	public SDArmijo () { this.maxStep = 1.0; this.beta = 1.0E-4; this.tau = 0.5; this.K = 10; }
	
	// overloaded constructor
	public SDArmijo ( double maxStep, double beta, double tau, int K ) { this.maxStep = maxStep; this.beta = beta; this.tau = tau; this.K = K; }
	
	
	// getters
	public double getMaxStep () { return this.maxStep; }
	
	public double getBeta () { return this.beta; }
	
	public double getTau () { return this.tau; }
	
	public int getK () { return this.K; }
	
	
	// setters
	public void setMaxStep ( double a ) { this.maxStep = a; }
	
	public void setBeta ( double a ) { this.beta = a; }
	
	public void setTau ( double a ) { this.tau = a; }
	
	public void setK ( int a ) { this.K = a; }
	
	
	@Override // run Armijo line search
	public double lineSearch ( Polynomial P, double [] x ) {
		
		// declare step size and create array to represent lesser side of Armijo Decrease inequality
		double alpha = this.maxStep; double [] f = new double[x.length]; int iter;
		
		// set new array values for lesser side of Armijo Decrease inequality
		for (int i = 0; i < x.length; i++) { f[i] = x[i] - alpha * P.gradient(x)[i]; }
		
		// start line search and run until all conditions are met
		for (iter = 0; iter < this.K && P.f(f) > (P.f(x) - alpha * this.beta * Math.pow(P.gradientNorm(x), 2)) && alpha <= this.maxStep; iter++) {
			
			// increment step size
			alpha *= this.tau;
			
			// update lesser side of Armijo Decrease inequality
			for (int k = 0; k < x.length; k++) { f[k] = x[k] - alpha * P.gradient(x)[k]; }
			
			// line search fails to find a suitable step size
			if (iter == this.K - 1) { System.out.println("   Armijo line search did not converge!"); alpha = super.lineSearch(P, x); }
		}
		
		// return step size
		return alpha;
		
	} // end of function
	
	
	@Override // get algorithm parameters from user
	public boolean getParamsUser () throws NumberFormatException, IOException {
		
		System.out.println("Set parameters for SD with an Armijo line search:");
		
		// get Armijo max step size
		double m = Main.getDouble("Enter Armijo max step size (0 to cancel): ", 0.00, Double.POSITIVE_INFINITY);
		if (m == 0.00) { return false; }
		
		// get Armijo beta parameter
		double b = Main.getDouble("Enter Armijo beta (0 to cancel): ", 0.00, 1.00);
		if (b == 0.00) { return false; }
		
		// get Armijo tau parameter
		double t = Main.getDouble("Enter Armijo tau (0 to cancel): ", 0.00, 1.00);
		if (t == 0.00) { return false; }
		
		// get Armijo max number of iterations
		int k = Main.getInteger("Enter Armijo K (0 to cancel): ", 0, Integer.MAX_VALUE);
		if (k == 0) { return false; }
		
		// user canceled process
		if (!super.getParamsUser()) { return false; }
		
		// new algorithm parameters set
		else { this.maxStep = m; this.beta = b; this.tau = t; this.K = k; return true; }
		
	} // end of function
	
	
	@Override // print algorithm parameters
	public void print () {
		
		// print parameter
		System.out.println("\nSD with an Armijo line search:"); super.print();
		System.out.println("Armijo maximum step size: " + this.maxStep);
		System.out.println("Armijo beta: " + this.beta);
		System.out.println("Armijo tau: " + this.tau);
		System.out.println("Armijo maximum iterations: " + this.K);
		
	} // end of function
	
	
} // end of class



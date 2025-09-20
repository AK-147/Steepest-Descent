// header files
import java.io.*;
import java.util.*;

public class SteepestDescent {
	
	// tolerance epsilon
	private double eps;
	// maximum number of iterations
	private int maxIter;
	// starting point
	private double x0;
	// best point found for all polynomials
	private ArrayList <double[]> bestPoint;
	// best objective function value found for all polynomials
	private double [] bestObjVal;
	// best gradient norm found for all polynomials
	private double [] bestGradNorm; Double [] gradNorm;
	// computation time needed for all polynomials
	private long [] compTime; Long [] time;
	// number of iterations needed for all polynomials
	private int [] nIter; Integer [] iter;
	// whether or not results exist
	private boolean resultsExist;
	// maximum iteration limit
	private int iterLimit = 10000;
	// Armijo line search convergence results for all polynomials
	private boolean [] armijoResults;
	
	// default constructor
	public SteepestDescent () { this.eps = 0.001; this.maxIter = 100; this.x0 = 1.0; }
	
	// overloaded constructor
	public SteepestDescent ( double eps, int maxIter, double x0 ) { this.eps = eps; this.maxIter = maxIter; this.x0 = x0; }
	
	
	// getters
	public double getEps () { return this.eps; }
	
	public int getMaxIter () { return this.maxIter; }
	
	public double getX0 () { return this.x0; }
	
	public double [] getBestObjVal () { return this.bestObjVal; }
	
	public double [] getBestGradNorm () { return this.bestGradNorm; }
	
	public double [] getBestPoint (int i) { return this.bestPoint.get(i); }
	
	public int [] getNIter () { return this.nIter; }
	
	public long [] getCompTime () { return this.compTime; }
	
	public boolean hasResults () { return this.resultsExist; }
	
	
	// setters
	public void setEps ( double a ) { this.eps = a; }
	
	public void setMaxIter ( int a ) { this.maxIter = a; }
	
	public void setX0 ( double a ) { this.x0 = a; }
	
	public void setBestObjVal ( int i, double a ) { this.bestObjVal[i] = a; }
	
	public void setBestGradNorm ( int i, double a ) { this.bestGradNorm[i] = a; }
	
	public void setBestPoint ( int i, double [] a ) { this.bestPoint.set(i, a); }
	
	public void setCompTime ( int i, long a ) { this.compTime[i] = a; }
	
	public void setNIter ( int i, int a ) { this.nIter[i] = a; }
	
	public void setHasResults ( boolean a ) { this.resultsExist = a; }
	
	
	// initialize member arrays to correct size
	public void init ( ArrayList <Polynomial> P ) {
		
		// instantiate new array list for best points
		this.bestPoint = new ArrayList<double[]>();
		
		// initialize each array in array list to correct size
		for (int i = 0; i < P.size(); i++) { this.bestPoint.add(i, new double[P.get(i).getN()]); }
		
		// instantiate other metrics to correct size
		this.bestObjVal = new double[P.size()]; this.bestGradNorm = new double[P.size()];
		this.compTime = new long[P.size()]; this.nIter = new int[P.size()]; this.armijoResults = new boolean[P.size()];
	
	} // end of function
	
	
	// run the steepest descent algorithm
	public void run ( int i, Polynomial P ) {
		
		// instantiate array for starting point
		double [] x0 = new double[P.getN()];
		
		// set starting point to vector of equal dimension
		for (int j = 0; j < P.getN(); j++) { x0[j] = this.x0; }
		
		// get current time
		long start = System.currentTimeMillis();
		
		// set starting point and values
		this.bestPoint.set(i, x0); this.bestObjVal[i] = P.f(x0);
		this.bestGradNorm[i] = P.gradientNorm(x0); this.resultsExist = false;
		
		for (this.nIter[i] = 0; this.nIter[i] <= this.maxIter; this.nIter[i]++) {
			
			// get elapsed time
			this.compTime[i] = System.currentTimeMillis() - start;
			
			// set best values
			this.bestObjVal[i] = P.f(this.bestPoint.get(i)); this.resultsExist = true;
			this.bestGradNorm[i] = P.gradientNorm(this.bestPoint.get(i));
			
			// check for final iteration, sufficient closeness to local minimum, and undefined inputs
			if (this.nIter[i] == this.maxIter || P.gradientNorm(this.bestPoint.get(i)) <= this.eps || Double.isNaN(P.gradientNorm(this.bestPoint.get(i)))) { break; }
			
			// continue algorithm
			else {
				
				// get optimal step size for iteration
				double alpha = this.lineSearch(P, this.bestPoint.get(i));
				
				// line search found optimal step size
				if (alpha > 0) {
					
					// calculate the next point
					for (int k = 0; k < P.getN(); k++) { this.bestPoint.get(i)[k] += alpha * this.direction(P, this.bestPoint.get(i))[k]; }
					this.armijoResults[i] = true;
				}
				
				// line search failed
				else { this.nIter[i]++; this.armijoResults[i] = false; break; }
			}
		}
		
		System.out.println("Polynomial " + (i + 1) + " done in " + this.compTime[i] + "ms.");
		
	} // end of function
	
	
	// extraneous line search function which is always overridden by subclass methods
	public double lineSearch (Polynomial P, double [] x) { return -1; }
	
	
	// find the next direction
	public double [] direction ( Polynomial P, double [] x ) {
		
		// instantiate array for direction vector
		double [] direction = new double[P.getN()];
		
		// negate and add elements of gradient to direction vector
		for (int i = 0; i < direction.length; i++) { direction[i] = -P.gradient(x)[i]; }
		
		return direction;
		
	} // end of function
	
	
	// get parameters from user for n-dimensional polynomial
	public boolean getParamsUser () throws NumberFormatException, IOException { 
			
		// get tolerance epsilon
		double e = Main.getDouble("Enter tolerance epsilon (0 to cancel): ", 0.00, Double.POSITIVE_INFINITY);
		if (e == 0.00) { return false; }
		
		// get maximum number of iterations
		int m = Main.getInteger("Enter maximum number of iterations (0 to cancel): ", 0, this.iterLimit);
		if (m == 0) { return false; }
		
		// get starting point
		double x = Main.getDouble("Enter value for starting point (0 to cancel): ", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		if (x == 0.00) { return false; }
		
		// new algorithm parameters set
		else { this.eps = e; this.maxIter = m; this.x0 = x; this.resultsExist = false; return true; }
		
		
	} // end of function
	
	
	// print algorithm parameters
	public void print () {
		
		// print parameters
		System.out.println("Tolerance (epsilon): " + this.eps);
		System.out.println("Maximum iterations: " + this.maxIter);
		System.out.println("Starting point (x0): " + this.x0);
		
	} // end of function
	
	
	// print statistical summary of results
	public void printStats () {
		
		// print average for each metric in the sample
		this.printAverages("Average", false);
		
		// print standard deviation for each metric in the sample
		System.out.format("St Dev %13.3f%13.3f%18.3f%n", Main.stDev(gradNorm), Main.stDev(iter), Main.stDev(time));
		
		// print minimum for each metric in the sample
		System.out.format("Min    %13.3f%13.0f%18.0f%n", Main.min(gradNorm), Main.min(iter), Main.min(time));

		// print maximum for each metric in the sample
		System.out.format("Max    %13.3f%13.0f%18.0f%n%n", Main.max(gradNorm), Main.max(iter), Main.max(time));
		
	} // end of function
	
	
	// print final results for all polynomials
	public void printAll () {
			
		// print detailed results
		for (int i = 0; i < this.bestPoint.size(); i++) {
			
			// print column header for first iteration
			if (i == 0) { printSingleResult(i, false); }
			
			// print column values
			else { printSingleResult(i, true); }
		}
		
	} // end of function
	
	
	// print iteration results with optional column header 
	public void printSingleResult ( int i, boolean rowOnly ) {
		
		// print column header
		if (rowOnly == false) {
			System.out.println("-------------------------------------------------------------------------");
			System.out.println("Poly no.         f(x)   norm(grad)   # iter   Comp time (ms)   Best point   ");
			System.out.println("-------------------------------------------------------------------------");
		}
		
		// print column values
		System.out.format("%8d%13.6f%13.6f%9d%17d  ", i + 1, this.bestObjVal[i], this.bestGradNorm[i], this.nIter[i], this.compTime[i]);

		// best point is multidimensional
		if (this.bestPoint.get(i).length > 1) {
			
			// print best point
			for (int k = 0; k < this.bestPoint.get(i).length; k++) {
				
				// check for final iteration
				if (k != (this.bestPoint.get(i).length - 1)) { System.out.format(" %6.4f,", this.bestPoint.get(i)[k]); }
				
				else { System.out.format(" %6.4f", this.bestPoint.get(i)[k]); }
			}
		}
		
		// best point is one dimensional
		else { System.out.format("%6.4f", this.bestPoint.get(i)[0]); }
		
		// print extra carriage return at last iteration
		if (this.bestGradNorm[i] <= this.eps || this.nIter[i] >= this.maxIter || Double.isNaN(this.bestGradNorm[i]) || !this.armijoResults[i]) { System.out.println(""); }
	
	} // end of function

	
	// print performance metric averages labeled with title parameter
	public void printAverages ( String title, boolean rowOnly ) {
		
		// convert all metrics of performance to arrays of objects
		toObjects();
		
		// print column header
		if (rowOnly == false) {
			System.out.println("---------------------------------------------------");
			System.out.println("          norm(grad)       # iter    Comp time (ms)");
			System.out.println("---------------------------------------------------");
		}
		
		// print average for each metric of performance
		System.out.format("%-7s%13.3f%13.3f%18.3f%n", title, Main.mean(gradNorm), Main.mean(iter), Main.mean(time));
		
	} // end of function
	
	
	// convert primitive arrays to arrays of objects based on the wrapper class
	public void toObjects () {
		
		// initialize previously declared object arrays with same lengths as primitive versions
		gradNorm = new Double[this.bestGradNorm.length];
		iter = new Integer[this.compTime.length];
		time = new Long[this.nIter.length];
		
		// set primitive array elements in object arrays
		for (int i = 0; i < this.bestPoint.size(); i++) {
			gradNorm[i] = this.bestGradNorm[i]; iter[i] = this.nIter[i]; time[i] = this.compTime[i];
		}
		
	} // end of function
	

} // end of class



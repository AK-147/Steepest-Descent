// header file
import java.io.*;

public class SDFixed extends SteepestDescent {
	
	// fixed step size
	private double alpha; 
	
	// default constructor
	public SDFixed () { super(); this.alpha = 0.01; }
	
	// overloaded constructor
	public SDFixed ( double alpha ) { super(); this.alpha = alpha; }
	
	
	// getter
	public double getAlpha () { return this.alpha; }
	
	// setter
	public void setAlpha ( double a ) { this.alpha = a; }
	
	
	@Override // find fixed step size
	public double lineSearch ( Polynomial P, double [] x ) { return this.alpha; }
	
	
	@Override // get algorithm parameters from user
	public boolean getParamsUser () throws NumberFormatException, IOException {
		
		System.out.println("\nSet parameters for SD with a fixed line search:");
		
		// get fixed step size
		double a = Pro5_khanah41.getDouble("Enter fixed step size (0 to cancel): ", 0.00, Double.POSITIVE_INFINITY);
		if (a == 0.00) { return false; }
		
		// user canceled process
		if (!super.getParamsUser()) { return false; }
		
		// new algorithm parameters set
		else { this.alpha = a; return true; }
		
	} // end of function
	
	
	@Override // print algorithm parameters
	public void print () {
		
		// print parameters
		System.out.println("\nSD with a fixed line search:"); super.print();
		System.out.println("Fixed step size (alpha): " + this.alpha);
		
	} // end of function
	
	
} // end of class

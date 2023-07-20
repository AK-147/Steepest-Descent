
public class Polynomial {
	
	// number of variables
	private int n;
	// degree of polynomial
	private int degree;
	// coefficients
	private double [][] coefs;
	
	
	// default constructor
	public Polynomial () { this.n = 0; this.degree = 1; this.init(); }
	
	// overloaded constructor
	public Polynomial ( int n, int degree, double [][] coefs ) { this.n = n; this.degree = degree; this.coefs = coefs; }
	
	
	// getters
	public int getN () { return this.n; }
	
	public int getDegree () { return this.degree; }
	
	public double [][] getCoefs () { return this.coefs; }
	
	
	// setters
	public void setN ( int a ) { this.n = a; }
	
	public void setDegree ( int a ) { this.degree = a; }
	
	public void setCoef ( int j, int d, double a ) { this.coefs[j][d] = a; }
	
	
	// initialize member arrays to correct size
	public void init () { this.coefs = new double[this.n][this.degree + 1]; }
	
	
	// calculate function value at point x
	public double f ( double [] x ) {
		
		// running total variable for final value
		double value = 0;

		// evaluate polynomial
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j <= this.degree; j++) {
				if (this.degree == 0) { value += this.coefs[i][j]; }
				else { value += (this.coefs[i][j] * Math.pow(x[i], (this.degree - j))); }
			}
		}
		return value;

	} // end of function
	
	
	// calculate gradient at point x
	public double [] gradient ( double [] x ) {
		
		// array for storing final gradient vector
		double [] gradient = new double [this.n];
		
		for (int i = 0; i < this.n; i++) {			
			for (int j = 0; j < this.degree; j++) {
				
				// take partial derivative in this iteration and evaluate gradient at respective element of point
		        gradient[i] += this.coefs[i][j] * (this.degree - j) * Math.pow(x[i], this.degree - j - 1);
			}
		}	
		return gradient;
		
	} // end of function
	
	
	// calculate norm of gradient at point x
	public double gradientNorm ( double [] x ) {
		
		// get gradient at point x
		double [] gradient = this.gradient(x);
		
		// running total variable for final value
		double norm = 0;
		
		// update running total
		for (int i = 0; i < this.n; i++) { norm += Math.pow(gradient[i], 2); }
		
		// return norm of gradient
		return Math.sqrt(norm);
		
	} // end of function
	
	
	// indicate whether polynomial is set
	public boolean isSet () { 
		
		if (this.n == 0) { System.out.println("\nERROR: Polynomial function has not been entered!\n"); return false; }
		else { return true; }
		
	} // end of function
	
	
	// print out the polynomial
	public void print () { 
		
		// check whether polynomial is entered
		if (this.isSet()) {
			System.out.print("f(x) = ");
			
			for (int i = 0; i < this.n; i++) {
				System.out.print("( ");
				for (int j = 0; j <= this.degree; j++) {
					if (j != this.degree) {
						System.out.format("%.2fx" + (i + 1) + "^" + (this.degree - j) + " + ", this.coefs[i][j]);
					}
					
					else { System.out.format("%.2f )", this.coefs[i][j]); }
				}
				
				// check for final iteration
				if (i != this.n - 1) { System.out.print(" + "); }
				else { System.out.println(""); break; }
			}	
		}
		
	} // end of function

	
} // end of class


// header files
import java.io.*;
import java.util.*;

public class Pro5_khanah41 {

	// global object to read user input at command line
	public static BufferedReader bf;
	
	// main function which handles input exception errors
	public static void main ( String[] args ) throws NumberFormatException, IOException {
		
		// boolean variable for main loop
		Boolean running = true;
		
		// object to read user input at command line
		bf = new BufferedReader(new InputStreamReader(System.in));
		
		// instantiate new steepest descent objects and polynomial array list
		ArrayList <Polynomial> p = new ArrayList<Polynomial>(); SteepestDescent s = new SteepestDescent();
		SDFixed sf = new SDFixed(); SDArmijo sa = new SDArmijo(); SDGSS sg = new SDGSS();
		
		while (running) {
			
			// print menu to screen
			displayMenu();
			
			// get the entered choice
			String ans = bf.readLine();				
			
			// user chooses to read in polynomials from a specified file
			if (ans.equals("L") || ans.equals("l")) { loadPolynomialFile(p); sf.setHasResults(false); sa.setHasResults(false); sg.setHasResults(false); }
			
			
			// user chooses to view all polynomials
			else if (ans.equals("F") || ans.equals("f")) { printPolynomials(p); }

			
			// user chooses to clear all loaded polynomial functions
			else if (ans.equals("C") || ans.equals("c")) { clear(s, p); }
			
			
			// user chooses to set steepest descent parameters
			else if (ans.equals("S") || ans.equals("s")) { getAllParams(sf, sa, sg); }
			
			
			// user chooses to view steepest descent parameters
			else if (ans.equals("P") || ans.equals("p")) { sf.print(); sa.print(); sg.print(); }
			
			
			// user chooses to run steepest descent algorithms
			else if (ans.equals("R") || ans.equals("r")) { runAll(sf, sa, sg, p); }
			
			
			// user chooses to display algorithm performance with statistical summary
			else if (ans.equals("D") || ans.equals("d")) { printAllResults(sf, sa, sg, p); }
			
			
			// user chooses to compare average algorithm performance
			else if (ans.equals("X") || ans.equals("x")) { compare(sf, sa, sg); }
			
			
			// user terminates the program
			else if (ans.equals("Q") || ans.equals("q")) { running = false; }
			
			
			// user enters invalid choice
			else { System.out.println("\nERROR: Invalid menu choice!\n"); }
				
		} // end of main loop
		
		// close buffered reader and end program
		bf.close(); System.out.println("\nArrivederci.\n");
		
	} // end of main function

	
	// display the menu
	public static void displayMenu () {
		
		System.out.println("   JAVA POLYNOMIAL MINIMIZER (STEEPEST DESCENT)");
		System.out.println("L - Load polynomials from file");
		System.out.println("F - View polynomial functions");
		System.out.println("C - Clear polynomial functions");
		System.out.println("S - Set steepest descent parameters");
		System.out.println("P - View steepest descent parameters");
		System.out.println("R - Run steepest descent algorithms");
		System.out.println("D - Display algorithm performance");
		System.out.println("X - Compare average algorithm performance");
		System.out.println("Q - Quit\n");
		System.out.print("Enter choice: ");
		
	} // end of function
	
	
	// load the polynomial function details from a user-specified file
	public static boolean loadPolynomialFile ( ArrayList<Polynomial> P ) throws IOException {
		
		try {
			
			// get entered response
			System.out.print("\nEnter file name (0 to cancel): ");
			String ans = bf.readLine();
			
			// user chooses to cancel loading process
			if (ans.equals("0")) { System.out.println("\nFile loading process canceled.\n"); return false; }
			
			// objects to read file of polynomials
			BufferedReader fr = new BufferedReader(new FileReader(ans)); String line;
			
			// create array list of arrays to store coefficients of polynomial
			ArrayList <String[]> coefs = new ArrayList<String[]>();
			
			// number of total and consistent polynomials iterated through
			int totalPoly = 0; int numPoly = 0;
			
			// loop through each line in file
			while ((line = fr.readLine()) != null) {
				
				// declare objects and reset conditions for new polynomial of this iteration
				Polynomial p = new Polynomial(); boolean consistent = true; coefs.clear();
				
				while (line.charAt(0) != '*') {
			            
					// string array for storing coefficients in current line
					String [] varCoefs;
					
					if (consistent) {
						
						// extract coefficients in current line and set degree of polynomial
						varCoefs = line.split(","); p.setDegree(varCoefs.length - 1);

						// add coefficients and update number of variables
						coefs.add(varCoefs); p.setN(p.getN() + 1);
					}

					// check for final polynomial in file
					if ((line = fr.readLine()) == null) { break; }
				}
				
				// check for inconsistent dimensions
				for (int i = 0; i < coefs.size(); i++) {
					for (int j = i + 1; j < coefs.size(); j++) {
						if (coefs.get(i).length != coefs.get(j).length) { consistent = false; }
					}
				}
						
				// polynomial has consistent dimensions
				if (consistent) {
					
					// set coefficient array to correct size
					p.init();
					
					// set coefficients of polynomial
					for (int i = 0; i < coefs.size(); i++) {
						for (int j = 0; j < coefs.get(i).length; j++) {
							p.setCoef(i, j, Double.parseDouble(coefs.get(i)[j]));
						}
					}
					
					// add polynomial to array list and increment count
					P.add(p); numPoly++;
				}
				
				// polynomial has inconsistent dimensions
				else { System.out.println("\nERROR: Inconsistent dimensions in polynomial " + (totalPoly + 1) + "!"); }
				
				// increment total count
				totalPoly++;
			}
			
			// last line of the file is reached and all polynomials are loaded
			fr.close(); System.out.println("\n" + numPoly + " polynomials loaded!\n"); return true;
		}
		
		// user enters a file that does not exist
		catch (FileNotFoundException e) { System.out.println("\nERROR: File not found!\n"); return false; }
		
	} // end of function
	
	
	// get algorithm parameters from the user for each algorithm variation
	public static void getAllParams ( SDFixed SDF, SDArmijo SDA, SDGSS SDG ) throws NumberFormatException, IOException {
		
		// get fixed line search parameters
		if (SDF.getParamsUser()) { System.out.println("\nAlgorithm parameters set!\n"); }
		else { System.out.println("\nProcess canceled. No changes made to algorithm parameters.\n"); }
		
		// get Armijo line search parameters
		if (SDA.getParamsUser()) { System.out.println("\nAlgorithm parameters set!\n"); }
		else { System.out.println("\nProcess canceled. No changes made to algorithm parameters.\n"); }
		
		// get golden section search parameters
		if (SDG.getParamsUser()) { System.out.println("\nAlgorithm parameters set!\n"); }
		else { System.out.println("\nProcess canceled. No changes made to algorithm parameters.\n"); }
		
	} // end of function
	
	
	// run all algorithm variations on all loaded polynomials
	public static void runAll ( SDFixed SDF, SDArmijo SDA, SDGSS SDG, ArrayList<Polynomial> P ) {
		
		// check whether polynomials are loaded
		if (P.isEmpty()) { System.out.println("\nERROR: No polynomial functions are loaded!\n"); }
		
		// polynomials are loaded
		else {
			
			// initialize member arrays to correct size
			SDF.init(P); SDA.init(P); SDG.init(P);
			
			// run steepest descent with fixed line search
			System.out.println("\nRunning SD with a fixed line search:");
			for (int i = 0; i < P.size(); i++) { SDF.run(i, P.get(i)); }
			
			// run steepest descent with Armijo line search
			System.out.println("\nRunning SD with an Armijo line search:");
			for (int j = 0; j < P.size(); j++) { SDA.run(j, P.get(j)); }
			
			// run steepest descent with golden section line search
			System.out.println("\nRunning SD with a golden section line search:"); 
			for (int k = 0; k < P.size(); k++) { SDG.run(k, P.get(k)); }
			
			System.out.println("\nAll polynomials done.\n");
		}
		
	} // end of function
	
	
	// print all the polynomial functions currently loaded
	public static void printPolynomials ( ArrayList<Polynomial> P ) {
		
		// no polynomials functions are loaded
		if (P.isEmpty()) { System.out.println("\nERROR: No polynomial functions are loaded!\n"); }
		
		else {
			
			// print column header
			System.out.println("\n---------------------------------------------------------");
		    System.out.println("Poly No.  Degree   # vars   Function");
		    System.out.println("---------------------------------------------------------");
		    
		    // print polynomial information in current iteration
		    for (int i = 0; i < P.size(); i++) {
		    	System.out.format("%8d%8d%9d   ", i + 1, P.get(i).getDegree(), P.get(i).getN()); P.get(i).print();
		    }
		    
		    System.out.println("");
		}
		
	} // end of function
	
	
	// print the detailed results and statistics summaries for all algorithm variations
	public static void printAllResults ( SDFixed SDF, SDArmijo SDA, SDGSS SDG, ArrayList<Polynomial> P ) {
		
		// check whether each algorithm has existing results
		if (SDF.hasResults() && SDA.hasResults() && SDG.hasResults()) {
			
			// print all results for steepest descent with fixed line search
			System.out.println("\nDetailed results for SD with a fixed line search:"); SDF.printAll();
			System.out.println("\nStatistical summary for SD with a fixed line search:"); SDF.printStats();
			
			// print all results for steepest descent with Armijo line search
			System.out.println("\nDetailed results for SD with an Armijo line search:"); SDA.printAll();
			System.out.println("\nStatistical summary for SD with an Armijo line search:"); SDA.printStats();
			
			// print all results for steepest descent with golden section line search
			System.out.println("\nDetailed results for SD with a golden section line search:"); SDG.printAll();
			System.out.println("\nStatistical summary for SD with a golden section line search:"); SDG.printStats();
		}
		
		// results do not exist for all algorithms
		else { System.out.println("\nERROR: Results do not exist for all line searches!\n"); }
		
	} // end of function
	
	
	// compare the performance of the algorithms and pick winners
	public static void compare ( SDFixed SDF, SDArmijo SDA, SDGSS SDG ) {
		
		// check whether each algorithm has existing results
		if (SDF.hasResults() && SDA.hasResults() && SDG.hasResults()) {
			
			// print column header and rows of averages for each algorithm
			System.out.println(""); SDF.printAverages("Fixed", false); SDA.printAverages("Armijo", true); SDG.printAverages("GSS", true);
			
			// declare strings for storing fixed line search as winner by default
			String normWinner = "Fixed", iterWinner = "Fixed", timeWinner = "Fixed", overallWinner = "Fixed";
			
			// golden section norm performs better than fixed
			if (mean(SDG.gradNorm) < mean(SDF.gradNorm)) {
				
				// golden section norm performs better than Armijo
				if (mean(SDG.gradNorm) < mean(SDA.gradNorm)) { normWinner = "GSS"; }
				
				// Armijo norm performs better than golden section
				else { normWinner = "Armijo"; }
			}
			
			// Armijo norm performs better than fixed
			else if (mean(SDA.gradNorm) < mean(SDF.gradNorm)) { normWinner = "Armijo"; }
			
			
			// golden section has less iterations than fixed
			if (mean(SDG.iter) < mean(SDF.iter)) {
				
				// golden section has less iterations than Armijo
				if (mean(SDG.iter) < mean(SDA.iter)) { iterWinner = "GSS"; }
				
				// Armijo has less iterations than golden section
				else { iterWinner = "Armijo"; }
			}
			
			// Armijo has less iterations than fixed
			else if (mean(SDA.iter) < mean(SDF.iter)) { iterWinner = "Armijo"; }
			
			
			// golden section has faster computation than fixed
			if (mean(SDG.time) < mean(SDF.time)) {
				
				// golden section has faster computation than Armijo
				if (mean(SDG.time) < mean(SDA.time)) { timeWinner = "GSS"; }
				
				// Armijo has faster computation than golden section
				else { timeWinner = "Armijo"; }
			}
			
			// Armijo has faster computation than fixed
			else if (mean(SDA.time) < mean(SDF.time)) { timeWinner = "Armijo"; }
			
			
			// check for an overall winner in terms of best performance
			if (normWinner.equals(iterWinner) && normWinner.equals(timeWinner)) { overallWinner = normWinner; }
			
			// clear winner cannot be declared
			else { overallWinner = "Unclear"; }
			
			// print winners for each metric of performance
			System.out.println("---------------------------------------------------");
			System.out.format("%-7s%13s%13s%18s%n", "Winner", normWinner, iterWinner, timeWinner);
			System.out.println("---------------------------------------------------");
			System.out.println("Overall winner: " + overallWinner + "\n");
		}
		
		// results do not exist for all algorithms
		else { System.out.println("\nERROR: Results do not exist for all line searches!\n"); }
		
	} // end of function
	
	
	// clear all polynomials from array list and reset steepest descent results
	public static void clear ( SteepestDescent SD, ArrayList<Polynomial> P ) {
			
		P.clear(); SD.setHasResults(false); System.out.println("\nAll polynomials cleared.\n");
			
	} // end of function

	
	// get a valid integer in the range [LB, UB]
	public static int getInteger ( String prompt, int LB, int UB ) throws NumberFormatException, IOException {
		
		// boolean variable for loop
		boolean gettingInt = true;
		
		while (gettingInt) {
			
			try {
			
				// get integer
				System.out.print(prompt); int val = Integer.parseInt(bf.readLine());
				
				// user enters integer within range
				if ((val == (int)val) && (LB <= val && val <= UB)) { gettingInt = false; return val; }
				
				// user enters invalid integer
				else {
					
					// user enters invalid maximum number of iterations
					if (UB == 10000) { System.out.println("ERROR: Input must be an integer in [" + LB + ", " + UB + "]!\n"); }			
					
					else { System.out.println("ERROR: Input must be an integer in [" + LB + ", infinity]!\n"); }
				}
				
			// user enters string
			} catch (NumberFormatException e) {
				
				// user enters string for maximum number of iterations
				if (UB == 10000) { System.out.println("ERROR: Input must be an integer in [" + LB + ", " + UB + "]!\n"); }			
				
				// user enters string for integer
				else { System.out.println("ERROR: Input must be an integer in [" + LB + ", infinity]!\n"); }
			}
		}
			
		// extraneous return statement to satisfy compiler
		return 0;
		
	} // end of function
	
	
	// get a valid double in the range [LB, UB]
	public static double getDouble ( String prompt, double LB, double UB ) throws NumberFormatException, IOException {
		
		// boolean variable for loop
		boolean gettingDouble = true;
		
		while (gettingDouble) {
			
			try {
			
				// get double
				System.out.print(prompt); double val = Double.parseDouble(bf.readLine());
				
				// user enters integer within range
				if ((val == (double)val) && (LB <= val && val <= UB)) { gettingDouble = false; return val; }
				
				// user enters invalid double
				else if (UB == Double.POSITIVE_INFINITY) { System.out.format("ERROR: Input must be a real number in [%.2f, infinity]!\n%n", LB); }
				
				else { System.out.format("ERROR: Input must be a real number in [%.2f, %.2f]!\n%n", LB, UB); }
				
			// user enters string
			} catch (NumberFormatException e) {
				
				// user enters string for double
				if (LB == Double.NEGATIVE_INFINITY || LB == Double.MIN_VALUE) { 
					System.out.println("ERROR: Input must be a real number in [-infinity, infinity]!\n");
				}			
				
				else if (UB == Double.POSITIVE_INFINITY) { System.out.format("ERROR: Input must be a real number in [%.2f, infinity]!\n%n", LB); }
				
				else { System.out.format("ERROR: Input must be a real number in [%.2f, %.2f]!\n%n", LB, UB); }
			}
		}
			
		// extraneous return statement to satisfy compiler
		return 0;
		
	} // end of function
	
	
	// get the mean of all elements in given sample
	public static <T extends Number> double mean ( T [] x ) {
		
		// running total variable
		double total = 0;
		
		// sum all elements
		for (int i = 0; i < x.length; i++) { total += x[i].doubleValue(); }
		
		// return mean of sample
		return total / x.length;
		
	} // end of function
	
	
	// get the standard deviation of all elements in given sample
	public static <T extends Number> double stDev ( T [] x ) {
		
		// running total variable
		double total = 0;
		
		// sum squares of absolute differences of all elements and mean
		for (int i = 0; i < x.length; i++) { total += Math.pow(Math.abs(x[i].doubleValue() - mean(x)), 2); }
		
		// return standard deviation of sample
		return Math.sqrt(total / (x.length - 1));
		
	} // end of function
	
	
	// get the minimum value of all elements in given sample
	public static <T extends Number> double min ( T [] x ) {
		
		// dynamic variable for storing minimum
		double min = x[0].doubleValue();
		
		// find minimum value in sample
		for(int i = 0; i < x.length; i++) { if (x[i].doubleValue() < min) { min = x[i].doubleValue(); } }
		
		// return result
		return min;
		
	} // end of function
	
	
	// get the maximum value of all elements in given sample
	public static <T extends Number> double max ( T [] x ) {
		
		// dynamic variable for storing maximum
		double max = x[0].doubleValue();
		
		// find maximum value in sample
		for(int i = 0; i < x.length; i++) { if (x[i].doubleValue() > max) { max = x[i].doubleValue(); } }
		
		// return result
		return max;
		
	} // end of function

	
} // end of class


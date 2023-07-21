<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
</head>
<body>
  <img src="https://github.com/AK-147/Steepest-Descent/blob/main/SD%20Banner.png?raw=true" alt="Banner"/>
  
  <h2>:book: Description</h2>
  <ul>
    <li>:chart_with_downwards_trend: An algorithm to minimize multivariate polynomials via Steepest Descent using multiple line search methods</li>
    <li>:newspaper: Has a text-based user interface for use in command line along with an expansive menu with various options</li>
    <li>:books: Created as the cumalative final project for university course of object oriented programming fundamentals</li>
  </ul>

  <h2>:file_folder: Contents</h2>
  <p>The <strong>bin</strong> folder can be ignored for our purposes. Looking at the <strong>src</strong> folder, we have:</p>
  <ul>
    <li><strong>Polynomial.java</strong> is a class to define entered polynomials as its own object with properties</li>
    <li><strong>Pro5_khanah41.java</strong> is the driver class responsible for the text-based user interface</li>
    <li><strong>SDArmijo.java</strong> finds the optimal algorithm step sizes using the Armijo line search method</li>
    <li><strong>SDFixed.java</strong> sets and returns a single algorithm step size by way of a fixed value</li>
    <li><strong>SDGSS.java</strong> gets the optimal algorithm step sizes using the golden section search method</li>
    <li><strong>SteepestDescent.java</strong> consists of the logic for executing the main algorithm and other methods</li>
  </ul>

  <h2>:arrow_down: Installation</h2>
  <p>
    This application can be executed via the command terminal by switching to the correct directory and running <strong>Pro5_khanah41.class</strong>
    from the <strong>bin</strong> folder through the designated command, for which there are plenty of guides online. If you have an accredited
    Java IDE such as Eclipse or IntelliJ, you can simply open or copy and paste the files and run the program there instead.
  </p>
  
  <h2>:warning: Disclaimers</h2>
  <p>This application does not implement the Steepest Descent algorithm for all functions, rather only polynomials of the form:</p>
  <h3>
    <em>f(x) = a<sub>n</sub> x<sup>n</sup> + a<sub>n-1</sub> x<sup>n-1</sup> + ... + a<sub>2</sub> x<sup>2</sup> + a<sub>1</sub> x + a<sub>0</sub></em>
  </h3>
  <p>
    <em>More to be added</em>
  </p>
  <h2></h2>
  <h3>:chart_with_upwards_trend::chart_with_downwards_trend: Enjoy :chart_with_upwards_trend::chart_with_downwards_trend:</h3>
</body>
</html>
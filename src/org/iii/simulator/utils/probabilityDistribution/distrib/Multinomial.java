package org.iii.simulator.utils.probabilityDistribution.distrib;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.distribution.BinomialDistribution;

/**
 * The multinomial distribution accepts two parameters, an integer
 * <code>N</code> representing the number of trials, and <code>P</code>, an
 * unnormalized column vector representing the unnormalized probabilities for
 * each of the categories.
 *
 * See https://en.wikipedia.org/wiki/Multinomial_distribution
 *
 * @author cgioia
 * @since June 17, 2014
 */
public class Multinomial implements CondProbDistrib {

  /**
   * set parameters for multinomial distribution.
   *
   * @param params
   *          an array of the form [Integer, MatrixLib]
   *          <ul>
   *          <li>params[0]: <code>N</code>, number of trials (Integer)</li>
   *          <li>params[1]: <code>P</code>, probability of success for each
   *          category (MatrixLib, column vector)</li>
   *          </ul>
   *
   * @see blog.distrib.CondProbDistrib#setParams(java.util.List)
   */
  @Override
  public void setParams(Object[] params) {
    if (params.length != 2) {
      throw new IllegalArgumentException("expected two parameters");
    }
    setParams((Number) params[0], (MatrixLib) params[1]);
  }

  /**
   * If method parameter n is non-null, then attempt to set distribution
   * parameter <code>N</code> to n if n is a nonnegative integer.
   * If method parameter p is non-null, then set distribution parameter
   * <code>P</code> to method parameter p.
   * 
   * @param n
   *          distribution parameter <code>N</code>, representing the number of
   *          categories. Must be nonnegative. Can be set multiple times.
   * 
   * @param p
   *          distribution parameter <code>P</code>, representing the column
   *          vector of unnormalized probabilities.
   */
  public void setParams(Number n, MatrixLib p) {
    if (n != null) {
      if (n.intValue() < 0) {
        throw new IllegalArgumentException(
            "The number of trials 'n' for a Multinomial Distribution must be nonnegative");
      }
      this.n = n.intValue();
      this.hasN = true;
    }
    if (p != null) {
      if (p.numCols() != 1 || p.numRows() == 0) {
        throw new IllegalArgumentException(
            "The vector p passed in is not a column vector");
      }
      initializeProbabilityVector(p);
      this.hasP = true;
    }
    this.finiteSupport = null;
  }

  /**
   * Precondition: p is a column vector
   * 
   * Sets instance variable p to a normalized array of probabilities
   * Sets pCDF to the CDF of p
   * 
   * @param p
   */
  private void initializeProbabilityVector(MatrixLib p) {
    double sum = 0.0;
    for (int i = 0; i < p.numRows(); i++) {
      double ele = p.elementAt(i, 0);
      if (ele < 0) {
        throw new IllegalArgumentException("Probability " + ele
            + " for element " + i + " is negative.");
      }
      sum += ele;
    }
    if (sum < 1e-9) {
      throw new IllegalArgumentException("Probabilities sum to approx zero");
    }
    this.k = p.numRows();
    this.p = new double[k];
    this.pCDF = new double[k];
    this.p[0] = p.elementAt(0, 0) / sum;
    this.pCDF[0] = this.p[0];
    for (int i = 1; i < p.numRows(); i++) {
      this.p[i] = p.elementAt(i, 0) / sum;
      this.pCDF[i] = pCDF[i - 1] + this.p[i];
    }
  }

  private void checkHasParams() {
    if (!this.hasN) {
      throw new IllegalArgumentException("parameter N not provided");
    }
    if (!this.hasP) {
      throw new IllegalArgumentException("parameter P not provided");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see blog.distrib.CondProbDistrib#getProb(java.lang.Object)
   */
  @Override
  public double getProb(Object value) {
    return getProb((ArrayList<Integer>) value);
  }

  /**
   * Returns the probability of value outcomes, where value is a row vector
   * representing the number of times each category occurred.
   * 
   * @param value
   */
  public double getProb(ArrayList<Integer> value) {
    checkHasParams();
    if (!inSupport(value)) {
      return 0.0;
    }
    double prob = Util.factorial(n);
    for (int i = 0; i < k; i++) {
      prob *= Math.pow(p[i], value.get(i));
      prob /= Util.factorial((int) Math.round(value.get(i)));
    }
    return prob;
  }

  /**
   * Returns whether or not the row vector <code>value</code> is a possible
   * combination of occurrences for this multinomial distribution. In other
   * words, returns true iff <code>value</code> is in the support of this
   * multinomial distribution. Refer to
   * http://en.wikipedia.org/wiki/Multinomial_distribution.
   * 
   * @throws IllegalArgumentException
   *           if value is not a row vector of the correct dimension (1 by k)
   */
  private boolean inSupport(ArrayList<Integer> value) {
    if (value.size() != k) {
      throw new IllegalArgumentException(
          "The value provided is of the incorrect dimensions. Expecting a "
              + this.k + " Integer array but instead got a " + value.size()
              + " array ");
    }
    int sum = 0;
    for (int i = 0; i < k; i++) {
      int element = value.get(i);
      if (element < 0) {
        // Number of successes for a particular category is negative
        return false;
      }
      sum += element;
    }
    if (sum != n) {
      return false; // N != the sum of values
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see blog.distrib.CondProbDistrib#getLogProb(java.lang.Object)
   */
  @Override
  public double getLogProb(Object value) {
    return getLogProb((ArrayList<Integer>) value);
  }

  /**
   * Returns the log probability of value outcomes, where value is a row vector
   * representing the number of times each category occurred.
   * 
   * @param value
   */
  public double getLogProb(ArrayList<Integer> value) {
    checkHasParams();
    if (!inSupport(value)) {
      return Double.NEGATIVE_INFINITY;
    }
    double logProb = Util.logFactorial(n);
    for (int i = 0; i < k; i++) {
      logProb += value.get(i) * Math.log(p[i]);
      logProb -= Util.logFactorial((int) Math.round(value.get(i)));
    }
    return logProb;
  }

  /*
   * (non-Javadoc)
   * 
   * @see blog.distrib.CondProbDistrib#sampleVal()
   */
  @Override
  public Object sampleVal() {
    // Modified by yiwu on Oct.8.2014.
    return sample_value();
  }

  /** Samples a value from the multinomial. */
  public ArrayList<Integer> sample_value() {
    checkHasParams();
    if (n > 3 * k) // Currently it is a heuristic.
      return sample_value_use_binomial();
    else
      return sample_value_use_bsearch();
  }

  /** Samples a value from the multinomial. */
  private ArrayList<Integer> sample_value_use_bsearch() {
    ArrayList<Integer> result = new ArrayList<Integer>(k);
    for (int i = 0; i < k; i++) {
      result.add(0);
    }

    for (int trial = 0; trial < n; trial++) {
      double val = Util.random();
      int bucket = Arrays.binarySearch(pCDF, val);
      if (bucket < 0)
        bucket = -bucket - 1;
      result.set(bucket, result.get(bucket) + 1);
    }
    return result;
  }

  /** Sample value from Multinomial Distribution using Binomial Distribution */
  /*
   * Authored by yiwu on Oct.8.2014
   * reference:
   * chapter 2.2 of
   * http://www.sciencedirect.com/science/article/pii/016794739390115A
   */
  private ArrayList<Integer> sample_value_use_binomial() {
    BinomialDistribution binom = null;
    int cur = 0;
    double cdf = 0;
    ArrayList<Integer> result = new ArrayList<Integer>(k);
    for (int i = 0; i < k - 1; i++) {
      if (n == cur) {
        result.add(0);
        continue;
      }
      binom = new BinomialDistribution(n - cur, p[i] / (1.0 - cdf));
      int x = binom.sample();
      cur += x;
      cdf += p[i];
      result.add(x);
    }
    result.add(n - cur);
    return result;
  }

  @Override
  public Object[] getFiniteSupport() {
    if (finiteSupport == null) {
      checkHasParams();
      int kPos = 0;
      for (int i = 0; i < p.length; i++) {
        if (!Util.closeToZero(this.p[i])) {
          kPos++;
        }
      }
      finiteSupport = new Object[Util.multichoose(kPos, n)];
      ArrayList<Integer> currentList = new ArrayList<Integer>();
      supportNum = 0;
      calculateFiniteSupport(currentList, 0, n);
    }
    return finiteSupport;
  }

  private void calculateFiniteSupport(ArrayList<Integer> currentList,
      int depth, int remain) {
    if (depth == k) {
      finiteSupport[supportNum] = currentList.clone();
      supportNum++;
    } else if (depth == k - 1) {
      if (remain == 0) {
        currentList.add(0);
        calculateFiniteSupport(currentList, depth + 1, 0);
        currentList.remove(depth);
      } else if (!Util.closeToZero(p[depth])) {
        currentList.add(remain);
        calculateFiniteSupport(currentList, depth + 1, 0);
        currentList.remove(depth);
      }
    } else {
      currentList.add(0);
      calculateFiniteSupport(currentList, depth + 1, remain);
      currentList.remove(depth);
      if (!Util.closeToZero(p[depth])) {
        for (int i = 1; i <= remain; i++) {
          currentList.add(i);
          calculateFiniteSupport(currentList, depth + 1, remain - i);
          currentList.remove(depth);
        }
      }
    }
  }

  private int n; // the number of trials
  private boolean hasN;
  private double[] p; // probability vector
  private double[] pCDF;
  private boolean hasP;
  private int k; // the number of categories; dimension of p
  private int supportNum;
  private Object[] finiteSupport = null;
}

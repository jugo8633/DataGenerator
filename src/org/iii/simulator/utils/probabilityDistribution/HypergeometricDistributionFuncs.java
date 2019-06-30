package org.iii.simulator.utils.probabilityDistribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

import java.util.Arrays;

public class HypergeometricDistributionFuncs extends HypergeometricDistribution {

    public HypergeometricDistributionFuncs(int populationSize, int numberOfSuccesses, int sampleSize) throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        super(populationSize, numberOfSuccesses, sampleSize);
    }
    public int[] HypergeometricRandomGenerator(int r, int N, int pn, int n){
        HypergeometricDistribution hypergeometricRG = new HypergeometricDistributionFuncs(N,pn, n);
        return hypergeometricRG.sample(r);
    }
//    public static void main(String[] args){
//        System.out.println(Arrays.toString(HypergeometricRandomGenerator(20,20,2,5)));
//    }

}

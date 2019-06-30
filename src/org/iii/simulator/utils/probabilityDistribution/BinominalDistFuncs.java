package org.iii.simulator.utils.probabilityDistribution;

import org.apache.commons.math3.distribution.BinomialDistribution;


public class BinominalDistFuncs extends BinomialDistribution
{
    
    public BinominalDistFuncs(int trials, double p)
    {
        super(trials, p);
    }
    
    public int[] getSample(int r)
    {
        //BinominalDistFuncs binominalRG = new BinominalDistFuncs(n, p);
        return sample(r);
    }
    
}

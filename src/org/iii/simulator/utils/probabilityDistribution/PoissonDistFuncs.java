package org.iii.simulator.utils.probabilityDistribution;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class PoissonDistFuncs extends PoissonDistribution
{
    public PoissonDistFuncs(double p) throws NotStrictlyPositiveException
    {
        super(p);
    }
    
    public int[] getSample(int r)
    {
        return sample(r);
    }
}

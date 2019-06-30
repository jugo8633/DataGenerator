package org.iii.simulator.utils.probabilityDistribution;

import org.apache.commons.math3.distribution.ParetoDistribution;

import java.util.ArrayList;
import java.util.Arrays;

public class ParetoDistFuncs extends ParetoDistribution
{
    public double[] getSample(int r)
    {
        return sample(r);
    }
    
}

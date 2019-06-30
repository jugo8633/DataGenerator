package org.iii.simulator.utils.probabilityDistribution;

import org.iii.simulator.utils.probabilityDistribution.distrib.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultinominalDistributionFuncs
{
    public Object multinominalRandomGenerator(int r, int n, double[] p)
    {
        Multinomial distribMultiNominal = new Multinomial();
        Util.initRandom(true); //設定是否產生隨機結果
        multinominalProbSetting mbProbSetting = new multinominalProbSetting(); //初始化機率矩陣
//            double[] probVector = {0.2,0.2,0.2,0.2,0.2}; //注意機率加總要等於1
        mbProbSetting.setElement(n, 1, p); //x的位置控制矩陣向量長度(目前放p的長度)
        distribMultiNominal.setParams(n, mbProbSetting); //n控制生成總個數
        ArrayList randomValueResult = (ArrayList) distribMultiNominal.sampleVal();
//        System.out.println("randomValueResult: " + randomValueResult);

        /*
        針對multinominal distribution random的結果轉成樣本點
         */
        ArrayList randomValueResultToSampleData = new ArrayList();
        for (int i = 1; i <= p.length; i++)
        {
//            System.out.println(i);
//            System.out.println(randomValueResult.get(i-1));
            int mbValue = (int) randomValueResult.get(i - 1);
            for (int j = 0; j <= mbValue; j++)
            {
                if (j != 0)
                {
                    randomValueResultToSampleData.add(i - 1);
                }
            }
//            System.out.println(randomValueResultToSampleData);
        }
        Collections.shuffle((List<?>) randomValueResultToSampleData);
        //針對randomValueResultToSampleData shuffle重組

        /*
        針對不同的r進行抽取
         */
        ArrayList allRandomValueResultRev = new ArrayList<>();
        for (int i = 0; i <= r - 1; i++)
        {
            allRandomValueResultRev.add(randomValueResultToSampleData.get(i));
        }
//        System.out.println("allRandomValueResult: " + allRandomValueResultRev);
        
        return allRandomValueResultRev;
    }
}

package org.iii.simulator.generator;

import java.util.Random;

/**
 * Created by Jugo on 2019/4/15
 * <p>
 * 第一位為英文字母
 * 第二個數字是男女生之分，男生為 1，女生為 2
 * <p>
 * 身份證字號後面八個數字之中，前面七個可以隨便打
 * 最後一位為檢查碼，必須經過之前一個字母與8個數字的組合計算後得出
 * <p>
 * 檢查碼的運算原則：
 * <p>
 * 英文代號以下表轉換成數字
 * A=10  台北市       J=18 新竹縣         S=26  高雄縣
 * B=11  台中市       K=19 苗栗縣         T=27  屏東縣
 * C=12  基隆市       L=20 台中縣         U=28  花蓮縣
 * D=13  台南市       M=21 南投縣         V=29  台東縣
 * E=14  高雄市       N=22 彰化縣         W=32  金門縣
 * F=15  台北縣       O=35 新竹市         X=30  澎湖縣
 * G=16  宜蘭縣       P=23 雲林縣         Y=31  陽明山
 * H=17  桃園縣       Q=24 嘉義縣         Z=33  連江縣
 * I=34  嘉義市       R=25 台南縣
 * <p>
 * (1)英文轉成的數字, 個位數乘９再加上十位數
 * (2)各數字從右到左依次乘１、２、３、４．．．．８
 * <p>
 * (1)與(2)的和，除10求出餘數
 * 用10減該餘數，結果就是檢查碼，若餘數為0，檢查碼就是 0。
 * <p>
 * 例如: 身分證號碼是 Ｚ１２３４５６７８？
 * <p>
 * Ｚ　１　２　３　４　５　６　７　８
 * 　　 　３　３
 * Ｘ  Ｘ  Ｘ  Ｘ Ｘ  Ｘ  Ｘ  Ｘ  Ｘ Ｘ
 * １  ９  ８　７　６　５　４  ３  ２  １
 * 　─────────────────────
 * 3 + 27 + 8 + 14 + 18 + 20 + 20 + 18 +14 + 8 = 150
 * 150/10=15....0 (餘數)
 * 10-0=10 (檢查碼為0)
 * ∴身份證字號為Z123456780
 */

public class IDMaker
{
    private final static String checkHead = "ABCDEFGHJKLMNPQRSTUVWXYZIO";
    private int m_nSex = 0;
    
    public IDMaker()
    {
        init();
    }
    
    /**
     * @param nSex 男生為 1，女生為 2
     */
    public void setSex(int nSex)
    {
        m_nSex = nSex;
        if (1 != nSex && 2 != nSex)
        {
            m_nSex = 0;
        }
        
    }
    
    public void setSex(String strSex)
    {
        m_nSex = 0;
        if (null != strSex && 0 == strSex.compareTo("男"))
        {
            m_nSex = 1;
        }
        if (null != strSex && 0 == strSex.compareTo("女"))
        {
            m_nSex = 2;
        }
    }
    
    public String getSex()
    {
        if (1 == m_nSex)
        {
            return "男";
        }
        return "女";
    }
    
    public IDMaker init()
    {
        m_nSex = 0;
        return this;
    }
    
    public String random()
    {
        Random r = new Random();
        StringBuilder strId = new StringBuilder();
        int checknum = 0;    // 產生前9碼的同時計算產生驗證碼
        
        // 產生第一個英文字母
        int t = (r.nextInt(26) + 65);
        strId.append((char) t);
        t = checkHead.indexOf((char) t) + 10;
        checknum += t / 10;
        checknum += t % 10 * 9;
        
        // 產生第2個數字 (1~2)
        if (0 == m_nSex)
        {
            strId.append(Integer.toString(m_nSex = t = r.nextInt(2) + 1));
        }
        else
        {
            strId.append(Integer.toString(t = m_nSex));
        }
        checknum += t * 8;
        
        // 產生後8碼
        for (int i = 2; i < 9; i++)
        {
            strId.append(Integer.toString(t = r.nextInt(10)));
            checknum += t * (9 - i);
        }
        
        // 完成驗證碼計算
        checknum = (10 - ((checknum) % 10)) % 10;
        strId.append(Integer.toString(checknum));
        return (strId.toString());
    }
}

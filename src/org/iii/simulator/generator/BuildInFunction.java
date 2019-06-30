package org.iii.simulator.generator;

import org.iii.simulator.utils.probabilityDistribution.BinominalDistFuncs;
import org.iii.simulator.utils.probabilityDistribution.MultinominalDistributionFuncs;
import org.iii.simulator.utils.probabilityDistribution.ParetoDistFuncs;
import org.iii.simulator.utils.probabilityDistribution.PoissonDistFuncs;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author Y.C Tsai
 * @project dataGenerator
 * @Date 2019/6/10
 */
public class BuildInFunction
{
    //data members
    private int surNameIndex = 0;
    private int name1Index = 0;
    private int name2Index = 0;
    private Random random = new Random();
    private ArrayList<String> listMobilePhone = new ArrayList<String>();
    
    //function members
    public String strSurname()
    {
        surNameIndex = random.nextInt(CharacterInformation.surname.length - 1);
        return CharacterInformation.surname[surNameIndex];
    }
    
    public String strFirstName()
    {
        name1Index = random.nextInt(CharacterInformation.word.length - 1);
        name2Index = random.nextInt(CharacterInformation.word.length - 1);
        return (CharacterInformation.word[name1Index] + CharacterInformation.word[name2Index]);
    }
    
    public String strChineseName()
    {
        return String.format("%s%s", strSurname(), strFirstName());
    }
    
    public String strEnglishName()
    {
        String surname;
        String name1;
        String name2;
        String englishName;
        
        if (0 != surNameIndex)
        {
            surname = CharacterInformation.esurname[surNameIndex];
            surNameIndex = 0;
        }
        else
        {
            surname =
                    CharacterInformation.esurname[random.nextInt(CharacterInformation.esurname.length - 1)];
        }
        
        if (0 != name1Index)
        {
            name1 = CharacterInformation.eword[name1Index];
        }
        else
        {
            name1 = CharacterInformation.eword[random.nextInt(CharacterInformation.eword.length - 1)];
        }
        
        if (0 != name2Index)
        {
            name2 = CharacterInformation.eword[name2Index];
        }
        else
        {
            name2 = CharacterInformation.eword[random.nextInt(CharacterInformation.eword.length - 1)];
        }
        
        return String.format("%s %s %s", name1, name2, surname);
    }
    
    public String strEmail()
    {
        String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890";
        int nLen = random.nextInt(9) + 1;
        while (5 > nLen)
        {
            nLen = nLen * 2;
        }
        
        int nSize = 0;
        StringBuilder salt = new StringBuilder();
        while (nSize < nLen)
        {
            salt.append(allowedChars.charAt(random.nextInt(allowedChars.length() - 1)));
            ++nSize;
        }
        
        String saltStr = salt.toString();
        return (saltStr + "@" + CharacterInformation.email[random.nextInt(CharacterInformation.email.length - 1)]);
        
    }
    
    public String strCompany()
    {
        return (CharacterInformation.company[random.nextInt(CharacterInformation.company.length - 1)]);
    }
    
    public String strEnglistNameMan()
    {
        return CharacterInformation.enameboy[random.nextInt(CharacterInformation.enameboy.length - 1)];
    }
    
    public String strEnglistNameWomen()
    {
        return CharacterInformation.enamegirl[random.nextInt(CharacterInformation.enamegirl.length - 1)];
    }
    
    public String strCompanyPhone()
    {
        String strNumber = "1234567890";
        int nSize = 0;
        StringBuilder salt = new StringBuilder();
        while (nSize < 8)
        {
            salt.append(strNumber.charAt(random.nextInt(strNumber.length() - 1)));
            ++nSize;
        }
        return salt.toString();
    }
    
    public String strPhone(Integer nType, Integer nLocal)
    {
        String strResult;
        String strNumber = "1234567890";
        int nSize = 0;
        StringBuilder salt = new StringBuilder();
        int local = nLocal;
        if (0 == local)
        {
            local = random.nextInt(6) + 2;
        }
        
        while (nSize < 8)
        {
            salt.append(strNumber.charAt(random.nextInt(strNumber.length() - 1)));
            ++nSize;
        }
        switch (nType)
        {
            case 0:
                strResult = String.format("%02d-%s", local, salt.toString());
                break;
            case 1:
                strResult = String.format("(%02d)%s", local, salt.toString());
                break;
            default:
                strResult = String.format("%s", salt.toString());
                break;
        }
        return strResult;
    }
    
    /**
     * n=4,p1=0.9,p2=0.06,p3=0.02,p4=0.02
     * 電梯大廈	90%
     * 透天	6%
     * 套房	2%
     * 其他	2%
     *
     * @return
     */
    public String strBuilding()
    {
        String strResult;
        double[] p = {0.9, 0.06, 0.02, 0.02};
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1, 4,
                        p);
        int nIndex = (int) result.get(0);
        switch (nIndex)
        {
            case 0:
                strResult = "電梯大廈";
                break;
            case 1:
                strResult = "透天";
                break;
            case 2:
                strResult = "套房";
                break;
            default:
                strResult = "其他";
                break;
        }
        
        return strResult;
    }
    
    /**
     * n=16,p1=0.2,p2=0.12,p3=0.07,p4=0.1,p5=0.05,p6=0.09,p7=0.03,p8=0.08,p9=0.07,p10=0.06,p11=0
     * .01,p12=0.04,p13=0.03,p14=0.02,p15=0.01,p16=0.02
     * 軍公教	20%
     * 政府／公營事業	12%
     * 軍／警／消防	7%
     * 學校／補習班	10%
     * 醫療／專業事務所	5%
     * 金融／保險	9%
     * 製造／建築	3%
     * 資訊／科技	8%
     * 貿易／銷售	7%
     * 服務／傳播	6%
     * 無業	1%
     * 退休及家管	4%
     * 批發零售業	3%
     * 學生	2%
     * 其他	1%
     * 自由業	2%
     *
     * @return
     */
    final String[] jobs = {"軍公教", "政府／公營事業", "軍／警／消防", "學校／補習班", "醫療／專業事務所", "金融／保險", "製造／建築",
            "資訊／科技", "貿易／銷售", "服務／傳播", "無業", "退休及家管", "批發零售業", "學生", "其他", "自由業"};
    
    public String strJob()
    {
        String strResult;
        double[] p = {0.2, 0.12, 0.07, 0.1, 0.05, 0.09, 0.03, 0.08, 0.07, 0.06, 0.01, 0.04, 0.03,
                0.02, 0.01, 0.02};
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1, 16
                        , p);
        
        int nIndex = (int) result.get(0);
        if (0 > nIndex || jobs.length <= nIndex)
        {
            nIndex = 14;
        }
        
        return jobs[nIndex];
    }
    
    /**
     * n=8,p1=0.01,p2=0.01,p3=0.1,p4=0.5,p5=0.3,p6=0.04,p7=0.01,p8=0.03
     * 大型企業負責人/董監事	1%
     * 中小型企業負責人/董監事	1%
     * 主管	10%
     * 一般職員	50%
     * 佣金收入者	30%
     * 專業人員	4%
     * 學生	1%
     * 自營業主	3%
     *
     * @return
     */
    private final double[] jobTitleP = {0.01, 0.01, 0.1, 0.5, 0.3, 0.04, 0.01, 0.03};
    private final String[] jobTitleS = {"大型企業負責人/董監事", "中小型企業負責人/董監事", "主管", "一般職員", "佣金收入者",
            "專業人員", "學生", "自營業主"};
    
    public String strJobTitle()
    {
        return MultinominalDistribution(jobTitleP, jobTitleS);
    }
    
    public String MultinominalDistribution(double[] p, String[] strItemName)
    {
        String strResult;
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1,
                        p.length, p);
        int nIndex = (int) result.get(0);
        if (0 > nIndex || strItemName.length <= nIndex)
        {
            nIndex = 0;
        }
        return strItemName[nIndex];
    }
    
    public String MultinominalDistribution(ArrayList<MultinomialData> listMD)
    {
        String strResult;
        int nIndex;
        double[] p = new double[listMD.size()];
        for (nIndex = 0; nIndex < listMD.size(); ++nIndex)
        {
            p[nIndex] = listMD.get(nIndex).probability;
        }
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1,
                        p.length, p);
        nIndex = (int) result.get(0);
        if (0 > nIndex || listMD.size() <= nIndex)
        {
            nIndex = 0;
        }
        
        return listMD.get(nIndex).itemName;
    }
    
    /**
     * n=10,p1(null)=0.5,p2~p10=0.055
     * 購物(0.055)/親友轉帳(0.055)/還款(0.055)/房租(0.055)/卡費(0.055)/保費(0.055)/生活(水電瓦斯電費電信)(0.055)/投資(0
     * .055)/其它(0.055)/null(0.5)
     *
     * @return
     */
    private final double[] transferNoteP = {0.055, 0.055, 0.055, 0.055, 0.055, 0.055, 0.055,
            0.055, 0.055, 0.5};
    private final String[] transferNoteS = {"購物", "親友轉帳", "還款", "房租", "卡費", "保費", "生活" +
            "(水電瓦斯電費電信)", "投資", "其它"};
    
    public String strTransferNote()
    {
        return MultinominalDistribution(transferNoteP, transferNoteS);
    }
    
    /**
     * MCC 8碼 前4制式 後4流水
     *
     * @return
     */
    public String strMCC()
    {
        String strPreCode =
                CharacterInformation.MCC[random.nextInt(CharacterInformation.MCC.length - 1)];
        String strBackCode = String.format("%04d", random.nextInt(9999));
        return (strPreCode + strBackCode);
    }
    
    /**
     * n=4,p1=0.25,p2=0.25,p3=0.25,p4=0.25
     * 網路交易	25%
     * 網路特店交易	25%
     * 特店交易	25%
     * 一般交易	25%
     *
     * @return
     */
    private final double[] saleTypeP = {0.25, 0.25, 0.25, 0.25};
    private final String[] saleTypeS = {"網路交易", "網路特店交易", "特店交易", "一般交易"};
    
    public String strSaleType()
    {
        return MultinominalDistribution(saleTypeP, saleTypeS);
    }
    
    /**
     * n=4,p1=0.6,p2=0.15,p3=0.15,p4=0.1
     * 0-100, avg25%
     * 0-15(0.6),15-30(0.15),30-45(0.15),45-60(0.1)
     *
     * @return
     */
    private final double[] buildP = {0.6, 0.15, 0.15, 0.1};
    private final String[] buildS = {"0-15", "15-30", "30-45", "45-60"};
    
    public String strBuildingInch()
    {
        return MultinominalDistribution(buildP, buildS);
    }
    
    /**
     * n=8,p1=0.2,p2=0.5,p3=0.1,p4=0.06,p5=0.05,p6=0.04,p7=0.03,p8=0.02
     * 0-40, 10高峰, 偏左常態分布
     * 0-5(0.2),5-10(0.5),10-15(0.1),15-20(0.06),20-25(0.05),25-30(0.04),30-35(0.03),35-40(0.02)
     *
     * @return
     */
    private final double[] CustomHisP = {0.2, 0.5, 0.1, 0.06, 0.05, 0.04, 0.03, 0.02};
    private final String[] CustomHisS = {"0-5", "5-10", "10-15", "15-20", "20-25", "25-30",
            "30" + "-35", "35-40"};
    
    public String strCustomHis()
    {
        return MultinominalDistribution(buildP, buildS);
    }
    
    private int ParetoDist(int nCount, int nMax)
    {
        int nResult = 0;
        double[] resultValue = new ParetoDistFuncs().getSample(1);
        ArrayList tmpResult = new ArrayList();
        for (double i : resultValue)
        {
            tmpResult.add(Math.exp(i) * nCount);
        }
        
        Number number = (double) tmpResult.get(0);
        nResult = number.intValue();
        while (nResult > nMax)
        {
            nResult = nResult / 10;
        }
        
        return nResult;
    }
    
    private Object PoissonDist(int lambda, boolean ispercent)
    {
        int nResult = 0;
        int[] testResult = new PoissonDistFuncs(lambda).getSample(1);
        
        if (ispercent)
        {
            double[] altest = new double[testResult.length];
            for (int i = 0; i < testResult.length; ++i)
            {
                altest[i] = (double) testResult[i] / 10;
            }
            return altest[0];
        }
        return testResult[0];
    }
    
    /**
     * YYYY/MM/DD(char(32))HH:MM(char(32))12345TWD
     *
     * @return "2018-01-25 19:50 12345TWD"
     */
    public String strShopingInfo()
    {
        Calendar calendar = Calendar.getInstance();
        return String.format("%d-%02d-%02d %02d:%02d %dTWD", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                ParetoDist(100000, 100000000));
    }
    
    /**
     * 存款金額
     * min 10000
     * 年收平均數60w
     *
     * @return "125365912345TWD"
     */
    public String strSaveMoney()
    {
        return String.format("%d", ParetoDist(100000, 100000000));
    }
    
    /**
     * alpha=?
     * min 10000, 年收平均數60w,
     *
     * @return
     */
    public String strIncome()
    {
        return String.format("%d", ParetoDist(100000, 100000000));
    }
    
    /**
     * 使用ATM次數
     * 0-100, 0 發生次數很高(95% 0-3), 0-inflated巴松分布
     *
     * @return
     */
    public String strATM()
    {
        return String.format("%d", PoissonDist(1, false));
    }
    
    /**
     * 三個月登入網銀次數
     * 0-90, 0:發生次數很高, 0-inflated巴松分布
     *
     * @return
     */
    private int m_nLoginBankTime = 0;
    
    public String strLoginBank()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.95);
        if (1 == binominalDistFuncs.getSample(1)[0])
        {
            m_nLoginBankTime = 0;
        }
        else
        {
            m_nLoginBankTime = random.nextInt(89) + 1;
        }
        return String.format("%d", m_nLoginBankTime);
    }
    
    /**
     * @return "2018/4/2 08:15:55 AM"
     */
    public String strLoginBankWebTime()
    {
        
        String strTimeZ = "AM";
        Calendar calendar = Calendar.getInstance();
        int nYear = calendar.get(Calendar.YEAR);
        if (0 == m_nLoginBankTime)
        {
            nYear = nYear - 2;
        }
        int nMonth = random.nextInt(12);
        if (0 == nMonth)
        {
            nMonth = 2;
        }
        int nDay;
        if (2 == nMonth)
        {
            nDay = random.nextInt(28);
        }
        else
        {
            nDay = random.nextInt(30);
        }
        
        int nHour = random.nextInt(23);
        if (12 <= nHour)
        {
            strTimeZ = "PM";
            nHour -= nHour;
        }
        
        int nMinute = random.nextInt(59);
        int nSecond = random.nextInt(59);
        
        return String.format("%d/%02d/%02d %02d:%02d:%02d %s", nYear, nMonth, nDay, nHour,
                nMinute, nSecond, strTimeZ);
    }
    
    /**
     * n=7,p1=0.9,p2=0.08,p3~p7=0.04
     * 1(0.9)2(0.08),3~7(0.04)
     *
     * @return
     */
    private final double[] categoricalP = {0.9, 0.08, 0.04, 0.04, 0.04, 0.04, 0.04};
    private final String[] categoricalS = {"1", "2", "3", "4", "5", "6", "7"};
    
    public String strCategorical()
    {
        return MultinominalDistribution(categoricalP, categoricalS);
    }
    
    /**
     * n=6,p1=0.4,p2=0.1,p3=0.4,p4=0.04,p5=0.04,p6=0.02
     * 分行	40%
     * 仲介	10%
     * 代書	40%
     * 建商	4%
     * 網銀	4%
     * 其他	2%
     *
     * @return
     */
    private final double[] comeFromP = {0.4, 0.1, 0.4, 0.04, 0.04, 0.02};
    private final String[] comeFromS = {"分行", "仲介", "代書", "建商", "網銀", "其他"};
    
    public String strComeFrom()
    {
        return MultinominalDistribution(comeFromP, comeFromS);
    }
    
    /**
     * n=5,p1=0.9,p2=0.05,p3=0.03,p4=0.015,p5=0.005
     * 正常還款(0.9), 延至30(0.05), 60(0.03), 90(0.015), 跳票(0.005), 依序大幅遞減
     *
     * @return
     */
    private final double[] badRecordP = {0.9, 0.05, 0.03, 0.015, 0.005};
    private final String[] badRecordS = {"正常還款", "延至30", "延至60", "延至90", "跳票"};
    
    public String strBadRecord()
    {
        return MultinominalDistribution(badRecordP, badRecordS);
    }
    
    public String strCountry()
    {
        return (CharacterInformation.country[random.nextInt(CharacterInformation.country.length - 1)]);
    }
    
    public String strLocale()
    {
        Locale locale = Locale.getDefault();
        String s = locale.getDisplayName();
        return s;
    }
    
    public String strDelayDate()
    {
        int nAge = random.nextInt(3);
        
        Calendar calendar = Calendar.getInstance();
        int nYear = calendar.get(Calendar.YEAR) - nAge;
        int nMonth = random.nextInt(12);
        if (0 == nMonth)
        {
            nMonth = 2;
        }
        int nDay;
        if (2 == nMonth)
        {
            nDay = random.nextInt(28);
        }
        else
        {
            nDay = random.nextInt(30);
        }
        
        String strB = String.format("%d/%d/%d", nYear, nMonth, nDay);
        return strB;
    }
    
    public String strBirthday()
    {
        
        int nAge = random.nextInt(60);
        
        if (20 > nAge)
        {
            nAge = nAge + 18;
        }
        
        Calendar calendar = Calendar.getInstance();
        int nYear = calendar.get(Calendar.YEAR) - nAge;
        int nMonth = random.nextInt(12);
        if (0 == nMonth)
        {
            nMonth = 2;
        }
        int nDay;
        if (2 == nMonth)
        {
            nDay = random.nextInt(28);
        }
        else
        {
            nDay = random.nextInt(30);
        }
        
        String strB = String.format("%d/%d/%d", nYear, nMonth, nDay);
        return strB;
    }
    
    public String strTimeInMillis()
    {
        return String.format("%d", Calendar.getInstance().getTimeInMillis());
    }
    
    public String strTimeInFormat()
    {
        Calendar calendar = Calendar.getInstance();
        return String.format("%d%02d%02d%02d%02d%02d", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }
    
    
    public String strStreet()
    {
        String strRecord = "台北市民生東路四段133號";
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);
        
        SQLiteDataSource ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:database/streetname.db");
        
        try
        {
            Connection con = ds.getConnection();
            String sql = "SELECT * FROM streetname ORDER BY RANDOM() LIMIT 1";
            Statement stat = null;
            ResultSet rs = null;
            stat = con.createStatement();
            rs = stat.executeQuery(sql);
            if (rs.next())
            {
                strRecord = rs.getString("city") + rs.getString("country") + rs.getString("road");
            }
            con.close();
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return strRecord;
    }
    
    public void loadStreet(ArrayList<String> listStreet)
    {
        String strRecord = "台北市民生東路四段133號";
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);
        listStreet.clear();
        
        SQLiteDataSource ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:database/streetname.db");
        
        try
        {
            Connection con = ds.getConnection();
            String sql = "SELECT * FROM streetname ORDER BY RANDOM()";
            Statement stat = null;
            ResultSet rs = null;
            stat = con.createStatement();
            rs = stat.executeQuery(sql);
            
            while (rs.next())
            {
                strRecord = rs.getString("city") + rs.getString("country") + rs.getString("road");
                listStreet.add(strRecord);
            }
            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    private int MobilePhoneCount = 0;
    
    public String strMobilePhone(Integer nType)
    {
        String strNumber = "1234567890";
        String strPrex =
                CharacterInformation.mobile[random.nextInt(CharacterInformation.mobile.length - 1)];
        String strResult = "";
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 6; ++i)
        {
            salt.append(strNumber.charAt(random.nextInt(strNumber.length() - 1)));
        }
        
        switch (nType)
        {
            case 1:
                strResult = "886" + (strPrex + salt.toString()).substring(1); // 886912345678
                break;
            case 2:
            case 4:
                strResult = String.format("%s-%s-%s", strPrex, salt.substring(0, 3),
                        salt.substring(3, 6));
                break;
            case 3:
                if (MobilePhoneCount >= listMobilePhone.size())
                {
                    MobilePhoneCount = 0;
                }
                if (0 <= MobilePhoneCount && MobilePhoneCount < listMobilePhone.size())
                {
                    strResult = listMobilePhone.get(MobilePhoneCount++);
                }
                break;
            default:
                strResult = strPrex + salt.toString();
        }
        if (3 != nType && 4 != nType)
        {
            listMobilePhone.add(strResult);
        }
        return strResult;
    }
    
    public String strUniform()
    {
        String strNumber = "1234567890";
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 8; ++i)
        {
            salt.append(strNumber.charAt(random.nextInt(strNumber.length() - 1)));
        }
        return salt.toString();
    }
    
    public String strUUID(Integer nType)
    {
        String s = UUID.randomUUID().toString();
        String strResult = s;
        switch (nType)
        {
            case 0:
                strResult = s;
                break;
            case 1: // 去掉 "-"
                strResult =
                        s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
                break;
        }
        
        return strResult;
    }
    
    public String strRegion()
    {
        String strResult = "TW";
        
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 1);
        if (1 == binominalDistFuncs.getSample(1)[0])
        {
            strResult = "TW";
        }
        else
        {
            strResult =
                    CharacterInformation.region[random.nextInt(CharacterInformation.region.length - 1)];
        }
        
        return strResult;
    }
    
    public String strTaiwanCity()
    {
        return CharacterInformation.taiwan_city[random.nextInt(CharacterInformation.taiwan_city.length - 1)];
    }
    
    public String strOS(Integer nType)
    {
        String strResult =
                CharacterInformation.os[random.nextInt(CharacterInformation.os.length - 1)];
        
        switch (nType)
        {
            case 0:
                strResult = "Android";
                break;
        }
        
        return strResult;
    }
    
    public Integer strOsVersion(Integer nType)
    {
        String strResult =
                CharacterInformation.os[random.nextInt(CharacterInformation.os.length - 1)];
        Integer randomOsVersion = null;
        switch (nType)
        {
            case 0:
                strResult = "Android";
                randomOsVersion = random.nextInt(((27 - 17) + 17));
                break;
        }
        return randomOsVersion;
    }
    
    public String strMobileModel(Integer nBrand)
    {
        String modelResult = null;
        switch (nBrand)
        {
            case 9:
                modelResult =
                        CharacterInformation.model[random.nextInt(CharacterInformation.model.length - 1)];
                break;
        }
        return modelResult;
    }
    
    /**
     * @param nType 資料產生的方式,0 表示只有"zh" 跟 "other", "zh"出現的機率為97％,1 為隨機抓取各國語系代碼
     * @return 回傳語系代碼
     */
    public String strLanguage(Integer nType)
    {
        String strResult;
        int npoint = 0;
        
        switch (nType)
        {
            case 0:
                strResult = "other";
                BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.97);
                npoint = binominalDistFuncs.getSample(1)[0];
                if (1 == npoint)
                {
                    strResult = "zh";
                }
                break;
            case 1:
                strResult =
                        CharacterInformation.language[random.nextInt(CharacterInformation.language.length - 1)];
                break;
            default:
                strResult = "";
                break;
        }
        return strResult;
    }
    
    public int intLoginStatus()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.7);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    public int subscriptStatus()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.012);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    public int isContact()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.8);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    public int isSpam()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.18);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    public String gender()
    {
        String genderResult = null;
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.6);
        int genderEncoding = binominalDistFuncs.getSample(1)[0];
        switch (genderEncoding)
        {
            case 0:
                genderResult = "M";
                break;
            case 1:
                genderResult = "F";
        }
        return genderResult;
    }
    
    /**
     * 手機廠牌 2018統計10大,
     * apple(25.3%) samsung(18.81%) asus(11.75%) oppo(10.27%) htc(6.36%) sony(6.17%)
     * huawei(3.99%) mi(3.95%) sugar(2.5%) nokia(2.28%) etc(8.62%)
     *
     * @return 手機廠牌
     */
    public String strManufacturer()
    {
        String strResult;
        double[] p = {0.253, 0.1881, 0.1175, 0.1027, 0.0636, 0.0617, 0.0399, 0.0395, 0.025, 0.028
                , 0.0862};
        MultinominalDistributionFuncs multinominalDistributionFuncs =
                new MultinominalDistributionFuncs();
        ArrayList result =
                (ArrayList) multinominalDistributionFuncs.multinominalRandomGenerator(1, 11, p);
        
        int nIndex = (int) result.get(0);
        switch (nIndex)
        {
            case 0:
                strResult = "apple";
                break;
            case 1:
                strResult = "samsung";
                break;
            case 2:
                strResult = "asus";
                break;
            case 3:
                strResult = "oppo";
                break;
            case 4:
                strResult = "htc";
                break;
            case 5:
                strResult = "sony";
                break;
            case 6:
                strResult = "huawei";
                break;
            case 7:
                strResult = "mi";
                break;
            case 8:
                strResult = "sugar";
                break;
            case 9:
                strResult = "nokia";
                break;
            default:
                strResult = "etc";
                break;
            
        }
        return strResult;
    }
    
    /**
     * n=22;p1=0.12,p2=0.65,p3=0.07,p4~p20=0
     * food(0.12),activity(0.65),beauty(0.07),life(0),health(0),travel(0),entertainmean(0),
     * automobile(0),traffic(0),professional(0),bank(0),government(0),politics(0),organization(0)
     * ,pet(0),logistic(0),media(0),thers(0),null(0),personal(0),publicperson(0),shopping(0)
     *
     * @return string
     */
    public String bizCategory()
    {
        String strResult;
        double[] p = {0.12, 0.65, 0.07};
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1, 3,
                        p);
        int nIndex = (int) result.get(0);
        switch (nIndex)
        {
            case 0:
                strResult = "food";
                break;
            case 1:
                strResult = "activity";
                break;
            case 2:
                strResult = "beauty";
                break;
            case 3:
                strResult = "life";
                break;
            case 4:
                strResult = "health";
                break;
            case 5:
                strResult = "travel";
                break;
            case 6:
                strResult = "entertainmean";
                break;
            case 7:
                strResult = "automobile";
                break;
            case 8:
                strResult = "traffic";
                break;
            case 9:
                strResult = "professional";
                break;
            case 10:
                strResult = "bank";
                break;
            case 11:
                strResult = "government";
                break;
            case 12:
                strResult = "politics";
                break;
            case 13:
                strResult = "organization";
                break;
            case 14:
                strResult = "pet";
                break;
            case 15:
                strResult = "logistic";
                break;
            case 16:
                strResult = "media";
                break;
            case 17:
                strResult = "others";
                break;
            case 18:
                strResult = "personal";
                break;
            case 19:
                strResult = "publicperson";
                break;
            case 20:
                strResult = "shopping";
                break;
            default:
                strResult = "null";
                break;
            
        }
        return strResult;
    }
    
    
    /**
     * Data format is 2016-02-01T01:10:13
     *
     * @return
     */
    public String strAppInstallTime()
    {
        String strResult;
        
        int nAge = random.nextInt(5);
        
        Calendar calendar = Calendar.getInstance();
        int nYear = calendar.get(Calendar.YEAR) - nAge;
        int nMonth = random.nextInt(12);
        if (0 == nMonth)
        {
            nMonth = 2;
        }
        int nDay;
        if (2 == nMonth)
        {
            nDay = random.nextInt(28);
        }
        else
        {
            nDay = random.nextInt(30);
        }
        
        int nHour = random.nextInt(23);
        int nMin = random.nextInt(59);
        int nSecond = random.nextInt(59);
        
        strResult = String.format("%d-%02d-%02dT%02d:%02d:%02d", nYear, nMonth, nDay, nHour, nMin
                , nSecond);
        
        return strResult;
    }
    
    /**
     * 查詢來源為接電話 or 撥出電話 or 未接電話
     * n=3,p1=0.64,p2=0.32,p3=0.04
     *
     * @return "in","out","missed"
     */
    public String queryType()
    {
        String strResult;
        double[] p = {0.64, 0.32, 0.04};
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1, 3,
                        p);
        int nIndex = (int) result.get(0);
        switch (nIndex)
        {
            case 0:
                strResult = "in";
                break;
            case 1:
                strResult = "out";
                break;
            default:
                strResult = "missed";
                break;
            
        }
        return strResult;
    }
    
    /**
     * n=8,p1=0.55,p2=0.2,p3=0.25,p4~p8=0
     * HFB(0.55),Telemarketing(0.2),Harassment(0.25),Fraud(0),Adult(0),Call Center(0),Phishing(0)
     * ,Illeagl(0)
     *
     * @return string
     */
    public String spamCategory()
    {
        String strResult;
        double[] p = {0.55, 0.2, 0.25, 0, 0, 0, 0, 0};
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1, 8,
                        p);
        
        int nIndex = (int) result.get(0);
        switch (nIndex)
        {
            case 0:
                strResult = "HFB";
                break;
            case 1:
                strResult = "Telemarketing";
                break;
            case 2:
                strResult = "Harassment";
                break;
            case 3:
                strResult = "Fraud";
                break;
            case 4:
                strResult = "Adult";
                break;
            case 5:
                strResult = "Call Center";
                break;
            case 6:
                strResult = "Phishing";
                break;
            case 7:
                strResult = "Illeagl";
                break;
            default:
                strResult = "";
                break;
        }
        return strResult;
    }
    
    public String strID()
    {
        String strResult;
        IDMaker idMaker = new IDMaker();
        strResult = idMaker.random();
        return strResult;
    }
    
    public String strGeographicalLevel()
    {
        int nLevel = (int) PoissonDist(8, false);
        return String.format("%d-%d萬", nLevel, 10 + nLevel);
    }
    
    public String strCreditRating()
    {
        int nLevel = (int) PoissonDist(8, false);
        return String.format("%d", nLevel);
    }
    
    public String strTheNumOfHisCards()
    {
        int nLevel = (int) PoissonDist(1, false);
        return String.format("%d", nLevel);
    }
    
    public String strDepartmentTransRatio()
    {
        double nLevel = (double) PoissonDist(1, true);
        return String.format("%f", nLevel);
    }
    
    public String strCreditOnlineTransRatio()
    {
        double nLevel = (double) PoissonDist(3, true);
        return String.format("%f", nLevel);
    }
    
    public String strIP()
    {
        int[] anIP = new int[4];
        
        for (int i = 0; i < anIP.length; ++i)
        {
            anIP[i] = random.nextInt(254);
            if (0 == anIP[i] || 10 == anIP[i] || 172 == anIP[i] || 192 == anIP[i] || 127 == anIP[i])
            {
                anIP[i] = 140;
            }
        }
        
        return String.format("%d.%d.%d.%d", anIP[0], anIP[1], anIP[2], anIP[3]);
    }
    
    /**
     * 東經120度至122度，北緯22度至25度
     *
     * @return
     */
    public String strLatitudeAndLongitude()
    {
        int[] LaInt = {22, 23, 24, 25};
        int[] LoInt = {120, 121, 122};
        
        double La = LaInt[random.nextInt(LaInt.length - 1)] + random.nextDouble();
        double Lo = LoInt[random.nextInt(LoInt.length - 1)] + random.nextDouble();
        return String.format("%f %f", La, Lo);
    }
    
    
}
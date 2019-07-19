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
import java.text.SimpleDateFormat;
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
    private final static String EnglistUp = "ABCDEFGHJKLMNPQRSTUVWXYZIO";
    
    //function members
    private String strSurname()
    {
        surNameIndex = random.nextInt(CharacterInformation.surname.length - 1);
        return CharacterInformation.surname[surNameIndex];
    }
    
    private String strFirstName()
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
     * @return build
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
     * @return job
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
    
    final String[] jobsHuanan = {"製造業", "金融及保險業", "教育業", "批發及零售業", "住宿及餐飲業", "其他服務業", "其他"};
    
    public String strJobHuanan()
    {
        double[] p = {0.25, 0.15, 0.07, 0.15, 0.19, 0.10, 0.9};
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1, 7,
                        p);
        
        int nIndex = (int) result.get(0);
        if (0 > nIndex || jobs.length <= nIndex)
        {
            nIndex = 0;
        }
        
        return jobs[nIndex];
    }
    
    private final String[] residence = {"台北市", "基隆市", "新北市", "宜蘭縣", "台中市", "彰化縣", "南投縣", "嘉義市",
            "嘉義縣", "雲林縣", "台南市", "高雄市", "澎湖縣", "屏東縣", "台東縣", "花蓮縣", "連江縣"};
    
    public String strResidence()
    {
        double[] p = {0.16, 0.02, 0.24, 0.16, 0.09, 0.04, 0.01, 0.01, 0.01, 0.02, 0.07, 0.12,
                0.002, 0.03, 0.007, 0.01, 0.001};
        ArrayList result =
                (ArrayList) new MultinominalDistributionFuncs().multinominalRandomGenerator(1, 17
                        , p);
        
        int nIndex = (int) result.get(0);
        if (0 > nIndex || residence.length <= nIndex)
        {
            nIndex = 0;
        }
        
        return residence[nIndex];
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
    
    /**
     * 領現：53.13%
     * 台幣轉帳：24.04 %
     * 外匯結購結售：4.33%
     * 基金：12.23%
     * 貸款：2.06%
     * 繳稅費：3.93%
     * 其他 ： 0.28%
     */
    private final double[] trans_typeP = {0.53, 0.24, 0.04, 0.12, 0.02, 0.04, 0.003};
    private final String[] trans_typeS = {"領現", "台幣轉帳", "外匯結購結售", "基金", "貸款", "繳稅費", "其他"};
    
    public String strTransType()
    {
        return MultinominalDistribution(trans_typeP, trans_typeS);
    }
    
    /**
     * 臨櫃：18.74%
     * 網銀：20.78%
     * 行銀：25.77%
     * ATM：32.58%
     * 網路ATM：1.13%
     * 其他 ：1%
     */
    private final double[] trans_channelP = {0.19, 0.21, 0.26, 0.33, 0.01, 0.01};
    private final String[] trans_channelS = {"臨櫃", "網銀", "行銀", "ATM", "網路ATM", "其他"};
    
    public String strTransChannel()
    {
        return MultinominalDistribution(trans_channelP, trans_channelS);
    }
    
    /*
    * 1成 3%
2成 4%
3成 5%
4成 6%
5成 7%
6成 10%
7成 15%
8成 50%*/
    private final double[] huanan_loan_percentP = {0.03, 0.04, 0.05, 0.06, 0.07, 0.10, 0.15, 0.50};
    private final String[] huanan_loan_percentS = {"10", "20", "30", "40", "50", "60", "70", "80"};
    
    public int strHuananLoanPercent()
    {
        String strResult = MultinominalDistribution(huanan_loan_percentP, huanan_loan_percentS);
        return Integer.valueOf(strResult);
    }
    
    /*購置不動產-自用 60%
 購置不動產-非自用 5%
 房屋修繕 5%
 投資理財與其他 30%*/
    private final double[] huanan_loan_usageP = {0.6, 0.05, 0.05, 0.3};
    private final String[] huanan_loan_usageS = {"購置不動產-自用", "購置不動產-非自用", "房屋修繕", "投資理財與其他"};
    
    public String strHuananLoanUsage()
    {
        return MultinominalDistribution(huanan_loan_usageP, huanan_loan_usageS);
    }
    
    private String MultinominalDistribution(double[] p, String[] strItemName)
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
     * 華南
     * 私人企業：45.3%
     * 銀行：12.9%
     * 學校：6.6%
     * 公共企業：15.3%
     * 其他：19.9%
     */
    private final double[] service_unitsP = {0.45, 0.13, 0.07, 0.15, 0.20};
    private final String[] service_unitsS = {"私人企業", "銀行", "學校", "公共企業", "其他"};
    
    public String strService_units()
    {
        return MultinominalDistribution(service_unitsP, service_unitsS);
    }
    
    /**
     * 華南
     * 未婚：51.28%
     * 已婚：45.19%
     * 其他：3.53%
     */
    private final double[] maritalP = {0.51, 0.45, 0.04};
    private final String[] maritalS = {"未婚", "已婚", "其他"};
    
    public String strMarital()
    {
        return MultinominalDistribution(maritalP, maritalS);
    }
    
    /**
     * 華南
     * 國中以下：2.63%
     * 高中職：29.67%
     * 專科/大學：57.28%
     * 研究所以上：10.43%
     */
    private final double[] educationP = {0.03, 0.30, 0.57, 0.10};
    private final String[] educationS = {"國中以下", "高中職", "專科/大學", "研究所以上"};
    
    public String strEducation()
    {
        return MultinominalDistribution(educationP, educationS);
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
    
    /*薪資 50%
營利收入 25%
投資收入 15%
租金收入 7%
利息收入 3%
其他 2%*/
    private final double[] huananPaySrcP = {0.5, 0.25, 0.15, 0.07, 0.03, 0.02};
    private final String[] huananPaySrcS = {"薪資", "營利收入", "投資收入", "租金收入", "利息收入", "其他"};
    
    public String huananPaymentSource()
    {
        return MultinominalDistribution(huananPaySrcP, huananPaySrcS);
    }
    
    /*無寬限期 65%
0~1年   8%
1~2年 12%
2~3年 15%*/
    private final double[] huananGracePeriodP = {0.08, 0.12, 0.15};
    private final String[] huananGracePeriodS = {"0~1年", "1~2年", "2~3年"};
    
    public String huananGracePeriod()
    {
        return MultinominalDistribution(huananGracePeriodP, huananGracePeriodS);
    }
    
    /*一般房貸 75%
公教房貸 15%
政策性房貸 10%*/
    private final double[] huananPropertyP = {0.75, 0.15, 0.10};
    private final String[] huananPropertyS = {"一般房貸", "公教房貸", "政策性房貸"};
    
    public String huananProperty()
    {
        return MultinominalDistribution(huananPropertyP, huananPropertyS);
    }
    
    /*以元為單位
最小值200萬元
最大值1億元*/
    
    public int huananAppraisal()
    {
        return (2000000 + random.nextInt(80000000));
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
    
    /*無延遲繳款 99.5%
逾期30天以上 0.35%
逾期90天以上 0.15%*/
    private final double[] huananSituationP = {0.99, 0.0035, 0.0015};
    private final String[] huananSituationS = {"無延遲繳款", "逾期30天以上", "逾期90天以上"};
    
    public String huananSituation()
    {
        return MultinominalDistribution(huananSituationP, huananSituationS);
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
    
    /**
     * @param nType data format, 0:YYYY/mm/DD , 1:YYYY-mm-DD
     * @return
     */
    public String strBirthday(int nType)
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
        
        String strB = "";
        switch (nType)
        {
            case 0:
                strB = String.format("%d/%d/%d", nYear, nMonth, nDay);
                break;
            case 1:
                strB = String.format("%d-%02d-%02d", nYear, nMonth, nDay);
                break;
        }
        
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
    
    public int is_SNY()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.02);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    public int is_register_web_bank()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.27);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    public int is_app_bank()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.11);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    public int is_register_mobile_pay()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.02);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    
    public int dependents()
    {
        return random.nextInt(6);
    }
    
    public String strCredit_level()
    {
        String[] creditLevel = {"A", "B", "C"};
        return creditLevel[random.nextInt(creditLevel.length)];
    }
    
    public String strAccountNumber()
    {
        return String.format("%d%d%d%d", (103 + random.nextInt(300)),
                (1000 + random.nextInt(7999)), (100 + random.nextInt(899)),
                (10 + random.nextInt(89)));
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
    
    public String strGender()
    {
        String genderResult = null;
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.6);
        int genderEncoding = binominalDistFuncs.getSample(1)[0];
        switch (genderEncoding)
        {
            case 0:
                genderResult = "男";
                break;
            case 1:
                genderResult = "女";
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
    
    /**
     * 最早為2011-01-01，最晚為2019-04-30
     *
     * @return
     */
    public String strTransDate()
    {
        String strYear = String.format("%d", (2011 + random.nextInt(8)));
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
        if (0 == strYear.compareTo("2019"))
        {
            if (4 < nMonth)
            {
                nMonth = 4;
            }
        }
        
        return String.format("%s-%02d-%02d", strYear, nMonth, nDay);
    }
    
    public int amount()
    {
        return (269203 + random.nextInt(76000000));
    }
    
    public int balence()
    {
        return (888888 + random.nextInt(9000000));
    }
    
    public int amount(int nMix, int nMax, int nAverage)
    {
        int nResult;
        if (0 == random.nextInt(1))
        {
            nResult = nAverage - random.nextInt(nMix);
        }
        else
        {
            nResult = nAverage + random.nextInt(nMix);
            if (nMax < nResult)
            {
                nResult = nMax;
            }
            
        }
        return nResult;
    }
    
    /*
    最小值0.5年
最大值30年
平均值 12年
     */
    public int huanan_loan_period()
    {
        return randomAverage(1, 30, 12);
    }
    
    /*以元為單位
最小值200萬元
最大值8500萬元
平均值950萬*/
    public int huananBalance()
    {
        return randomAverage(2000000, 85000000, 9500000);
    }
    
    /*以元為單位
最小值1元
最大值8499萬元
平均值880萬*/
    public int huananValue()
    {
        return randomAverage(1, 84990000, 8800000);
    }
    
    private int randomAverage(int nMin, int nMax, int nAverage)
    {
        int nBasic = 1;
        int nResult;
        nBasic = random.nextInt(3);
        if (0 == nBasic)
        {
            ++nBasic;
        }
        if (1 > (nMin / nBasic))
        {
            nBasic = 1;
        }
        if (0 == random.nextInt(1))
        {
            
            nResult = nAverage - random.nextInt(nMin / nBasic);
        }
        else
        {
            nResult = nAverage + random.nextInt(nMax / nBasic);
            if (nMax < nResult)
            {
                nResult = nMax;
            }
        }
        return nResult;
    }
    
    /*最小值 1.60%
最大值 1.82%
平均值 1.70%*/
    public double huananInterestRate()
    {
        double[] rate = {1.60, 1.82, 1.70};
        return rate[random.nextInt(3)];
    }
    
    /*HAA000001~HAA200000*/
    public String huanan_benefit_id()
    {
        return String.format("%06d", random.nextInt(200000));
    }
    
    public String huanan_property()
    {
        final double[] P = {0.60, 0.40};
        final String[] S = {"正擔保", "加強債權"};
        return MultinominalDistribution(P, S);
    }
    
    public String huanan_location()
    {
        final double[] P = {0.02, 0.22, 0.25, 0.13, 0.02, 0.04, 0.03, 0.08, 0.02, 0.001, 0.008,
                0.004, 0.008, 0.06, 0.09, 0.01, 0.020, 0.007, 0.006, 0.0002, 0.0001};
        final String[] S = {"基隆市", "台北市", "新北市", "桃園市", "新竹市", "新竹縣", "苗栗縣", "台中市", "彰化縣", "南投縣",
                "雲林縣", "嘉義縣", "嘉義市", "台南市", "高雄市", "屏東縣", "宜蘭縣", "花蓮縣", "台東縣", "澎湖縣", "金門縣"};
        return MultinominalDistribution(P, S);
    }
    
    public String huanan_building_type()
    {
        final double[] P = {0.15, 0.1, 0.06, 0.04, 0.4, 0.2, 0.05};
        final String[] S = {"公寓(5樓含以下無電梯)", "透天厝", "店面(店舖)", "辦公室商業大樓", "住宅大樓(11層含以上有電梯)",
                "華廈" + "(10層含以下有電梯)", "套房"};
        return MultinominalDistribution(P, S);
    }
    
    /*住宅區 45%
商業區 35%
工業區 15%
其他 5%*/
    public String huanan_proximity_attr()
    {
        final double[] P = {0.45, 0.35, 0.15, 0.05};
        final String[] S = {"住宅區", "商業區", "工業區", "其他"};
        return MultinominalDistribution(P, S);
    }
    
    /*10年以下 7%
10年~20年 22%
20年~30年27%
30年~40年24%
40年以上20%*/
    public String huanan_house_age()
    {
        final double[] P = {0.07, 0.22, 0.27, 0.24, 0.20};
        final String[] S = {"10年以下", "10年~20年", "20年~30年", "30年~40年", "40年以上"};
        return MultinominalDistribution(P, S);
    }
    
    public String huanan_fund_code()
    {
        final String[] S = {"D1", "D2", "D3", "D4", "D5"};
        return S[random.nextInt(5)];
    }
    
    public String huanan_fund_name(String fund_code)
    {
        if (0 == fund_code.compareTo("D1"))
        {
            return "華南永昌永昌基金";
        }
        
        if (0 == fund_code.compareTo("D2"))
        {
            return "華南永昌鳳翔貨幣市場基金";
        }
        
        if (0 == fund_code.compareTo("D3"))
        {
            return "華南永昌龍盈平衡基金";
        }
        
        if (0 == fund_code.compareTo("D4"))
        {
            return "華南永昌麒麟貨幣市場基金";
        }
        
        if (0 == fund_code.compareTo("D5"))
        {
            return "華南永昌物聯網精選基金";
        }
        
        return "華南永昌永昌基金";
    }
    
    private final String[] dividend = {"月配", "季配", "年配", "半年配"};
    
    public String huanan_dividend_category()
    {
        return dividend[random.nextInt(4)];
    }
    
    public double huanan_net(String fund_code)
    {
        double[] net = {20.05, 12.05, 24.9565, 16.2784, 17.40};
        if (0 == fund_code.compareTo("D1"))
        {
            return net[0];
        }
        
        if (0 == fund_code.compareTo("D2"))
        {
            return net[1];
        }
        
        if (0 == fund_code.compareTo("D3"))
        {
            return net[2];
        }
        
        if (0 == fund_code.compareTo("D4"))
        {
            return net[3];
        }
        
        if (0 == fund_code.compareTo("D5"))
        {
            return net[4];
        }
        
        return net[0];
    }
    
    private final String[] ac = {"台幣", "指定外幣", "綜合外幣"};
    
    public String huanan_account_category()
    {
        return ac[random.nextInt(3)];
    }
    
    public int huanan_capital()
    {
        return 100000 + random.nextInt(5000000);
    }
    
    private String[] bankcode = {"001", "003", "004", "005", "006", "007", "008", "009", "010",
            "011", "012", "013", "016", "017", "018", "021", "024", "025", "039", "040", "050",
            "104", "108"};
    
    public String huanan_bank_code()
    {
        return bankcode[random.nextInt(bankcode.length)];
    }
    
    public int huanan_inventory_unit()
    {
        return 100 + random.nextInt(1000);
    }
    
    public int huanan_investment_cost()
    {
        return 100000 + random.nextInt(1000000);
    }
    
    public int huanan_risk()
    {
        int nRisk = random.nextInt(3);
        ++nRisk;
        return nRisk;
    }
    
    //AJF-0001～DZZ-9999
    public String strCarId()
    {
        StringBuilder strId = new StringBuilder();
        for (int i = 0; i < 3; ++i)
        {
            strId.append(EnglistUp.charAt(random.nextInt(EnglistUp.length())));
        }
        return String.format("%s-%04d", strId.toString(), random.nextInt(9999));
    }
    
    public int getClaimNO()
    {
        return random.nextInt(5);
    }
    
    /*◎強制險：SCF000001~SCF999999
◎任意險：SCX000001~SCX999999
◎旅綜險：SCT000001~SCT999999
◎個人傷害險：SCA000001~SCA999999
◎個人健康險：SCH000001~SCH999999*/
    private final String[] claimhead = {"SCF", "SCX", "SCT", "SCA", "SCH"};
    
    public String huanan_claim_no(int nClaimNo)
    {
        return String.format("%s-%06d", claimhead[nClaimNo], random.nextInt(999999));
    }
    
    private final String[] huanan_classification = {"強制險", "任意險", "旅綜險", "個人傷害險", "個人健康險"};
    
    public String huanan_classification(int nClaim)
    {
        return huanan_classification[nClaim];
    }
    
    /*◎強制險：SPF000001~SPF999999
◎任意險：SPX000001~SPX999999
◎旅綜險：SPT000001~SPT999999
◎個人傷害險：SPA000001~SPA999999
◎個人健康險：SPH000001~SPH999999*/
    private final String[] policy_no = {"SPF", "SPX", "SPT", "SPA", "SPH"};
    
    public String huanan_policy_no(int nClaimNo)
    {
        return String.format("%s-%06d", policy_no[nClaimNo], random.nextInt(999999));
    }
    
    public int huanan_claim_amount()
    {
        return random.nextInt(1018000) + 300;
    }
    
    private final String[] spf = {"駕駛人過失", "駕駛人酗酒", "第三人過失", "整車被竊", "零件被竊", "機械故障煞車失靈", "其他"};
    private final String[] spx = {"與其他車輛相撞之機動車交通事故", "其他非相撞(自撞)之機動車交通事故", "駕駛人過失", "駕駛人酗酒",
            "第三人過失", "不明原因停放中被刮損", "不明原因停放中被碰撞", "不明原因其他", "整車被竊", "零件被竊", "被竊尋回修復", "竊盜未遂", "火燒"
            , "天災－颱風", "其他"};
    private final String[] spt = {"交通工具延誤", "風災(強風、龍捲風、颱風)", "其他"};
    private final String[] spa = {"跌倒", "車禍", "被物體或人意外撞擊或擊中", "其他"};
    private final String[] sph = {"疾病", "車禍", "其他"};
    
    public String huanan_claim_descript(int nClaimNo)
    {
        String strDesc = "";
        switch (nClaimNo)
        {
            case 0:
                strDesc = spf[random.nextInt(spf.length)];
                break;
            case 1:
                strDesc = spx[random.nextInt(spx.length)];
                break;
            case 2:
                strDesc = spt[random.nextInt(spt.length)];
                break;
            case 3:
                strDesc = spa[random.nextInt(spa.length)];
                break;
            case 4:
                strDesc = sph[random.nextInt(sph.length)];
                break;
        }
        return strDesc;
    }
    
    public String huanan_exp_date()
    {
        String strYear = String.format("%d", (2019 + random.nextInt(5)));
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
        if (0 == strYear.compareTo("2019"))
        {
            if (4 < nMonth)
            {
                nMonth = 4;
            }
        }
        
        return String.format("%s-%02d-%02d", strYear, nMonth, nDay);
    }
    
    public int huanan_insurance_premiums()
    {
        return random.nextInt(2000) + 2000;
    }
    
    public String huanan_insurance_date()
    {
        String strYear = String.format("%d", (2016 + random.nextInt(3)));
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
        if (0 == strYear.compareTo("2019"))
        {
            if (4 < nMonth)
            {
                nMonth = 4;
            }
        }
        
        return String.format("%s-%02d-%02d", strYear, nMonth, nDay);
    }
    
    public String huanan_payments_period(String strInsurenceDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try
        {
            Date date = sdf.parse(strInsurenceDate);
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -3);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sdf.format(calendar.getTime());
    }
    
    public int huanan_pay_status()
    {
        BinominalDistFuncs binominalDistFuncs = new BinominalDistFuncs(1, 0.95);
        return binominalDistFuncs.getSample(1)[0];
    }
    
    /*◎信用卡:25.8%
◎超商繳費:24.9%
◎匯款:33.2%
◎現金:4.7%
◎支票:11.4%*/
    public String huanan_pay_category()
    {
        final double[] P = {0.26, 0.25, 0.33, 0.04, 0.11};
        final String[] S = {"信用卡", "超商繳費", "匯款", "現金", "支票"};
        return MultinominalDistribution(P, S);
    }
    
}

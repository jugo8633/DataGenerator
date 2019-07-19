import org.apache.commons.io.LineIterator;
import org.iii.modules.FileHandler;
import org.iii.modules.SqlHandler;
import org.iii.simulator.generator.BuildInFunction;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Jugo on 2019/7/3
 */

class HuananDataGenerator
{
    private static HuananDataGenerator instance;
    private SQLiteDataSource sqLiteDataSource;
    private BuildInFunction BIF;
    private ArrayList<String> listStreet;
    private Random random = new Random();
    private int m_nStreetSize;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private final String strPriceCurrency;
    private int m_nSerial;
    private LineIterator iterator;
    private FileHandler fileHandler;
    private final String m_strCarType = "自小客(車種代號:03)";
    private final int COUNT_TOKEN = 100;
    private final int COUNT_BLACKLIST = 1000;
    private int m_nCount;
    
    private HuananDataGenerator()
    {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);
        sqLiteDataSource = new SQLiteDataSource(config);
        BIF = new BuildInFunction();
        listStreet = new ArrayList<>();
        strPriceCurrency = "台幣(TWN)";
    }
    
    /**
     * this is a singleton class
     *
     * @return
     */
    public static HuananDataGenerator getInstance()
    {
        if (null == instance)
        {
            synchronized (HuananDataGenerator.class)
            {
                if (null == instance)
                {
                    instance = new HuananDataGenerator();
                }
            }
        }
        return instance;
    }
    
    int init(String strSQLitePath)
    {
        sqLiteDataSource.setUrl("jdbc:sqlite:" + strSQLitePath);
        try
        {
            if (null != (connection = sqLiteDataSource.getConnection()) && -1 != initStreetData(BIF))
            {
                statement = connection.createStatement();
                return 0;
            }
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
    
    void generate(int nId, int nTotal)
    {
        int nResult;
        
        ArrayList<String> listSQL = new ArrayList<>();
        
        fileHandler = new FileHandler();
        
        try
        {
            iterator = fileHandler.loadFile("database/serial.txt", "UTF-8");
            connection.setAutoCommit(false);
            m_nSerial = 0;
            
            
            //while (nTotal > simTimes)
            tableClear();
            while (iterator.hasNext())
            {
                //模擬數據與產生SQL
                simulateData(listSQL);
                if (0 >= listSQL.size())
                {
                    System.out.println("Error!! No SQL for Simulate Data");
                    connection.rollback();
                    continue;
                }
                
                nResult = -1;
                for (String strSQL : listSQL)
                {
                    if (-1 == runSQL(strSQL))
                    {
                        System.out.println("Exec SQL Fail: " + strSQL);
                        connection.rollback();
                        nResult = -1;
                        break;
                    }
                    nResult = 0;
                }
                
                if (0 == nResult)
                {
                    ++m_nCount;
                    connection.commit();
                    System.out.println("Thread " + nId + " - SQL Commit , Count = " + m_nCount);
                }
            }
            statement.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    private void tableClear()
    {
        runSQL("delete from sqlite_sequence");
        runSQL("delete from bank_account");
        runSQL("delete from account_number");
        runSQL("delete from trans_record");
        runSQL("delete from loan_record");
        runSQL("delete from construction_record");
        runSQL("delete from fund_information");
        runSQL("delete from fund_account");
        runSQL("delete from fund_inventory");
        runSQL("delete from beneficiary");
        runSQL("delete from blacklist");
        runSQL("delete from tokens");
        runSQL("delete from claim_record");
        runSQL("delete from insurance_exp_date");
        runSQL("delete from insurance_record");
        runSQL("delete from online_report");
        runSQL("delete from payment_method");
        m_nCount = 0;
    }
    
    private int runSQL(String strSQL)
    {
        if (null != statement)
        {
            try
            {
                statement.executeUpdate(strSQL);
                return 0;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return -1;
    }
    
    private void simulateData(ArrayList<String> listSQL) throws Exception
    {
        int user_id;
        String strAccountNum;
        String strSQL;
        String strUUID;
        int nSerial = 0;
        int nClaimNo = 0;
        String strPoliceNo;
        String strFundCode;
        String identity_id;
        String license_no;
        String claim_date;
        String insurance_date;
        
        while (iterator.hasNext())
        {
            nSerial = Integer.valueOf(iterator.nextLine());
            if (0 == checkSerial(nSerial))
            {
                break;
            }
        }
        
        listSQL.clear();
        strUUID = BIF.strUUID(1);
        strSQL = String.format(SqlHandler.SQL_HUANAN_BANK_ACCOUNT, strUUID, nSerial,
                BIF.strBirthday(1), BIF.strGender(), BIF.strJobHuanan(), BIF.strResidence(),
                BIF.strIncome(), BIF.strService_units(), BIF.strMarital(), BIF.strEducation(),
                BIF.dependents(), BIF.strCredit_level(), BIF.is_SNY(), BIF.is_register_web_bank()
                , BIF.is_app_bank(), BIF.is_register_mobile_pay());
        if (-1 == runSQL(strSQL))
        {
            System.out.println("Error Exec SQL: " + strSQL);
            return;
        }
        
        user_id = getBasicId(strUUID);
        if (0 > user_id)
        {
            System.out.println("Error basic_id invalid uuid: " + strUUID);
            return;
        }
        
        strFundCode = BIF.huanan_fund_code();
        strAccountNum = BIF.strAccountNumber();
        identity_id = BIF.strID();
        license_no = BIF.strCarId();
        nClaimNo = BIF.getClaimNO();
        strPoliceNo = BIF.huanan_policy_no(nClaimNo);
        claim_date = BIF.strTransDate();
        insurance_date = BIF.huanan_insurance_date();
        
        if (COUNT_TOKEN > m_nCount)
        {
            listSQL.add(String.format(SqlHandler.SQL_TOKENS, UUID.randomUUID().toString()));
        }
        
        listSQL.add(String.format(SqlHandler.SQL_ACCOUNT_NUMBER, user_id, strAccountNum));
        
        listSQL.add(String.format(SqlHandler.SQL_TRANS_RECORD, user_id, strAccountNum,
                BIF.strTransType(), BIF.strTransChannel(), claim_date, BIF.amount(),
                BIF.balence()));
        
        listSQL.add(String.format(SqlHandler.SQL_LOAN_RECORD, user_id, BIF.amount(800000,
                50000000, 6000000), BIF.strHuananLoanPercent(), BIF.strHuananLoanUsage(),
                BIF.huanan_loan_period(), BIF.huananPaymentSource(), BIF.huananGracePeriod(),
                BIF.huananProperty(), BIF.huananAppraisal(), BIF.huananBalance(),
                BIF.huananValue(), BIF.huananSituation(), BIF.huananInterestRate()));
        
        listSQL.add(String.format(SqlHandler.SQL_CONSTRUCTION_RECORD, user_id,
                BIF.huanan_benefit_id(), BIF.huanan_property(), BIF.huanan_location(),
                BIF.huanan_building_type(), BIF.huanan_proximity_attr(), BIF.huanan_house_age()));
        
        listSQL.add(String.format(SqlHandler.SQL_FUND_INFORMATION, user_id, strFundCode,
                BIF.huanan_fund_name(strFundCode), strPriceCurrency,
                BIF.huanan_dividend_category(), claim_date, BIF.huanan_net(strFundCode)));
        
        listSQL.add(String.format(SqlHandler.SQL_FUND_ACCOUNT, user_id,
                BIF.huanan_account_category(), BIF.huanan_capital(), BIF.huanan_bank_code(),
                BIF.strAccountNumber()));
        
        listSQL.add(String.format(SqlHandler.SQL_FUND_INVENTORY, user_id, strFundCode,
                BIF.huanan_fund_name(strFundCode), strPriceCurrency, BIF.huanan_inventory_unit(),
                BIF.huanan_investment_cost()));
        
        listSQL.add(String.format(SqlHandler.SQL_BENEFICIARY, user_id, BIF.huanan_benefit_id(),
                BIF.strChineseName(), BIF.strBirthday(1), BIF.huanan_risk(), claim_date));
    
        if (COUNT_BLACKLIST > m_nCount)
        {
            listSQL.add(String.format(SqlHandler.SQL_BLACK_LIST, identity_id, license_no));
        }
        
        listSQL.add(String.format(SqlHandler.SQL_CLAM_RECORD, BIF.huanan_claim_no(nClaimNo),
                strPoliceNo, claim_date, BIF.huanan_claim_amount(),
                BIF.huanan_claim_descript(nClaimNo)));
        
        listSQL.add(String.format(SqlHandler.SQL_INSURANCE_EXP_DATE, identity_id, strPoliceNo,
                BIF.huanan_exp_date(), 1));
        
        listSQL.add(String.format(SqlHandler.SQL_INSURANCE_RECORD, user_id, strPoliceNo,
                BIF.huanan_classification(nClaimNo), BIF.huanan_classification(nClaimNo),
                BIF.huanan_claim_descript(nClaimNo), BIF.huanan_insurance_premiums(),
                insurance_date, BIF.huanan_exp_date(), m_strCarType));
        
        listSQL.add(String.format(SqlHandler.SQL_ONLINE_REPORT, strPoliceNo, identity_id,
                license_no, claim_date));
        
        listSQL.add(String.format(SqlHandler.SQL_PAYMENT_METHOD, user_id, strPoliceNo,
                BIF.huanan_pay_status(), BIF.huanan_insurance_date(), BIF.huanan_pay_category(),
                BIF.huanan_payments_period(insurance_date)));
        
    }
    
    private int initStreetData(BuildInFunction buildInFunction)
    {
        buildInFunction.loadStreet(listStreet);
        if (0 >= listStreet.size())
        {
            System.out.println("SQLite street data load fail!!");
            return -1;
        }
        m_nStreetSize = listStreet.size() - 1;
        return listStreet.size();
    }
    
    private int getBasicId(String strUUID) throws SQLException
    {
        String strSQL = String.format(SqlHandler.SQL_BANK_ACCOUNT_ID, strUUID);
        //System.out.println(strSQL);
        ResultSet tmpNID = statement.executeQuery(strSQL);
        if (tmpNID.next())
        {
            return tmpNID.getInt("id");
        }
        return -1;
    }
    
    private int checkSerial(int nSerial) throws SQLException
    {
        ResultSet tmp =
                statement.executeQuery("SELECT * FROM stock_history WHERE serial = " + nSerial);
        if (tmp.next())
        {
            return 0;
        }
        return -1;
    }
    
}

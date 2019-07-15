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
    private SQLiteDataSource sqLiteDataSource;
    private BuildInFunction BIF;
    private ArrayList<String> listStreet;
    private Random random = new Random();
    private int m_nStreetSize;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private final String strPriceCurrency = "台幣(TWN)";
    private int m_nSerial;
    private LineIterator iterator;
    private FileHandler fileHandler;
    
    HuananDataGenerator()
    {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);
        sqLiteDataSource = new SQLiteDataSource(config);
        BIF = new BuildInFunction();
        listStreet = new ArrayList<>();
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
        int simTimes = 0;
        ArrayList<String> listSQL = new ArrayList<>();
        
        fileHandler = new FileHandler();
        
        try
        {
            iterator = fileHandler.loadFile("database/serial.txt", "UTF-8");
            connection.setAutoCommit(false);
            m_nSerial = 0;
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
            
            //while (nTotal > simTimes)
            while(iterator.hasNext())
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
                    ++simTimes;
                    connection.commit();
                    System.out.println("Thread " + nId + " - SQL Commit , Count = " + simTimes);
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
        
        listSQL.add(String.format(SqlHandler.SQL_TOKENS, user_id, UUID.randomUUID().toString()));
        
        strAccountNum = BIF.strAccountNumber();
        listSQL.add(String.format(SqlHandler.SQL_ACCOUNT_NUMBER, user_id, strAccountNum));
        
        listSQL.add(String.format(SqlHandler.SQL_TRANS_RECORD, user_id, strAccountNum,
                BIF.strTransType(), BIF.strTransChannel(), BIF.strTransDate(), BIF.amount(),
                BIF.balence()));
        
        listSQL.add(String.format(SqlHandler.SQL_LOAN_RECORD, user_id, BIF.amount(800000,
                50000000, 6000000), BIF.strHuananLoanPercent(), BIF.strHuananLoanUsage(),
                BIF.huanan_loan_period(), BIF.huananPaymentSource(), BIF.huananGracePeriod(),
                BIF.huananProperty(), BIF.huananAppraisal(), BIF.huananBalance(),
                BIF.huananValue(), BIF.huananSituation(), BIF.huananInterestRate()));
        
        listSQL.add(String.format(SqlHandler.SQL_CONSTRUCTION_RECORD, user_id,
                BIF.huanan_benefit_id(), BIF.huanan_property(), BIF.huanan_location(),
                BIF.huanan_building_type(), BIF.huanan_proximity_attr(), BIF.huanan_house_age()));
        
        String strFundCode = BIF.huanan_fund_code();
        
        listSQL.add(String.format(SqlHandler.SQL_FUND_INFORMATION, user_id, strFundCode,
                BIF.huanan_fund_name(strFundCode), strPriceCurrency,
                BIF.huanan_dividend_category(), BIF.strTransDate(), BIF.huanan_net(strFundCode)));
        
        listSQL.add(String.format(SqlHandler.SQL_FUND_ACCOUNT, user_id,
                BIF.huanan_account_category(), BIF.huanan_capital(), BIF.huanan_bank_code(),
                BIF.strAccountNumber()));
        
        listSQL.add(String.format(SqlHandler.SQL_FUND_INVENTORY, user_id, strFundCode,
                BIF.huanan_fund_name(strFundCode), strPriceCurrency, BIF.huanan_inventory_unit(),
                BIF.huanan_investment_cost()));
        
        listSQL.add(String.format(SqlHandler.SQL_BENEFICIARY, user_id, BIF.huanan_benefit_id(),
                BIF.strChineseName(), BIF.strBirthday(1), BIF.huanan_risk(), BIF.strTransDate()));
        
        listSQL.add(String.format(SqlHandler.SQL_BLACK_LIST, BIF.strID(), BIF.strCarId()));
        
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

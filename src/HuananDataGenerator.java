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
        
        try
        {
            connection.setAutoCommit(false);
            while (nTotal > simTimes)
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
        
        listSQL.clear();
        strUUID = BIF.strUUID(1);
        strSQL = String.format(SqlHandler.SQL_HUANAN_BANK_ACCOUNT, strUUID, BIF.strBirthday(1),
                BIF.strGender(), BIF.strJobHuanan(), BIF.strResidence(), BIF.strIncome(),
                BIF.strService_units(), BIF.strMarital(), BIF.strEducation(), BIF.dependents(),
                BIF.strCredit_level(), BIF.is_SNY(), BIF.is_register_web_bank(),
                BIF.is_app_bank(), BIF.is_register_mobile_pay());
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
        
        strAccountNum = BIF.strAccountNumber();
        listSQL.add(String.format(SqlHandler.SQL_ACCOUNT_NUMBER, user_id, strAccountNum));
        
        listSQL.add(String.format(SqlHandler.SQL_TRANS_RECORD, user_id, strAccountNum,
                BIF.strTransType(), BIF.strTransChannel(), BIF.strTransDate(), BIF.amount(),
                BIF.balence()));
        
        //"user_id" INTEGER NOT NULL,@
        // "amount" INTEGER,@
        // "percent" INTEGER,@
        // "usage" TEXT,@
        // "period" INTEGER,
        // "payment_sources" TEXT,
        // "grace_period" TEXT,
        // "property" TEXT,
        // "appraisal" INTEGER,
        // "balance" INTEGER,
        // "value" INTEGER,
        // "situation" TEXT,
        // "interest_rate" INTEGER
        listSQL.add(String.format(SqlHandler.SQL_LOAN_RECORD, user_id, BIF.amount(800000,
                50000000, 6000000), BIF.strHuananLoanPercent(), BIF.strHuananLoanUsage()));
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
}

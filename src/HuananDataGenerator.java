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
            statement.execute("BEGIN");
            while (nTotal > simTimes)
            {
                //模擬數據與產生SQL
                simulateData(listSQL);
            }
            statement.executeUpdate(String.format(SqlHandler.SQL_HUANAN_BANK_ACCOUNT, ));
            statement.execute("COMMIT");
            
            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
    }
    
    private void simulateData(ArrayList<String> listSQL) throws Exception
    {
        String basic_id;
        String strSQL;
        String strUUID;
        
        listSQL.clear();
        strUUID = BIF.strUUID(1);
        strSQL = String.format(SqlHandler.SQL_HUANAN_BANK_ACCOUNT, strUUID, BIF.strChineseName(),
                BIF.strEnglishName(), BIF.gender(), BIF.strID());
        if (-1 == statement.executeUpdate(strSQL))
        {
            System.out.println("Error Exec SQL: " + strSQL);
            return;
        }
        
        basic_id = getBasicId(strUUID);
        if (null == basic_id)
        {
            System.out.println("Error basic_id invalid uuid: " + strUUID);
            return;
        }
        
//        listSQL.add(String.format(SqlHandler.SQL_COMMUNICATION, BIF.strMobilePhone(4),
//                BIF.strPhone(0, 2), BIF.strEmail(), listStreet.get(random.nextInt(m_nStreetSize))
//                , BIF.strGeographicalLevel(), basic_id));
//
//        listSQL.add(String.format(SqlHandler.SQL_JOB, basic_id, BIF.strJob(), BIF.strJobTitle(),
//                BIF.strCustomHis(), BIF.strIncome()));
//
//        listSQL.add(String.format(SqlHandler.SQL_ACCOUNT, basic_id, BIF.strSaveMoney(),
//                BIF.strTransferNote(), BIF.strATM(), BIF.strLoginBank(), BIF.strIP(),
//                BIF.strLoginBankWebTime()));
//
//        listSQL.add(String.format(SqlHandler.SQL_LOAN, basic_id, BIF.strTheNumOfHisCards(),
//                BIF.strCreditRating(), BIF.strBuilding(), BIF.strBuildingInch(),
//                BIF.strCategorical(), BIF.strComeFrom(), BIF.strBadRecord(), BIF.strDelayDate()));
//
//        listSQL.add(String.format(SqlHandler.SQL_CONSUMPTION, basic_id, BIF.strMCC(),
//                BIF.strMCC(), BIF.strSaleType(), BIF.strShopingInfo(),
//                BIF.strDepartmentTransRatio(), BIF.strCreditOnlineTransRatio(),
//                BIF.strLatitudeAndLongitude()));
//
//        listSQL.add(String.format(SqlHandler.SQL_OTHER_COMMUNICATION, strUUID, BIF.strUUID(0),
//                basic_id, BIF.strRegion(), BIF.strTaiwanCity(), BIF.strOS(0), BIF.strOsVersion(0)
//                , BIF.strMobileModel(9), BIF.strManufacturer()));
//
//        listSQL.add(String.format(SqlHandler.SQL_APP, basic_id, BIF.subscriptStatus(),
//                BIF.intLoginStatus(), BIF.strLanguage(0), BIF.strAppInstallTime(),
//                BIF.strRegion(), BIF.queryType(), BIF.isContact(), BIF.bizCategory(),
//                BIF.isSpam(), BIF.spamCategory()));
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
}

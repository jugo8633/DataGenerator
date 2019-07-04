import com.mysql.jdbc.Connection;

import org.iii.modules.MysqlHandler;
import org.iii.modules.SqlHandler;
import org.iii.simulator.generator.BuildInFunction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Y.C Tsai
 * @project DataGenerator
 * @Date 2019/6/10
 */
class DataGenerator
{
    private MysqlHandler mysqlHandler;
    private BuildInFunction BIF;
    private ArrayList<String> listStreet;
    private Random random = new Random();
    private int m_nStreetSize;
    private Connection con;
    
    DataGenerator()
    {
        mysqlHandler = new MysqlHandler();
        BIF = new BuildInFunction();
        listStreet = new ArrayList<>();
    }
    
    int init(String strIP, int nPort, String strAccount, String strPassword, String strDB)
    {
        if (-1 != initStreetData(BIF))
        {
            mysqlHandler.init(strIP, nPort, strAccount, strPassword, strDB);
            con = mysqlHandler.start();
            if (null != con)
            {
                return 0;
            }
        }
        return -1;
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
    
    void generate(int nId, int nTotal)
    {
        int nResult;
        int simTimes = 0;
        ArrayList<String> listSQL = new ArrayList<>();
        
        try
        {
            mysqlHandler.execute("SET FOREIGN_KEY_CHECKS=0");
            mysqlHandler.execute("BEGIN");
            con.setAutoCommit(false);
            
            while (nTotal > simTimes)
            {
                //模擬數據與產生SQL
                simulateData(listSQL);
                if (0 >= listSQL.size())
                {
                    System.out.println("Error!! No SQL for Simulate Data");
                    con.rollback();
                    mysqlHandler.execute("ROLLBACK");
                    continue;
                }
                
                nResult = -1;
                for (String strSQL : listSQL)
                {
                    if (-1 == mysqlHandler.execute(strSQL))
                    {
                        System.out.println("Exec SQL Fail: " + strSQL);
                        con.rollback();
                        mysqlHandler.execute("ROLLBACK");
                        nResult = -1;
                        break;
                    }
                    nResult = 0;
                }
                
                if (0 == nResult)
                {
                    ++simTimes;
                    con.commit();
                    mysqlHandler.execute("COMMIT");
                    System.out.println("Thread " + nId + " - SQL Commit , Count = " + simTimes);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        mysqlHandler.execute("SET FOREIGN_KEY_CHECKS=1");
        mysqlHandler.close();
    }
    
    
    private void simulateData(ArrayList<String> listSQL) throws Exception
    {
        String basic_id;
        String strSQL;
        String strUUID;
        
        listSQL.clear();
        strUUID = BIF.strUUID(1);
        strSQL = String.format(SqlHandler.SQL_PERSONAL, strUUID,
                BIF.strChineseName(), BIF.strEnglishName(), BIF.gender(), BIF.strID());
        if (-1 == mysqlHandler.execute(strSQL))
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
        
        listSQL.add(String.format(SqlHandler.SQL_COMMUNICATION, BIF.strMobilePhone(4),
                BIF.strPhone(0, 2), BIF.strEmail(), listStreet.get(random.nextInt(m_nStreetSize))
                , BIF.strGeographicalLevel(), basic_id));
        
        listSQL.add(String.format(SqlHandler.SQL_JOB, basic_id, BIF.strJob(), BIF.strJobTitle(),
                BIF.strCustomHis(), BIF.strIncome()));
        
        listSQL.add(String.format(SqlHandler.SQL_ACCOUNT, basic_id, BIF.strSaveMoney(),
                BIF.strTransferNote(), BIF.strATM(), BIF.strLoginBank(), BIF.strIP(),
                BIF.strLoginBankWebTime()));
        
        listSQL.add(String.format(SqlHandler.SQL_LOAN, basic_id, BIF.strTheNumOfHisCards(),
                BIF.strCreditRating(), BIF.strBuilding(), BIF.strBuildingInch(),
                BIF.strCategorical(), BIF.strComeFrom(), BIF.strBadRecord(), BIF.strDelayDate()));
        
        listSQL.add(String.format(SqlHandler.SQL_CONSUMPTION, basic_id, BIF.strMCC(),
                BIF.strMCC(), BIF.strSaleType(), BIF.strShopingInfo(),
                BIF.strDepartmentTransRatio(), BIF.strCreditOnlineTransRatio(),
                BIF.strLatitudeAndLongitude()));
        
        listSQL.add(String.format(SqlHandler.SQL_OTHER_COMMUNICATION, strUUID,
                BIF.strUUID(0), basic_id, BIF.strRegion(), BIF.strTaiwanCity(), BIF.strOS(0),
                BIF.strOsVersion(0), BIF.strMobileModel(9), BIF.strManufacturer()));
        
        listSQL.add(String.format(SqlHandler.SQL_APP, basic_id, BIF.subscriptStatus(),
                BIF.intLoginStatus(), BIF.strLanguage(0), BIF.strAppInstallTime(),
                BIF.strRegion(), BIF.queryType(), BIF.isContact(), BIF.bizCategory(),
                BIF.isSpam(), BIF.spamCategory()));
    }
    
    private String getBasicId(String strUUID) throws SQLException
    {
        String strBasicId = null;
        
        ResultSet tmpNID = mysqlHandler.executeQuery(String.format(SqlHandler.SQL_PERSONAL_ID,
                strUUID));
        if (tmpNID.next())
        {
            strBasicId = tmpNID.getString("id");
        }
        return strBasicId;
    }
}
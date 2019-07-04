import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Jugo on 2019/7/3
 */

class HuananDataGenerator
{
    private SQLiteDataSource sqLiteDataSource;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    HuananDataGenerator()
    {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);
        sqLiteDataSource = new SQLiteDataSource(config);
    }
    
    int init(String strSQLitePath)
    {
        sqLiteDataSource.setUrl("jdbc:sqlite:" + strSQLitePath);
        try
        {
            if (null != (connection = sqLiteDataSource.getConnection()))
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
            statement.execute("BEGIN");
            statement.execute("COMMIT");
            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
    }
    
}

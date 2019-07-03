import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Jugo on 2019/7/3
 */

class HuananDataGenerator
{
    private SQLiteDataSource sqLiteDataSource;
    private Connection con;
    
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
            con = sqLiteDataSource.getConnection();
            if (null != con)
            {
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
    
        
    }
    
}

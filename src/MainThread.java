import org.iii.modules.ConfigHandler;

/**
 * Created by Jugo on 2019/6/30
 */

class MainThread
{
    public static void main(String[] args)
    {
        String strTarget;
        String mysqlIP;
        String mysqlAccount;
        String mysqlPassword;
        String mysqlDB;
        String sqlitePath;
        int mysqlPort;
        int threadCount;
        int recordCount;
        
        ConfigHandler configHandler = new ConfigHandler("dataGenerator.conf");
        strTarget = configHandler.getProperty("TARGET");
        threadCount = Integer.valueOf(configHandler.getProperty("THREAD_COUNT"));
        recordCount = Integer.valueOf(configHandler.getProperty("RECORD_COUNT"));
        
        if (0 == strTarget.compareTo("MYSQL"))
        {
            mysqlIP = configHandler.getProperty("MYSQL_IP");
            mysqlAccount = configHandler.getProperty("MYSQL_ACCOUNT");
            mysqlPassword = configHandler.getProperty("MYSQL_PASSWORD");
            mysqlDB = configHandler.getProperty("MYSQL_DB");
            mysqlPort = Integer.valueOf(configHandler.getProperty("MYSQL_PORT"));
            
            
            System.out.println("MYSQL IP:" + mysqlIP + "\nMYSQL PORT:" + mysqlPort + "\nMYSQL " + "ACCOUNT:" + mysqlAccount + "\nMYSQL PASSWORD:" + mysqlPassword + "\nMYSQL DB:" + mysqlDB + "\nTHREAD COUNT:" + threadCount + "\nRECORD COUNT:" + recordCount);
            
            if (null == mysqlIP || null == mysqlAccount || null == mysqlPassword || null == mysqlDB || 0 >= mysqlPort || 0 >= threadCount || 0 >= recordCount)
            {
                System.out.println("Configuration Parameter Error!!");
                return;
            }
            
            for (int i = 1; i <= threadCount; ++i)
            {
                new DataGenThread(i).init(mysqlIP, mysqlPort, mysqlAccount, mysqlPassword,
                        mysqlDB, recordCount).start();
            }
        }
        
        if (0 == strTarget.compareTo("SQLITE"))
        {
            sqlitePath = configHandler.getProperty("SQLITE_PATH");
            System.out.println("SQLITE PATH:" + sqlitePath + "\nTHREAD COUNT:" + threadCount +
                    "\nRECORD COUNT:" + recordCount);
            for (int i = 1; i <= threadCount; ++i)
            {
                new HuananGenThread(i).init(sqlitePath, recordCount).start();
            }
        }
    }
    
    static class DataGenThread extends Thread
    {
        private int tid;
        String m_strIP;
        int m_nPort;
        String m_strAccount;
        String m_strPassword;
        String m_strDB;
        int m_nRecordCount;
        
        DataGenThread(int x)
        {
            tid = x;
        }
        
        DataGenThread init(String strIP, int nPort, String strAccount, String strPassword,
                String strDB, int nRecordCount)
        {
            m_strIP = strIP;
            m_nPort = nPort;
            m_strAccount = strAccount;
            m_strPassword = strPassword;
            m_strDB = strDB;
            m_nRecordCount = nRecordCount;
            return this;
        }
        
        public void run()
        {
            System.out.println("Hello I'm Thread " + tid + " I will generate " + m_nRecordCount + " records\n");
            DataGenerator dataGenerator = new DataGenerator();
            if (0 == dataGenerator.init(m_strIP, m_nPort, m_strAccount, m_strPassword, m_strDB))
            {
                dataGenerator.generate(tid, m_nRecordCount);
            }
        }
    }
    
    static class HuananGenThread extends Thread
    {
        private int tid;
        private String m_strDBPath;
        private int m_nRecordCount;
        
        HuananGenThread(int x)
        {
            tid = x;
        }
        
        HuananGenThread init(String strDBPath, int nRecordCount)
        {
            m_strDBPath = strDBPath;
            m_nRecordCount = nRecordCount;
            return this;
        }
        
        public void run()
        {
            System.out.println("Hello I'm Thread " + tid + " I will generate " + m_nRecordCount + " records\n");
            HuananDataGenerator huananDataGenerator = new HuananDataGenerator();
            if (0 == huananDataGenerator.init(m_strDBPath))
            {
                huananDataGenerator.generate(tid, m_nRecordCount);
            }
        }
    }
}

package org.iii.modules;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Y.C Tsai
 * @project dataGenerator
 * @Date 2019/6/10
 */
public class MysqlHandler
{
    //data members
    private String dbName;
    private String ip;
    private int port;
    private String loginName;
    private String loginPassword;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs;
    
    //define constructor
    public MysqlHandler(String ip, int port, String loginName, String loginPassword,
            String dbName)
    {
        init(ip, port, loginName, loginPassword, dbName);
    }
    
    public MysqlHandler()
    {
    }
    
    public void init(String ip, int port, String loginName, String loginPassword, String dbName)
    {
        this.ip = ip;
        this.port = port;
        this.loginName = loginName;
        this.loginPassword = loginPassword;
        this.dbName = dbName;
    }
    
    
    //function members
    public Connection getConn()
    {
        return conn;
    }
    
    public Statement getStmt()
    {
        return stmt;
    }
    
    public String getDbName()
    {
        return dbName;
    }
    
    public String getIp()
    {
        return ip;
    }
    
    public Integer getPort()
    {
        return port;
    }
    
    public String getLoginName()
    {
        return loginName;
    }
    
    public String getLoginPassword()
    {
        return loginPassword;
    }
    
    public void setIp(String value)
    {
        this.ip = value;
    }
    
    public void setDbName(String value)
    {
        this.dbName = value;
    }
    
    public void setLoginName(String value)
    {
        this.loginName = value;
    }
    
    public void setLoginPassword(String value)
    {
        this.loginPassword = value;
    }
    
    public void setPort(int value)
    {
        this.port = value;
    }
    
    public Connection start()
    {
        System.out.println("Mysql ready to start connect...");
        String dbUrl = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useUnicode=yes" +
                "&characterEncoding=UTF-8";
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(dbUrl, loginName, loginPassword);
            stmt = (Statement) conn.createStatement();
        }
        catch (ClassNotFoundException | SQLException ce)
        {
            conn = null;
            stmt = null;
            ce.printStackTrace();
        }
        System.out.println("Connect successfully.");
        
        return conn;
    }
    
    public Connection close()
    {
        System.out.println("Mysql ready to close connect...");
        try
        {
            conn.close();
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        System.out.println("connect close successfully.");
        return conn;
    }
    
    /***
     *執行sql query並將結果以ResultSet之資料型態存放回傳
     * @param sql SQL語法
     * @return ResultSet
     */
    public ResultSet executeQuery(String sql)
    {
        try
        {
//            stmt = (Statement) conn.createStatement();
            rs = stmt.executeQuery(sql);
        }
        catch (SQLException se)
        {
            rs = null;
            se.printStackTrace();
            System.out.println("sql executeQuery Exception : " + se.getMessage());
        }
        //System.out.println("The object ready to be execute and the result will be stored into
        // ResultSet.");
        return rs;
    }
    
    public Integer execute(String sql)
    {
        try
        {
            stmt.execute(sql);
        }
        catch (SQLException se)
        {
            if(se.getMessage().contains("Duplicate"))
            {
                System.out.println("sql execute Exception : " + se.getMessage());
            }
            else
            {
                se.printStackTrace();
            }
            return -1;
        }
        return 0;
    }
    
    /***
     *執行SQL update
     * @param sql SQL語法
     * @return 0:成功;-1:失敗
     */
    public Integer executeUpdate(String sql)
    {
        try
        {
//            stmt = (Statement) conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            System.out.println("sql update Exception : " + se.getMessage());
            return -1;
        }
        //       System.out.println("sql update successfully.");
        return 0;
    }
    
    /***
     *顯示ResultSet中的所有資料,並將資料轉存至container裡面
     * @param rs jdbc所定義的資料型態(存放query資料用)
     * @param tableResult 使用者自定義存放ResultSet的容器
     * @return 0:成功;-1:SQL錯誤
     */
    public Integer displayAll(ResultSet rs, ArrayList<Map<String, String>> tableResult)
    {
        String clmname = null;
        String clmtype = null;
        ResultSetMetaData rsmd = null;
        int clmsize = 0;
        
        ArrayList<String> clmnameList = new ArrayList<>();
        ArrayList<String> clmtypeList = new ArrayList<>();
        ArrayList<Integer> clmsizeList = new ArrayList<>();
        Map<String, String> clmvalue = new HashMap<>();
//        ArrayList<Map<String, String>> tableResult = new ArrayList<>();
        
        //get table info(column name/type/size)
        try
        {
            rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                clmname = rsmd.getColumnName(i);
                clmtype = rsmd.getColumnTypeName(i);
                clmsize = rsmd.getColumnDisplaySize(i);
                clmnameList.add(clmname);
                clmtypeList.add(clmtype);
                clmsizeList.add(clmsize);
//                System.out.println(clmname);
//                System.out.println(clmtype);
//                System.out.println(clmsize);
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return -1;
        }
        System.out.println("clmname: " + clmnameList);
        System.out.println("clmtype: " + clmtypeList);
        System.out.println("clmsize: " + clmsizeList);
        try
        {
            while (rs.next())
            {
                for (String colElement : clmnameList)
                {
                    String tableName = rs.getString(colElement);
                    clmvalue.put(tableName, colElement);
                    System.out.println(colElement + ":" + tableName);
                }
                tableResult.add(clmvalue);
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return -1;
        }
        return 0;
    }
    
    public Integer truncatedTable(String tableName)
    {
        try
        {
//            stmt = (Statement) conn.createStatement();
            stmt.executeUpdate(String.format("truncate %s", tableName));
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return -1;
        }
        System.out.println(String.format("Table: %s was truncated.", tableName));
        return 0;
    }
    
    public Integer getTableName(ArrayList<String> tableNameResult, Boolean show)
    {
        try
        {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            
            while (rs.next())
            {
                if (show)
                {
                    System.out.println(rs.getString(3));
                }
                tableNameResult.add(rs.getString(3));
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return -1;
        }
        return 0;
    }
    
    public Integer getColumnName(String tableName, ArrayList<String> columnNameResult, Boolean show)
    {
        try
        {
//            stmt = (Statement) conn.createStatement();
            rs = stmt.executeQuery(String.format("SELECT * FROM %s", tableName));
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                String firstColumnName = rsmd.getColumnName(i);
                columnNameResult.add(firstColumnName);
                if (show)
                {
                    System.out.println(firstColumnName);
                }
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return -1;
        }
        return 0;
    }
    
    public Integer executeBatch(String sql, Boolean commit)
    {
        try
        {
//            conn.setAutoCommit(false);
            stmt.addBatch(sql);
            if (commit)
            {
                stmt.executeBatch();
                conn.commit();
                stmt.clearBatch();
//                conn.setAutoCommit(true);
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return -1;
        }
        return 0;
    }


//    protected abstract void query();
}

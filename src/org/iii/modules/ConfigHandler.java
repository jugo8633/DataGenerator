package org.iii.modules;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Jugo on 2019/6/29
 */

public class ConfigHandler
{
    private Properties configFile;
    
    public ConfigHandler(String strConf)
    {
        configFile = new java.util.Properties();
        try
        {
            InputStream in = new BufferedInputStream(new FileInputStream(strConf));
            configFile.load(in);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public String getProperty(String key)
    {
        return this.configFile.getProperty(key);
    }
}

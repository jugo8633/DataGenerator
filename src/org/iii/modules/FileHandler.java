package org.iii.modules;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.time.Instant;

/**
 * Created by Jugo on 2019/7/6
 */

public class FileHandler
{
    public FileHandler()
    {
    
    }
    
    public LineIterator loadFile(String strPath, String strWordCoding)
    {
        int nResult = 0;
        int lines = 0;
        File f = new File(strPath);
        LineIterator it = null;
        
        try
        {
            it = FileUtils.lineIterator(f, strWordCoding);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return it;
    }
    
}

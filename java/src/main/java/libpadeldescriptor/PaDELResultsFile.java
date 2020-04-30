package libpadeldescriptor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaDELResultsFile {
    private FileOutputStream fos = null;
    private PrintStream ps = null;
    
    public PaDELResultsFile()
    {

    }
    
    public String Open(String filename)
    {
        Close();
        try
        {
            fos = new FileOutputStream(filename);
            ps = new PrintStream(fos);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger("global").log(Level.SEVERE, "Cannot create results file", ex);
            return "Cannot create results file.";
        }
        return null;
    }

    public void Close()
    {
        if (fos!=null)
        {
            try
            {
                fos.close();
                fos = null;
                ps = null;
            }
            catch (IOException ex)
            {
                Logger.getLogger("global").log(Level.FINE, "Cannot close results file", ex);
            }
        }
    }

    public void Write(String text)
    {
        ps.print(text);
    }
}
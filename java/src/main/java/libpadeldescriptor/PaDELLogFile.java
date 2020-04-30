package libpadeldescriptor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaDELLogFile {
    private FileOutputStream fos = null;
    private PrintStream ps = null;
    private boolean useLog = true;

    public PaDELLogFile()
    {

    }

    public String Open(String filename, boolean useLog)
    {
        Close();
        this.useLog = useLog;
        if (useLog)
        {
            try
            {
                fos = new FileOutputStream(filename);
                ps = new PrintStream(fos);
                CaptureStdErr();
            } 
            catch (FileNotFoundException ex)
            {
                Logger.getLogger("global").log(Level.SEVERE, "Cannot create log file", ex);
                return "Cannot create log file.";
            }
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
                Logger.getLogger("global").log(Level.FINE, "Cannot close log file", ex);
            }
        }
    }

    public void CaptureStdErr()
    {
        System.setErr(ps);
    }

    public void Write(String text)
    {
        if (useLog) ps.print(text);
    }
}
package libpadeldescriptor;

import java.util.ArrayList;
import libpadeljobs.Job;
import org.openscience.cdk1.interfaces.IAtomContainer;

/**
 * A job to calculate molecular descriptor
 * 
 * @author yapchunwei
 */
public class libPaDELDescriptorJob extends Job
{
    protected String name = null;
    protected String filename = null;
    protected IAtomContainer structure = null;
    protected ArrayList<String> descriptors = null;
    protected boolean removeSalt = true;
    protected boolean detectAromaticity = true;
    protected boolean standardizeTautomers = true;
    protected boolean standardizeNitro = true;
    protected boolean retain3D = false;
    protected boolean convertTo3D = false;
    protected String forcefield = MM2;
    public static final String MM2 = "mm2";
    public static final String MMFF94 = "mmff94";
    protected String[] tautomerList;
    protected long maxRunTime;
    
    libPaDELDescriptorJob(int id)
    {
        super(id);
    }

    libPaDELDescriptorJob(int id, String filename, String name, IAtomContainer structure)
    {
        this(id, filename, name, structure, true, true, true, null, true, false, false, MM2, -1);
    }

    libPaDELDescriptorJob(int id, String filename, String name, IAtomContainer structure, 
                          boolean removeSalt, 
                          boolean detectAromaticity, 
                          boolean standardizeTautomers, String[] tautomerList, 
                          boolean standardizeNitro,
                          boolean retain3D,
                          boolean convertTo3D, String forcefield,
                          long maxRunTime)
    {
        super(id);
        this.name = name;
        this.filename = filename;
        this.structure = structure;
        this.removeSalt = removeSalt;
        this.detectAromaticity = detectAromaticity;
        this.standardizeTautomers = standardizeTautomers;
        this.tautomerList = tautomerList;
        this.standardizeNitro = standardizeNitro;
        this.retain3D = retain3D;
        this.convertTo3D = convertTo3D;
        this.forcefield = forcefield;
        this.maxRunTime = maxRunTime;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public IAtomContainer getStructure()
    {
        return structure;
    }

    public void setStructure(IAtomContainer structure)
    {
        this.structure = structure;
    }

    public ArrayList<String> getDescriptors()
    {     
        return descriptors;
    }

    public void setDescriptors(ArrayList<String> descriptors)
    {
        this.descriptors = descriptors;
    }

    public boolean isRemoveSalt()
    {
        return removeSalt;
    }

    public void setRemoveSalt(boolean removeSalt)
    {
        this.removeSalt = removeSalt;
    }
    
    public boolean isDetectAromaticity()
    {
        return detectAromaticity;
    }

    public void setDetectAromaticity(boolean detectAromaticity)
    {
        this.detectAromaticity = detectAromaticity;
    }
        
    public boolean isStandardizeTautomers()
    {
        return standardizeTautomers;
    }

    public void setStandardizeTautomers(boolean standardizeTautomers)
    {
        this.standardizeTautomers = standardizeTautomers;
    }
    
    public String[] getTautomerList()
    {
        return tautomerList;
    }
    
    public void setTautomerList(String[] tautomerList)
    {
        this.tautomerList = tautomerList;
    }
    
    public boolean isStandardizeNitro()
    {
        return standardizeNitro;
    }

    public void setStandardizeNitro(boolean standardizeNitro)
    {
        this.standardizeNitro = standardizeNitro;
    }
    
    public boolean isRetain3D()
    {
        return retain3D;
    }

    public void setRetain3D(boolean retain3D)
    {
        this.retain3D = retain3D;
    }

    public boolean isConvertTo3D()
    {
        return convertTo3D;
    }

    public void setConvertTo3D(boolean convertTo3D)
    {
        this.convertTo3D = convertTo3D;
    }

    public String getForcefield()
    {
        return forcefield;
    }

    public void setForcefield(String forcefield)
    {
        this.forcefield = forcefield;
    }
    
    public long getMaxRunTime()
    {
        return maxRunTime;
    }

    public void setMaxRunTime(long maxRunTime)
    {
        this.maxRunTime = maxRunTime;
    }
}

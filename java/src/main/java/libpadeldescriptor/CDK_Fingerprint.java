package libpadeldescriptor;

import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.fingerprint.IFingerprinter;
import org.openscience.cdk1.interfaces.IAtomContainer;

/**
 * Base class for making CDK fingerprint classes runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_Fingerprint extends Thread
{
    protected IFingerprinter cdkFingerprinter_;
    protected IAtomContainer molecule_;
    protected String[] descriptorNames_ = null;
    public String[] descriptorValues_;
    protected String prefix_;
    protected String errorCode_ = "";
    protected int maxFingerprints_ = 0;

    public CDK_Fingerprint()
    {
        
    }

    /**
     * Implements run() in interface Runnable.
     */
    @Override
    public void run() 
    {
        try
        {
            initDescriptorsValues();
            BitSet fingerprint = cdkFingerprinter_.getFingerprint(molecule_);
            String one = Integer.toString(1);
            String zero = Integer.toString(0);
            int maxFingerprints = getFingerprintCount();
            for (int i=0; i<maxFingerprints; ++i)
            {
                if (fingerprint.get(i)==true) descriptorValues_[i] = one;
                else if (fingerprint.get(i)==false) descriptorValues_[i] = zero;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger("global").log(Level.SEVERE, "Fingerprinter error", ex);
        }
    }
    
    @Override
    public void interrupt()
    {
        molecule_ = null;
        this.stop();
        super.interrupt();
    }
    
    public void initDescriptorsValues()
    {
        int maxFingerprints = getFingerprintCount();
        descriptorValues_ = new String[maxFingerprints];
        for (int i=0; i<maxFingerprints; ++i)
        {
            descriptorValues_[i] = errorCode_;
        }
    }

    /**
     *
     */
    public int getFingerprintCount()
    {
        if (maxFingerprints_ == 0)
        {
            maxFingerprints_ = cdkFingerprinter_.getSize();
        }
        return maxFingerprints_;
    }

    /**
     * 
     * @return List of descriptor names.
     */
    public String[] getDescriptorNames()
    {
        if (descriptorNames_ == null)
        {
            int maxFingerprints = getFingerprintCount();
            descriptorNames_ = new String[maxFingerprints];
            for (int i=0; i<maxFingerprints; ++i)
            {
                descriptorNames_[i] = prefix_ + Integer.toString(i+1);
            }
        }
     
        return descriptorNames_;
    }
    
    /**
     * 
     * @param molecule Molecule for calculating descriptors.
     */
    public void setMolecule(IAtomContainer molecule) 
    {
        this.molecule_ = molecule;
        initDescriptorsValues();
    }

    /**
     * 
     * @param prefix Prefix to identify fingerprints type.
     */
    public void setPrefix(String prefix) 
    {
        this.prefix_ = prefix;
    }

    /**
     *
     * @param errorCode Error code to insert if descriptor value cannot be calculated.
     */
    public void setErrorCode(String errorCode)
    {
        this.errorCode_ = errorCode;
    }
   
   public String[] getDescriptorValues()
   {
     return descriptorValues_.clone();
  }
}


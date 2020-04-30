package libpadeldescriptor;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;

/**
 * Base class for making CDK descriptor classes runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_Descriptor extends Thread
{
    protected IMolecularDescriptor cdkDescriptor_;
    protected IAtomContainer molecule_;
    protected String[] descriptorValues_;
    protected String errorCode_ = "";
    protected int maxDescriptors_ = 0;

    public CDK_Descriptor()
    {
        
    }

    /**
     * Implements run() in interface Runnable.
     */
    @Override
    public void run() 
    {
        initDescriptorsValues();
        DescriptorValue descriptor = cdkDescriptor_.calculate(molecule_);
        if (descriptor.getException()!=null)
        {
            Logger.getLogger("global").log(Level.FINE, cdkDescriptor_.toString() + ":" + descriptor.getException().toString());
        }
        String valueStr[] = descriptor.getValue().toString().split(",");

        int maxDescriptors = getDescriptorCount();
        for (int i=0; i<maxDescriptors; ++i)
        {
            if (!valueStr[i].equals("NaN")) descriptorValues_[i] = valueStr[i];
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
        int maxDescriptors = getDescriptorCount();
        descriptorValues_ = new String[maxDescriptors];
        for (int i=0; i<maxDescriptors; ++i)
        {
            descriptorValues_[i] = errorCode_;
        }
    }

    /**
     *
     */
    public int getDescriptorCount()
    {
        if (maxDescriptors_ == 0)
        {
            maxDescriptors_ = cdkDescriptor_.getDescriptorResultType().length();
        }
        return maxDescriptors_;
    }

    /**
     *
     * @return List of descriptor names.
     */
    public String[] getDescriptorNames()
    {
        return cdkDescriptor_.getDescriptorNames();
    }

    /**
     *
     * @return List of descriptor values.
     */
    public String[] getDescriptorValues()
    {
        return descriptorValues_.clone();
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
     * @param errorCode Error code to insert if descriptor value cannot be calculated.
     */
    public void setErrorCode(String errorCode) 
    {
        this.errorCode_ = errorCode;
    }
    
    /**
     * 
     * @param params Parameters for the CDK descriptor class.
     * @throws org.openscience.cdk1.exception.CDKException
     */
    public void setParameters(Object[] params) throws CDKException
    {
        cdkDescriptor_.setParameters(params);
    }
}

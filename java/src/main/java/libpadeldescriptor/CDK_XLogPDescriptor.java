package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.XLogPDescriptor;

/**
 * Make XLogPDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_XLogPDescriptor extends CDK_Descriptor
{
    public CDK_XLogPDescriptor()
    {
        super();
        cdkDescriptor_ = new XLogPDescriptor();
        initDescriptorsValues();
    }
    
    /**
     * 
     * @param params Parameters for the CDK descriptor class.
     */
    public CDK_XLogPDescriptor(Object[] params)
    {
        super();
        cdkDescriptor_ = new XLogPDescriptor();
        initDescriptorsValues();
        try
        {
            cdkDescriptor_.setParameters(params);
        }
        catch (Exception e) 
        {
            System.out.println(e.toString());
        }
    }
}

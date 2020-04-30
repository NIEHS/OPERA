package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.AtomCountDescriptor;

/**
 * Make AtomCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_AtomCountDescriptor extends CDK_Descriptor
{
    public CDK_AtomCountDescriptor()
    {
        super();
        cdkDescriptor_ = new AtomCountDescriptor();
        initDescriptorsValues();
    }
    
    /**
     * 
     * @param params Parameters for the CDK descriptor class.
     */
    public CDK_AtomCountDescriptor(Object[] params)
    {
        super();
        cdkDescriptor_ = new AtomCountDescriptor();
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

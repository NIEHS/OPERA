package libpadeldescriptor;

/**
 * Make ExtendedTopochemicalAtomDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_ExtendedTopochemicalAtomDescriptor extends CDK_Descriptor
{
    public CDK_ExtendedTopochemicalAtomDescriptor()
    {
        super();
        cdkDescriptor_ = new ExtendedTopochemicalAtomDescriptor();
        initDescriptorsValues();
    }
    
    /**
     * 
     * @param params Parameters for the CDK descriptor class.
     */
    public CDK_ExtendedTopochemicalAtomDescriptor(Object[] params)
    {
        super();
        cdkDescriptor_ = new ExtendedTopochemicalAtomDescriptor();
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

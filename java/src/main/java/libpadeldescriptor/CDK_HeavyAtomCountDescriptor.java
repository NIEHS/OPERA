package libpadeldescriptor;

/**
 * Make HeavyAtomCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_HeavyAtomCountDescriptor extends CDK_Descriptor
{
    public CDK_HeavyAtomCountDescriptor()
    {
        super();
        cdkDescriptor_ = new HeavyAtomCountDescriptor();
        initDescriptorsValues();
    }
}

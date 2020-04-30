package libpadeldescriptor;

/**
 * Make RingCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_RingCountDescriptor extends CDK_Descriptor
{
    public CDK_RingCountDescriptor()
    {
        super();
        cdkDescriptor_ = new RingCountDescriptor();
        initDescriptorsValues();
    }
}

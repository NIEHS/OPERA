package libpadeldescriptor;

/**
 * Make HalogenCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_HalogenCountDescriptor extends CDK_Descriptor
{
    public CDK_HalogenCountDescriptor()
    {
        super();
        cdkDescriptor_ = new HalogenCountDescriptor();
        initDescriptorsValues();
    }
}

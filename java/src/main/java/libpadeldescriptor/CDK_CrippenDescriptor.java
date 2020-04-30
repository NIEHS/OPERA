package libpadeldescriptor;

/**
 * Make CrippenDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_CrippenDescriptor extends CDK_Descriptor
{
    public CDK_CrippenDescriptor()
    {
        super();
        cdkDescriptor_ = new CrippenDescriptor();
        initDescriptorsValues();
    }
}

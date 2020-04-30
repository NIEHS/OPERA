package libpadeldescriptor;

/**
 * Make WalkCountDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_WalkCountDescriptor extends CDK_Descriptor
{
    public CDK_WalkCountDescriptor()
    {
        super();
        cdkDescriptor_ = new WalkCountDescriptor();
        initDescriptorsValues();
    }
}

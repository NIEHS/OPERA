package libpadeldescriptor;

/**
 * Make PathCountDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_PathCountDescriptor extends CDK_Descriptor
{
    public CDK_PathCountDescriptor()
    {
        super();
        cdkDescriptor_ = new PathCountDescriptor();
        initDescriptorsValues();
    }
}

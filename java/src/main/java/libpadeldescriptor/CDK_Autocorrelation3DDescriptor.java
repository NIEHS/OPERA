package libpadeldescriptor;

/**
 * Make Autocorrelation3DDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_Autocorrelation3DDescriptor extends CDK_Descriptor
{
    public CDK_Autocorrelation3DDescriptor()
    {
        super();
        cdkDescriptor_ = new Autocorrelation3DDescriptor();
        initDescriptorsValues();
    }
}

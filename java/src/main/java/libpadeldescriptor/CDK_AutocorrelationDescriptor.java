package libpadeldescriptor;

/**
 * Make AutocorrelationDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_AutocorrelationDescriptor extends CDK_Descriptor
{
    public CDK_AutocorrelationDescriptor()
    {
        super();
        cdkDescriptor_ = new AutocorrelationDescriptor();
        initDescriptorsValues();
    }
}

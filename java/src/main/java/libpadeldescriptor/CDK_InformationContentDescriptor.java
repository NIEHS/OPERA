package libpadeldescriptor;

/**
 * Make InformationContentDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_InformationContentDescriptor extends CDK_Descriptor
{
    public CDK_InformationContentDescriptor()
    {
        super();
        cdkDescriptor_ = new InformationContentDescriptor();
        initDescriptorsValues();
    }
}

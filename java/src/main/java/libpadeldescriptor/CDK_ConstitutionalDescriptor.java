package libpadeldescriptor;

/**
 * Make ConstitutionalDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_ConstitutionalDescriptor extends CDK_Descriptor
{
    public CDK_ConstitutionalDescriptor()
    {
        super();
        cdkDescriptor_ = new ConstitutionalDescriptor();
        initDescriptorsValues();
    }
}

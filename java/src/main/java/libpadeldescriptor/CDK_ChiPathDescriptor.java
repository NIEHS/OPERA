package libpadeldescriptor;

/**
 * Make PaDELChiPathDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_ChiPathDescriptor extends CDK_Descriptor
{
    public CDK_ChiPathDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELChiPathDescriptor();
        initDescriptorsValues();
    }
}

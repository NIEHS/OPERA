package libpadeldescriptor;

/**
 * Make RDFDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_RDFDescriptor extends CDK_Descriptor
{
    public CDK_RDFDescriptor()
    {
        super();
        cdkDescriptor_ = new RDFDescriptor();
        initDescriptorsValues();
    }
}

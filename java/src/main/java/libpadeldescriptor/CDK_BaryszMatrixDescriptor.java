package libpadeldescriptor;

/**
 * Make BaryszMatrixDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_BaryszMatrixDescriptor extends CDK_Descriptor
{
    public CDK_BaryszMatrixDescriptor()
    {
        super();
        cdkDescriptor_ = new BaryszMatrixDescriptor();
        initDescriptorsValues();
    }
}

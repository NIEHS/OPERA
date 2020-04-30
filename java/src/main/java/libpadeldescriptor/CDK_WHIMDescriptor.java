package libpadeldescriptor;

/**
 * Make PaDELWHIMDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_WHIMDescriptor extends CDK_Descriptor
{
    public CDK_WHIMDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELWHIMDescriptor();
        initDescriptorsValues();
    }
}

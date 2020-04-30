package libpadeldescriptor;

/**
 * Make BurdenModifiedEigenvaluesDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_BurdenModifiedEigenvaluesDescriptor extends CDK_Descriptor
{
    public CDK_BurdenModifiedEigenvaluesDescriptor()
    {
        super();
        cdkDescriptor_ = new BurdenModifiedEigenvaluesDescriptor();
        initDescriptorsValues();
    }
}

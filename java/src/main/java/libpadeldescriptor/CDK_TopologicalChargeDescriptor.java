package libpadeldescriptor;

/**
 * Make TopologicalChargeDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_TopologicalChargeDescriptor extends CDK_Descriptor
{
    public CDK_TopologicalChargeDescriptor()
    {
        super();
        cdkDescriptor_ = new TopologicalChargeDescriptor();
        initDescriptorsValues();
    }
}

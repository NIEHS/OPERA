package libpadeldescriptor;

/**
 * Make TopologicalDistanceMatrixDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_TopologicalDistanceMatrixDescriptor extends CDK_Descriptor
{
    public CDK_TopologicalDistanceMatrixDescriptor()
    {
        super();
        cdkDescriptor_ = new TopologicalDistanceMatrixDescriptor();
        initDescriptorsValues();
    }
}

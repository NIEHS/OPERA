package libpadeldescriptor;

/**
 * Make TopologicalDistanceDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_TopologicalDescriptor extends CDK_Descriptor
{
    public CDK_TopologicalDescriptor()
    {
        super();
        cdkDescriptor_ = new TopologicalDescriptor();
        initDescriptorsValues();
    }
}

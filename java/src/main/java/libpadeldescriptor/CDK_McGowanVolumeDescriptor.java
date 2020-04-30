package libpadeldescriptor;

/**
 * Make McGowanVolumeDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_McGowanVolumeDescriptor extends CDK_Descriptor
{
    public CDK_McGowanVolumeDescriptor()
    {
        super();
        cdkDescriptor_ = new McGowanVolumeDescriptor();
        initDescriptorsValues();
    }
}

package libpadeldescriptor;

/**
 * Make HBondDonorCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_HBondDonorCountDescriptor extends CDK_Descriptor
{
    public CDK_HBondDonorCountDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELHBondDonorCountDescriptor();
        initDescriptorsValues();
    }
}

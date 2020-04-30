package libpadeldescriptor;

/**
 * Make RotatableBondsCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_RotatableBondsCountDescriptor extends CDK_Descriptor
{
    public CDK_RotatableBondsCountDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELRotatableBondsCountDescriptor();
        initDescriptorsValues();
    }
}

package libpadeldescriptor;

/**
 * Make PaDELWeightDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_WeightDescriptor extends CDK_Descriptor
{
    public CDK_WeightDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELWeightDescriptor();
        initDescriptorsValues();
    }
}

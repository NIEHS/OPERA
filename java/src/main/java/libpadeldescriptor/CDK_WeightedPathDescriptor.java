package libpadeldescriptor;

/**
 * Make PaDELWeightedPathDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_WeightedPathDescriptor extends CDK_Descriptor
{
    public CDK_WeightedPathDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELWeightedPathDescriptor();
        initDescriptorsValues();
    }
}

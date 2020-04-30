package libpadeldescriptor;

/**
 * Make CarbonTypesDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_CarbonTypesDescriptor extends CDK_Descriptor
{
    public CDK_CarbonTypesDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELCarbonTypesDescriptor();
        initDescriptorsValues();
    }
}

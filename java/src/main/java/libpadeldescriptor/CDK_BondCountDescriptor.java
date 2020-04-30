package libpadeldescriptor;

/**
 * Make BondCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_BondCountDescriptor extends CDK_Descriptor
{
    private String order = "";

    public CDK_BondCountDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELBondCountDescriptor();
        initDescriptorsValues();
    }
}

package libpadeldescriptor;

/**
 * Make HBondAcceptorCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_HBondAcceptorCountDescriptor extends CDK_Descriptor
{
    public CDK_HBondAcceptorCountDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELHBondAcceptorCountDescriptor();
        initDescriptorsValues();
    }
}

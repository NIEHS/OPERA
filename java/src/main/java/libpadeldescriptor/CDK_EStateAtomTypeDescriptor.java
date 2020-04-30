package libpadeldescriptor;

/**
 * Make EStateAtomTypeDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_EStateAtomTypeDescriptor extends CDK_Descriptor
{
    public CDK_EStateAtomTypeDescriptor()
    {
        super();
        cdkDescriptor_ = new EStateAtomTypeDescriptor();
        initDescriptorsValues();
    }
}

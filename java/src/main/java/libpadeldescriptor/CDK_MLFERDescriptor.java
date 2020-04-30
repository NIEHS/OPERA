package libpadeldescriptor;

/**
 * Make MLFERDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_MLFERDescriptor extends CDK_Descriptor
{
    public CDK_MLFERDescriptor()
    {
        super();
        cdkDescriptor_ = new MLFERDescriptor();
        initDescriptorsValues();
    }
}

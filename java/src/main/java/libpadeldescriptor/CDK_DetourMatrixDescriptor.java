package libpadeldescriptor;

/**
 * Make DetourMatrixDescriptor in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_DetourMatrixDescriptor extends CDK_Descriptor
{
    public CDK_DetourMatrixDescriptor()
    {
        super();
        cdkDescriptor_ = new DetourMatrixDescriptor();
        initDescriptorsValues();
    }
}

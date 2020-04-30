package libpadeldescriptor;

/**
 * Make FragmentComplexityDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_FragmentComplexityDescriptor extends CDK_Descriptor
{
    public CDK_FragmentComplexityDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELFragmentComplexityDescriptor();
        initDescriptorsValues();
    }
}

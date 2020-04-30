package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.LargestPiSystemDescriptor;

/**
 * Make LargestPiSystemDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_LargestPiSystemDescriptor extends CDK_Descriptor
{
    public CDK_LargestPiSystemDescriptor()
    {
        super();
        cdkDescriptor_ = new LargestPiSystemDescriptor();
        initDescriptorsValues();
    }
}

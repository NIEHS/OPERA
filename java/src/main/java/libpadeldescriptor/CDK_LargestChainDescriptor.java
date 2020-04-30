package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.LargestChainDescriptor;

/**
 * Make LargestChainDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_LargestChainDescriptor extends CDK_Descriptor
{
    public CDK_LargestChainDescriptor()
    {
        super();
        cdkDescriptor_ = new LargestChainDescriptor();
        initDescriptorsValues();
    }
}

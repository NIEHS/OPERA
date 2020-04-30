package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.LongestAliphaticChainDescriptor;

/**
 * Make LongestAliphaticChainDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_LongestAliphaticChainDescriptor extends CDK_Descriptor
{
    public CDK_LongestAliphaticChainDescriptor()
    {
        super();
        cdkDescriptor_ = new LongestAliphaticChainDescriptor();
        initDescriptorsValues();
    }
}

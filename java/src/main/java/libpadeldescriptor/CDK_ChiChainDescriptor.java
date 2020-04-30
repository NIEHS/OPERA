package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.ChiChainDescriptor;

/**
 * Make ChiChainDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_ChiChainDescriptor extends CDK_Descriptor
{
    public CDK_ChiChainDescriptor()
    {
        super();
        cdkDescriptor_ = new ChiChainDescriptor();
        initDescriptorsValues();
    }
}

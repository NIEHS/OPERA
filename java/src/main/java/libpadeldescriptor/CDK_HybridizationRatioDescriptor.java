package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.HybridizationRatioDescriptor;

/**
 * Make HybridizationRatioDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_HybridizationRatioDescriptor extends CDK_Descriptor
{
    public CDK_HybridizationRatioDescriptor()
    {
        super();
        cdkDescriptor_ = new HybridizationRatioDescriptor();
        initDescriptorsValues();
    }
}

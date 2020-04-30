package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.APolDescriptor;

/**
 * Make APolDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_APolDescriptor extends CDK_Descriptor
{
    public CDK_APolDescriptor()
    {
        super();
        cdkDescriptor_ = new APolDescriptor();
        initDescriptorsValues();
    }
}

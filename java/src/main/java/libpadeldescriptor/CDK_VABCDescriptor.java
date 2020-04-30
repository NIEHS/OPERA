package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.VABCDescriptor;

/**
 * Make ALOGPDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_VABCDescriptor extends CDK_Descriptor
{
    public CDK_VABCDescriptor()
    {
        super();
        cdkDescriptor_ = new VABCDescriptor();
        initDescriptorsValues();
    }
}

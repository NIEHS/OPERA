package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.MDEDescriptor;

/**
 * Make MDEDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_MDEDescriptor extends CDK_Descriptor
{
    public CDK_MDEDescriptor()
    {
        super();
        cdkDescriptor_ = new MDEDescriptor();
        initDescriptorsValues();
    }
}

package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.TPSADescriptor;

/**
 * Make TPSADescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_TPSADescriptor extends CDK_Descriptor
{
    public CDK_TPSADescriptor()
    {
        super();
        cdkDescriptor_ = new TPSADescriptor();
        initDescriptorsValues();
    }
}

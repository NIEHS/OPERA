package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.ChiClusterDescriptor;

/**
 * Make ChiClusterDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_ChiClusterDescriptor extends CDK_Descriptor
{
    public CDK_ChiClusterDescriptor()
    {
        super();
        cdkDescriptor_ = new ChiClusterDescriptor();
        initDescriptorsValues();
    }
}

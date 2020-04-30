package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.BPolDescriptor;

/**
 * Make BPolDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_BPolDescriptor extends CDK_Descriptor
{
    public CDK_BPolDescriptor()
    {
        super();
        cdkDescriptor_ = new BPolDescriptor();
        initDescriptorsValues();
    }
}

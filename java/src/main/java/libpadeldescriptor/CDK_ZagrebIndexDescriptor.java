package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.ZagrebIndexDescriptor;

/**
 * Make ZagrebIndexDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_ZagrebIndexDescriptor extends CDK_Descriptor
{
    public CDK_ZagrebIndexDescriptor()
    {
        super();
        cdkDescriptor_ = new ZagrebIndexDescriptor();
        initDescriptorsValues();
    }
}

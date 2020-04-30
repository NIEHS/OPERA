package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.FMFDescriptor;

/**
 * Make FMFDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_FMFDescriptor extends CDK_Descriptor
{
    public CDK_FMFDescriptor()
    {
        super();
        cdkDescriptor_ = new FMFDescriptor();
        initDescriptorsValues();
    }
}

package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.MomentOfInertiaDescriptor;

/**
 * Make MomentOfInertiaDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_MomentOfInertiaDescriptor extends CDK_Descriptor
{
    public CDK_MomentOfInertiaDescriptor()
    {
        super();
        cdkDescriptor_ = new MomentOfInertiaDescriptor();
        initDescriptorsValues();
    }
}

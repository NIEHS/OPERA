package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.BCUTDescriptor;

/**
 * Make BCUTDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_BCUTDescriptor extends CDK_Descriptor
{
    public CDK_BCUTDescriptor()
    {
        super();
        cdkDescriptor_ = new BCUTDescriptor();
        initDescriptorsValues();
    }
}

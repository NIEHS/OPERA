package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.MannholdLogPDescriptor;

/**
 * Make MannholdLogPDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_MannholdLogPDescriptor extends CDK_Descriptor
{
    public CDK_MannholdLogPDescriptor()
    {
        super();
        cdkDescriptor_ = new MannholdLogPDescriptor();
        initDescriptorsValues();
    }
}

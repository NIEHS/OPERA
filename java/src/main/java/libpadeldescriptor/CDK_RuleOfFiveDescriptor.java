package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.RuleOfFiveDescriptor;

/**
 * Make RuleOfFiveDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_RuleOfFiveDescriptor extends CDK_Descriptor
{
    public CDK_RuleOfFiveDescriptor()
    {
        super();
        cdkDescriptor_ = new RuleOfFiveDescriptor();
        initDescriptorsValues();
    }
}

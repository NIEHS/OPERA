package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.AromaticBondsCountDescriptor;

/**
 * Make AromaticBondsCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_AromaticBondsCountDescriptor extends CDK_Descriptor
{
    public CDK_AromaticBondsCountDescriptor()
    {
        super();
        cdkDescriptor_ = new AromaticBondsCountDescriptor();
        initDescriptorsValues();
    }
}

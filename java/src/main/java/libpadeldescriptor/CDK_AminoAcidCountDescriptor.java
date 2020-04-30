package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.AminoAcidCountDescriptor;

/**
 * Make AminoAcidCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_AminoAcidCountDescriptor extends CDK_Descriptor
{
    public CDK_AminoAcidCountDescriptor()
    {
        super();
        cdkDescriptor_ = new AminoAcidCountDescriptor();
        initDescriptorsValues();
    }
}

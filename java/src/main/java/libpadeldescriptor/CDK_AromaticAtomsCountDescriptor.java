package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.AromaticAtomsCountDescriptor;

/**
 * Make AromaticAtomsCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_AromaticAtomsCountDescriptor extends CDK_Descriptor
{
    public CDK_AromaticAtomsCountDescriptor()
    {
        super();
        cdkDescriptor_ = new AromaticAtomsCountDescriptor();
        initDescriptorsValues();
    }
}

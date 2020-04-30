package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.IPMolecularLearningDescriptor;

/**
 * Make IPMolecularLearningDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_IPMolecularLearningDescriptor extends CDK_Descriptor
{
    public CDK_IPMolecularLearningDescriptor()
    {
        super();
        cdkDescriptor_ = new IPMolecularLearningDescriptor();
        initDescriptorsValues();
    }
}

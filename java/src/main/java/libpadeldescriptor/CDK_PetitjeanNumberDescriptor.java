package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.PetitjeanNumberDescriptor;

/**
 * Make PetitjeanNumberDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_PetitjeanNumberDescriptor extends CDK_Descriptor
{
    public CDK_PetitjeanNumberDescriptor()
    {
        super();
        cdkDescriptor_ = new PetitjeanNumberDescriptor();
        initDescriptorsValues();
    }
}

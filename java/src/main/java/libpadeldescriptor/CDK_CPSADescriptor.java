package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.CPSADescriptor;

/**
 * Make CPSADescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_CPSADescriptor extends CDK_Descriptor
{
    public CDK_CPSADescriptor()
    {
        super();
        cdkDescriptor_ = new CPSADescriptor();
        initDescriptorsValues();
    }
}

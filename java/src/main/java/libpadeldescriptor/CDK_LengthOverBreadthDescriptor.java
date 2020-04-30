package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.LengthOverBreadthDescriptor;

/**
 * Make LengthOverBreadthDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_LengthOverBreadthDescriptor extends CDK_Descriptor
{
    public CDK_LengthOverBreadthDescriptor()
    {
        super();
        cdkDescriptor_ = new LengthOverBreadthDescriptor();
        initDescriptorsValues();
    }
}

package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.GravitationalIndexDescriptor;

/**
 * Make GravitationalIndexDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_GravitationalIndexDescriptor extends CDK_Descriptor
{
    public CDK_GravitationalIndexDescriptor()
    {
        super();
        cdkDescriptor_ = new GravitationalIndexDescriptor();
        initDescriptorsValues();
    }
}

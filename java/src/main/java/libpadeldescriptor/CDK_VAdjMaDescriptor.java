package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.VAdjMaDescriptor;

/**
 * Make VAdjMaDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_VAdjMaDescriptor extends CDK_Descriptor
{
    public CDK_VAdjMaDescriptor()
    {
        super();
        cdkDescriptor_ = new VAdjMaDescriptor();
        initDescriptorsValues();
    }
}

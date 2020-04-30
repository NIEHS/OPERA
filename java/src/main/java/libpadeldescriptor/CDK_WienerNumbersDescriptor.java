package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.WienerNumbersDescriptor;

/**
 * Make WienerNumbersDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_WienerNumbersDescriptor extends CDK_Descriptor
{
    public CDK_WienerNumbersDescriptor()
    {
        super();
        cdkDescriptor_ = new WienerNumbersDescriptor();
        initDescriptorsValues();
    }
}

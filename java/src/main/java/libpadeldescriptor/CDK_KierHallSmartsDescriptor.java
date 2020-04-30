package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.KierHallSmartsDescriptor;

/**
 * Make KierHallSmartsDescripto in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_KierHallSmartsDescriptor extends CDK_Descriptor
{
    public CDK_KierHallSmartsDescriptor()
    {
        super();
        cdkDescriptor_ = new KierHallSmartsDescriptor();
        initDescriptorsValues();
    }
}

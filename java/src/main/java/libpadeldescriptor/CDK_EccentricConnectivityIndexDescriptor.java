package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.EccentricConnectivityIndexDescriptor;

/**
 * Make EccentricConnectivityIndexDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_EccentricConnectivityIndexDescriptor extends CDK_Descriptor
{
    public CDK_EccentricConnectivityIndexDescriptor()
    {
        super();
        cdkDescriptor_ = new EccentricConnectivityIndexDescriptor();
        initDescriptorsValues();
    }
}

package libpadeldescriptor;

import org.openscience.cdk1.qsar.descriptors.molecular.KappaShapeIndicesDescriptor;

/**
 * Make KappaShapeIndicesDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_KappaShapeIndicesDescriptor extends CDK_Descriptor
{
    public CDK_KappaShapeIndicesDescriptor()
    {
        super();
        cdkDescriptor_ = new KappaShapeIndicesDescriptor();
        initDescriptorsValues();
    }
}

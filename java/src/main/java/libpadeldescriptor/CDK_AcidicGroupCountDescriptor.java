package libpadeldescriptor;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.qsar.descriptors.molecular.AcidicGroupCountDescriptor;

/**
 * Make AcidicGroupCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_AcidicGroupCountDescriptor extends CDK_Descriptor
{
    public CDK_AcidicGroupCountDescriptor()
    {
        super();
        try
        {
            cdkDescriptor_ = new AcidicGroupCountDescriptor();
            initDescriptorsValues();
        }
        catch (CDKException ex)
        {
            Logger.getLogger(CDK_AcidicGroupCountDescriptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

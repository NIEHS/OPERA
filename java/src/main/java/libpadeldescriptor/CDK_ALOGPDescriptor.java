package libpadeldescriptor;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.qsar.descriptors.molecular.ALOGPDescriptor;

/**
 * Make ALOGPDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_ALOGPDescriptor extends CDK_Descriptor
{
    public CDK_ALOGPDescriptor()
    {
        super();
        try
        {
            cdkDescriptor_ = new ALOGPDescriptor();
            initDescriptorsValues();
        }
        catch (CDKException ex)
        {
            Logger.getLogger(CDK_ALOGPDescriptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

package libpadeldescriptor;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.qsar.descriptors.molecular.BasicGroupCountDescriptor;

/**
 * Make BasicGroupCountDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_BasicGroupCountDescriptor extends CDK_Descriptor
{
    public CDK_BasicGroupCountDescriptor()
    {
        super();
        try
        {
            cdkDescriptor_ = new BasicGroupCountDescriptor();
            initDescriptorsValues();
        }
        catch (CDKException ex)
        {
            Logger.getLogger(CDK_BasicGroupCountDescriptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

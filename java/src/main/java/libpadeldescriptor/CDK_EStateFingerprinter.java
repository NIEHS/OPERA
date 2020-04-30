package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.EStateFingerprinter;

/**
 * Make EStateFingerprinter in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_EStateFingerprinter extends CDK_Fingerprint
{
    public CDK_EStateFingerprinter()
    {
        super();
        cdkFingerprinter_ = new EStateFingerprinter();
        initDescriptorsValues();
        this.setPrefix("EStateFP");
    }
}

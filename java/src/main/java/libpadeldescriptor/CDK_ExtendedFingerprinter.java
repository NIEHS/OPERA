package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.ExtendedFingerprinter;

/**
 * Make ExtendedFingerprinter in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_ExtendedFingerprinter extends CDK_Fingerprint
{
    public CDK_ExtendedFingerprinter()
    {
        super();
        cdkFingerprinter_ = new ExtendedFingerprinter();
        initDescriptorsValues();
        this.setPrefix("ExtFP");
    }
}

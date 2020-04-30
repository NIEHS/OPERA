package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.Fingerprinter;

/**
 * Make Fingerprinter in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_Fingerprinter extends CDK_Fingerprint
{
    public CDK_Fingerprinter()
    {
        super();
        cdkFingerprinter_ = new Fingerprinter();
        initDescriptorsValues();
        this.setPrefix("FP");
    }
}

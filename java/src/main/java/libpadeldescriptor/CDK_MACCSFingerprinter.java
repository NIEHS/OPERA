package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.MACCSFingerprinter;

/**
 * Make MACCSFingerprinter in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_MACCSFingerprinter extends CDK_Fingerprint
{
    public CDK_MACCSFingerprinter()
    {
        super();
        cdkFingerprinter_ = new MACCSFingerprinter();
        initDescriptorsValues();
        this.setPrefix("MACCSFP");
    }
}

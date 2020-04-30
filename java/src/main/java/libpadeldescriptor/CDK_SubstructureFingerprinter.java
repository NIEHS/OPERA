package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.SubstructureFingerprinter;

/**
 * Make SubstructureFingerprinter in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_SubstructureFingerprinter extends CDK_Fingerprint
{
    public CDK_SubstructureFingerprinter()
    {
        super();
        cdkFingerprinter_ = new SubstructureFingerprinter();
        initDescriptorsValues();
        this.setPrefix("SubFP");
    }
}

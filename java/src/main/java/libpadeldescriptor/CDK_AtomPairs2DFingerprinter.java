package libpadeldescriptor;

/**
 * Make SubstructureFingerprinter in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_AtomPairs2DFingerprinter extends CDK_Fingerprint
{
    public CDK_AtomPairs2DFingerprinter()
    {
        super();
        cdkFingerprinter_ = new AtomPairs2DFingerprinter();
        initDescriptorsValues();
        this.setPrefix("AD2D");
    }
}

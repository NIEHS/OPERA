package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.GraphOnlyFingerprinter;

/**
 * Make GraphOnlyFingerprinter in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_GraphOnlyFingerprinter extends CDK_Fingerprint
{
    public CDK_GraphOnlyFingerprinter()
    {
        super();
        cdkFingerprinter_ = new GraphOnlyFingerprinter();
        initDescriptorsValues();
        this.setPrefix("GraphFP");
    }
}

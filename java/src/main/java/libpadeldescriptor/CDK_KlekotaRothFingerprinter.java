package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.KlekotaRothFingerprinter;

/**
 * Make SubstructureFingerprinter in CDK runnable in a separate thread.
 * Klekota J, Roth FP. Chemical substructures that enrich for biological activity. Bioinformatics, 2008;24(21):2518-25.
 * Presence of 4860 substructures
 * 
 * @author yapchunwei
 */
public class CDK_KlekotaRothFingerprinter extends CDK_Fingerprint
{
    public CDK_KlekotaRothFingerprinter()
    {
        super();
        cdkFingerprinter_ = new KlekotaRothFingerprinter();
        initDescriptorsValues();
        this.setPrefix("KRFP");
    } 
}

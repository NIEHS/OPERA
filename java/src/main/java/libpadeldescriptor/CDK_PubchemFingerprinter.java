package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.PubchemFingerprinter;

/**
 * Make PubchemFingerprinter in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_PubchemFingerprinter extends CDK_Fingerprint
{
    public CDK_PubchemFingerprinter()
    {
        super();
        cdkFingerprinter_ = new PubchemFingerprinter(); 
        initDescriptorsValues();
        this.setPrefix("PubchemFP");
    }

        /**
     *
     * @return List of descriptor names.
     */
    @Override
    public String[] getDescriptorNames()
    {
        if (descriptorNames_ == null)
        {
            int maxFingerprints = getFingerprintCount();
            descriptorNames_ = new String[maxFingerprints];
            for (int i=0; i<maxFingerprints; ++i)
            {
                descriptorNames_[i] = prefix_ + Integer.toString(i);
            }
        }

        return descriptorNames_;
    }
}

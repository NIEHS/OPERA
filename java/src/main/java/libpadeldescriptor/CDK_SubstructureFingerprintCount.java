package libpadeldescriptor;

import org.openscience.cdk1.fingerprint.StandardSubstructureSets;

/**
 * Make SubstructureFingerprintCount in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_SubstructureFingerprintCount extends CDK_FingerprintCount
{
    public CDK_SubstructureFingerprintCount()
    {
        super();
        cdkDescriptor_ = new SubstructureFingerprintCount();
        String[] smarts;
        try {
            smarts = StandardSubstructureSets.getFunctionalGroupSMARTS();
        } catch (Exception e) {
            smarts = new String[0];
        }

        String[] names = new String[smarts.length];
        for (int i=0; i<names.length; ++i)
        {
            names[i] = "SubFPC" + Integer.toString(i+1);
        }
        ((SubstructureFingerprintCount)cdkDescriptor_).setDescriptorNames(names);
        initDescriptorsValues();
    }
}

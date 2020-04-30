package libpadeldescriptor;

/**
 * Make AtomPairs2DFingerprintCount in CDK runnable in a separate thread.
 *
 * @author yapchunwei
 */
public class CDK_AtomPairs2DFingerprintCount extends CDK_FingerprintCount
{
    public CDK_AtomPairs2DFingerprintCount()
    {
        super();
        cdkDescriptor_ = new AtomPairs2DFingerprintCount();
        initDescriptorsValues();
    }
}

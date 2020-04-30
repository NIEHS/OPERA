package libpadeldescriptor;

/**
 * Make PetitjeanShapeIndexDescriptor in CDK runnable in a separate thread.
 * 
 * @author yapchunwei
 */
public class CDK_PetitjeanShapeIndexDescriptor extends CDK_Descriptor
{
    public CDK_PetitjeanShapeIndexDescriptor()
    {
        super();
        cdkDescriptor_ = new PaDELPetitjeanShapeIndexDescriptor();
        initDescriptorsValues();
    }
}

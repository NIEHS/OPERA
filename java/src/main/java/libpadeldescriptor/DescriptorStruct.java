package libpadeldescriptor;

/**
 * Structure for a descriptor type.
 * 
 * @author yapchunwei
 */
public class DescriptorStruct
{
    static public final String TYPE_2D = "2D";
    static public final String TYPE_3D = "3D";
    static public final String TYPE_FINGERPRINT = "Fingerprint";

    private String name;
    private String type;
    private boolean active;

    public DescriptorStruct(String name, String type)
    {
        this(name, type, true);
    }

    public DescriptorStruct(String name, String type, boolean active)
    {
        this.name = name;
        this.type = type;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DescriptorStruct other = (DescriptorStruct) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 43 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }
}


package libpadeljobs;

/**
 * A job to be done. To be extended by other classes.
 * 
 * @author yapchunwei
 */
public class Job
{
    protected int id = 0;
    protected long starttime = 0;
    protected long endtime = 0;
    
    public Job(int id)
    {
        this.id = id;
    }
    
    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Job other = (Job) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.id;
        return hash;
    }

    public final int getId() {
        return id;
    }

    public final long getEndtime()
    {
        return endtime;
    }

    public final void setEndtime(long endtime)
    {
        this.endtime = endtime;
    }

    public final long getStarttime()
    {
        return starttime;
    }

    public final void setStarttime(long starttime)
    {
        this.starttime = starttime;
    }
}

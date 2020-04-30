package ambit2.base.interfaces;

import ambit2.base.exceptions.AmbitException;
import java.io.Serializable;

public abstract interface IProcessor<Target, Result>
  extends Serializable
{
  public abstract Result process(Target paramTarget)
    throws AmbitException;
  
  public abstract boolean isEnabled();
  
  public abstract void setEnabled(boolean paramBoolean);
  
  public abstract long getID();
}


/* Location:              /Users/jasonphillips/Desktop/ambit2-base-2.4.7-SNAPSHOT.jar!/ambit2/base/interfaces/IProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
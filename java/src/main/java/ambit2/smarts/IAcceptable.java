package ambit2.smarts;

import java.util.Vector;
import org.openscience.cdk1.interfaces.IAtom;

public abstract interface IAcceptable
{
  public abstract boolean accept(Vector<IAtom> paramVector);
}


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/IAcceptable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */

/*    */ package ambit2.smarts;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import org.openscience.cdk1.interfaces.IAtom;
/*    */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*    */ import org.openscience.cdk1.interfaces.IBond;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TopLayer
/*    */ {
/*    */   public static final String TLProp = "TL";
/* 40 */   public Vector<IAtom> atoms = new Vector();
/* 41 */   public Vector<IBond> bonds = new Vector();
/*    */   
/*    */   public static void setAtomTopLayers(IAtomContainer container, Object propObj)
/*    */   {
/* 45 */     int nAtom = container.getAtomCount();
/* 46 */     int nBond = container.getBondCount();
/* 47 */     for (int i = 0; i < nAtom; i++) {
/* 48 */       container.getAtom(i).setProperty(propObj, new TopLayer());
/*    */     }
/* 50 */     for (int i = 0; i < nBond; i++)
/*    */     {
/* 52 */       IBond bond = container.getBond(i);
/* 53 */       IAtom at0 = bond.getAtom(0);
/* 54 */       IAtom at1 = bond.getAtom(1);
/* 55 */       TopLayer tl0 = (TopLayer)at0.getProperty(propObj);
/* 56 */       TopLayer tl1 = (TopLayer)at1.getProperty(propObj);
/* 57 */       tl0.atoms.add(at1);
/* 58 */       tl0.bonds.add(bond);
/* 59 */       tl1.atoms.add(at0);
/* 60 */       tl1.bonds.add(bond);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 66 */     StringBuffer sb = new StringBuffer();
/* 67 */     for (int i = 0; i < this.atoms.size(); i++) {
/* 68 */       sb.append("(" + SmartsHelper.atomToString((IAtom)this.atoms.get(i)) + ", " + SmartsHelper.bondToString((IBond)this.bonds.get(i)) + ") ");
/*    */     }
/* 70 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/TopLayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */

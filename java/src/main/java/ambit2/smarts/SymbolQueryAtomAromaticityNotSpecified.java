/*    */ package ambit2.smarts;
/*    */ 
/*    */ import org.openscience.cdk1.interfaces.IAtom;
/*    */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSAtom;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SymbolQueryAtomAromaticityNotSpecified
/*    */   extends SMARTSAtom
/*    */ {
/*    */   private static final long serialVersionUID = -18035318274561647L;
/*    */   
/*    */   public boolean matches(IAtom atom)
/*    */   {
/* 17 */     if (getSymbol().equals(atom.getSymbol())) {
/* 18 */       return true;
/*    */     }
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 24 */     return "SymbolQueryAtomAromaticityNotSpecified()";
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SymbolQueryAtomAromaticityNotSpecified.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */

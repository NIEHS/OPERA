/*    */ package ambit2.smarts;
/*    */ 
/*    */ import org.openscience.cdk1.interfaces.IAtom;
/*    */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSAtom;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AliphaticSymbolQueryAtom
/*    */   extends SMARTSAtom
/*    */ {
/*    */   private static final long serialVersionUID = -18004315234561145L;
/*    */   
/*    */   public boolean matches(IAtom atom)
/*    */   {
/* 45 */     if ((!atom.getFlag(5)) && (getSymbol().equals(atom.getSymbol())))
/*    */     {
/* 47 */       return true;
/*    */     }
/*    */     
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 54 */     return "AliphaticSymbolQueryAtom()";
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/AliphaticSymbolQueryAtom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */

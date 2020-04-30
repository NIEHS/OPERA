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
/*    */ public class AromaticSymbolQueryAtom
/*    */   extends SMARTSAtom
/*    */ {
/*    */   private static final long serialVersionUID = -56452386792649224L;
/*    */   
/*    */   public boolean matches(IAtom atom)
/*    */   {
/* 42 */     if ((atom.getFlag(5)) && (getSymbol().equals(atom.getSymbol())))
/*    */     {
/* 44 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 48 */     return false;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 53 */     return "AromaticSymbolQueryAtom()";
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/AromaticSymbolQueryAtom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */

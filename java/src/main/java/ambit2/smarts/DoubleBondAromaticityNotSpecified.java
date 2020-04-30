/*    */ package ambit2.smarts;
/*    */ 
/*    */ import org.openscience.cdk1.interfaces.IBond;
/*    */ import org.openscience.cdk1.interfaces.IBond.Order;
/*    */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSBond;
/*    */ 
/*    */ 
/*    */ public class DoubleBondAromaticityNotSpecified
/*    */   extends SMARTSBond
/*    */ {
/*    */   private static final long serialVersionUID = -9341431640874352L;
/*    */   
/*    */   public boolean matches(IBond bond)
/*    */   {
/* 15 */     if (bond.getOrder() == IBond.Order.DOUBLE) {
/* 16 */       return true;
/*    */     }
/* 18 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/DoubleBondAromaticityNotSpecified.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */

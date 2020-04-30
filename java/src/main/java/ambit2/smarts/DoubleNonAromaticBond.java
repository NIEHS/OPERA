/*    */ package ambit2.smarts;
/*    */ 
/*    */ import org.openscience.cdk1.interfaces.IBond;
/*    */ import org.openscience.cdk1.interfaces.IBond.Order;
/*    */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSBond;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DoubleNonAromaticBond
/*    */   extends SMARTSBond
/*    */ {
/*    */   private static final long serialVersionUID = -93413322217784352L;
/*    */   
/*    */   public boolean matches(IBond bond)
/*    */   {
/* 16 */     if ((bond.getOrder() == IBond.Order.DOUBLE) && 
/* 17 */       (!bond.getFlag(5))) {
/* 18 */       return true;
/*    */     }
/* 20 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/DoubleNonAromaticBond.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
